package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NumericsStylesChecker implements DocxElementChecker.DocumentChecker {
    private static final Map<String, Boolean> NUMERICS_STYLES = Map.of("Numeric1", false, "Numeric10", false, "Numerica", false);

    @Override
    public void checkElement(XWPFDocument document, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        var numericStylesUsage = new HashMap<>(NUMERICS_STYLES);

        for (XWPFParagraph paragraph : document.getParagraphs())
            numericStylesUsage.computeIfPresent(paragraph.getStyle(), (_, _) -> true);

        var moreThanOneStyleUsed = numericStylesUsage.values()
                .stream()
                .filter(Boolean::booleanValue).count() > 1;

        if (moreThanOneStyleUsed)
            errorsCollector.addError(ErrorTemplates.moreThanOneNumericStyleUsed());
    }
}
