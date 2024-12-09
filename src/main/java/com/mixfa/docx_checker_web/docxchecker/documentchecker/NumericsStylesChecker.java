package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NumericsStylesChecker implements DocumentChecker {
    private static final Map<String, Boolean> NUMERICS_STYLES = Map.of("Numeric1", false, "Numeric10", false, "Numerica", false);
    private static final String MORE_THAN_ONE_NUMERIC_STYLE_USED = "morethanonenumstyleused";

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
        var numericStylesUsage = new HashMap<>(NUMERICS_STYLES);

        for (XWPFParagraph paragraph : document.getParagraphs())
            numericStylesUsage.computeIfPresent(paragraph.getStyle(), (_, _) -> true);

        var moreThanOneStyleUsed = numericStylesUsage.values()
                .stream()
                .filter(Boolean::booleanValue).count() > 1;

        if (moreThanOneStyleUsed)
            errorsCollector.addError(MORE_THAN_ONE_NUMERIC_STYLE_USED);
    }
}
