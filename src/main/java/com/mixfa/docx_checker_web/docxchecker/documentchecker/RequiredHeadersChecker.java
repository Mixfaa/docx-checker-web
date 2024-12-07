package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequiredHeadersChecker implements DocumentChecker {
    private static final String HEADER_STYLE = "Headerofreportpart";
    private static final String REQUIRED_HEADER_NOT_FOUND = "reqheadernotfound";
    private static final Map<String, Integer> REQUIRED_HEADERS = Map.of("ЗМІСТ", 0, "ВСТУП", 0);

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
        var headersCounter = new HashMap<>(REQUIRED_HEADERS);

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            if (!StringUtils.equals(paragraph.getStyle(), HEADER_STYLE))
                continue;

            headersCounter.computeIfPresent(paragraph.getText(), (k, v) -> v + 1);
        }

        headersCounter.forEach((header, count) -> {
            if (count != 0) return;

            errorsCollector.addError(REQUIRED_HEADER_NOT_FOUND, header);
        });
    }
}
