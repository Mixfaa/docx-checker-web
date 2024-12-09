package com.mixfa.docx_checker_web.service;

import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Service
public class DocxCheckerService {
    private final List<DocxElementChecker<?>> checkers;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DocxCheckerService(List<DocxElementChecker<?>> checkers) {
        this.checkers = checkers;
    }

    public List<String> checkDocxFile(InputStream inputStream, Locale locale) {
        var errorsCollector = new ErrorsCollector.ListErrorsCollector();

        try (var document = new XWPFDocument(inputStream)) {
            for (var checker : checkers) {
                if (!checker.supports(document))
                    continue;

                try {
                    @SuppressWarnings("unchecked")
                    var castedChecker = (DocxElementChecker<XWPFDocument>) checker;
                    castedChecker.checkElement(document, errorsCollector);
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage());
                }
            }

            for (var bodyElement : document.getBodyElements()) {
                for (var checker : checkers) {
                    if (!checker.supports(bodyElement))
                        continue;

                    try {
                        @SuppressWarnings("unchecked")
                        var castedChecker = (DocxElementChecker<IBodyElement>) checker;
                        castedChecker.checkElement(bodyElement, errorsCollector);
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

        return errorsCollector.getErrors()
                .stream()
                .map(errorTemplate -> errorTemplate.formatError(locale))
                .toList();
    }
}
