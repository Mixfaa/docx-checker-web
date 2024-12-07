package com.mixfa.docx_checker_web.service;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import com.mixfa.docx_checker_web.docxchecker.documentchecker.DocumentChecker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Service
public class DocxCheckerService {
    private final List<DocumentChecker> documentCheckers;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DocxCheckerService(List<DocumentChecker> documentCheckers) {
        this.documentCheckers = documentCheckers;
    }

    public List<String> checkDocxFile(MultipartFile file, Locale locale) {
        var errorsCollector = new ErrorsCollector.ListErrorsCollector();

        try (var document = new XWPFDocument(file.getInputStream())) {
            for (var documentChecker : documentCheckers) {
                try {
                    documentChecker.checkElement(document, errorsCollector);
                } catch (Exception ex) {
                    logger.error(ex.getLocalizedMessage());
                }
            }
        } catch (IOException e) {
            logger.error("IO exception reading file");
        }

        return errorsCollector.getErrors()
                .stream()
                .map(errorTemplate -> errorTemplate.formatError(locale))
                .toList();
    }
}
