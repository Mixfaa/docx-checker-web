package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequiredHeadersChecker implements DocxElementChecker.DocumentChecker {
    private static final String HEADER_STYLE = "Headerofreportpart";
    private static final Map<String, Boolean> REQUIRED_HEADERS = Map.of("ЗМІСТ", false, "ВСТУП", false);

    @Override
    public void checkElement(XWPFDocument document, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        var headersCounter = new HashMap<>(REQUIRED_HEADERS);

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            if (!StringUtils.equals(paragraph.getStyle(), HEADER_STYLE))
                continue;

            headersCounter.computeIfPresent(paragraph.getText(), (_, _) -> true);
        }

        headersCounter.forEach((header, used) -> {
            if (used) return;
            errorsCollector.addError(ErrorTemplates.requiredHeaderNotFound(header));
        });
    }
}
