package com.mixfa.docx_checker_web.service.impl;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import com.mixfa.docx_checker_web.docxchecker.ListErrorsCollector;
import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import lombok.Setter;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Service
public class SyncDocxCheckerServiceImpl implements DocxCheckerService {
    private final List<DocxElementChecker<?>> checkers;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public SyncDocxCheckerServiceImpl(List<DocxElementChecker<?>> checkers) {
        this.checkers = checkers;
    }

    @Override
    public Collection<ErrorTemplate> checkDocxFile(InputStream inputStream) {
        ModifiableContext context = null;
        try (var document = new XWPFDocument(inputStream)) {
            context = new ModifiableContext(
                    document,
                    new ListErrorsCollector()
            );
            for (var checker : checkers) {
                if (!checker.supports(document)) continue;

                try {
                    @SuppressWarnings("unchecked") var castedChecker = (DocxElementChecker<XWPFDocument>) checker;
                    castedChecker.checkElement(document, context);
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage());
                }
            }

            var bodyElements = document.getBodyElements();
            for (int i = 0; i < bodyElements.size(); i++) {
                var bodyElement = bodyElements.get(i);
                context.setCurrentElementIndex(i);

                for (var checker : checkers) {
                    if (!checker.supports(bodyElement)) continue;

                    try {
                        @SuppressWarnings("unchecked") var castedChecker = (DocxElementChecker<IBodyElement>) checker;
                        castedChecker.checkElement(bodyElement, context);
                    } catch (Exception ex) {
                        logger.error(ex.getLocalizedMessage());
                    }
                }
            }
        } catch (IOException e) {
            logger.error("IO exception reading file");
        } catch (RuntimeException runtimeException) {
            logger.error(runtimeException.getLocalizedMessage());
        }

        if (context == null) return List.of();
        return context.errorsCollector().getErrors();
    }

    private static class ModifiableContext implements DocxCheckingContext {
        private final XWPFDocument document;
        private final ErrorsCollector errorsCollector;
        @Setter
        private int currentElementIndex = 0;

        public ModifiableContext(XWPFDocument document, ErrorsCollector errorsCollector) {
            this.document = document;
            this.errorsCollector = errorsCollector;
        }

        @Override
        public ErrorsCollector errorsCollector() {
            return errorsCollector;
        }

        @Override
        public int elementIndex() {
            return currentElementIndex;
        }

        @Override
        public XWPFDocument document() {
            return document;
        }
    }
}
