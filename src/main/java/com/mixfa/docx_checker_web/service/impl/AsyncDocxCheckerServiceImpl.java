package com.mixfa.docx_checker_web.service.impl;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import com.mixfa.docx_checker_web.docxchecker.ListErrorsCollector;
import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncDocxCheckerServiceImpl implements DocxCheckerService {
    private final List<DocxElementChecker<?>> checkers;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    @Override
    public Collection<ErrorTemplate> checkDocxFile(InputStream docxFile) {
        AsyncModifiableContext context = null;
        List<Callable<Void>> tasks = new ArrayList<>();
        try (var document = new XWPFDocument(docxFile)) {
            context = new AsyncModifiableContext(
                    document,
                    ListErrorsCollector.newThreadSafe()
            );
            final var finalContext = context;
            for (var checker : checkers) {
                if (!checker.supports(document)) continue;

                tasks.add(() -> {
                    try {
                        @SuppressWarnings("unchecked") var castedChecker = (DocxElementChecker<XWPFDocument>) checker;
                        castedChecker.checkElement(document, finalContext);
                    } catch (Exception ex) {
                        log.error(ex.getLocalizedMessage());
                    }
                    return null;
                });
            }

            var bodyElements = document.getBodyElements();
            for (int i = 0; i < bodyElements.size(); i++) {
                var bodyElement = bodyElements.get(i);

                for (var checker : checkers) {
                    if (!checker.supports(bodyElement)) continue;

                    int finalI = i;
                    tasks.add(() -> {
                        finalContext.setCurrentElementIndex(finalI);
                        try {
                            @SuppressWarnings("unchecked") var castedChecker = (DocxElementChecker<IBodyElement>) checker;
                            castedChecker.checkElement(bodyElement, finalContext);
                        } catch (Exception ex) {
                            log.error(ex.getLocalizedMessage());
                        }
                        return null;
                    });
                }
            }
        } catch (IOException e) {
            log.error("IO exception reading file");
        } catch (RuntimeException runtimeException) {
            log.error(runtimeException.getLocalizedMessage());
        }
        if (context == null) return List.of();

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException interruptedException) {
            log.error(interruptedException.getLocalizedMessage());
        }

        return context.errorsCollector().getErrors();
    }

    @RequiredArgsConstructor
    private static class AsyncModifiableContext implements DocxCheckingContext {
        private final XWPFDocument document;
        private final ErrorsCollector errorsCollector;
        private final ThreadLocal<Integer> threadLocalElementIndex = new ThreadLocal<>();

        @Override
        public ErrorsCollector errorsCollector() {
            return errorsCollector;
        }

        @Override
        public int elementIndex() {
            return threadLocalElementIndex.get();
        }

        public void setCurrentElementIndex(int index) {
            threadLocalElementIndex.set(index);
        }

        @Override
        public XWPFDocument document() {
            return document;
        }
    }
}
