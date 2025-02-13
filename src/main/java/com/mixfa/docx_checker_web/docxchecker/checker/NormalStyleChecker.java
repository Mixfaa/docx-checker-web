package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class NormalStyleChecker implements DocxElementChecker.DocumentChecker {


    @Override
    public void checkElement(XWPFDocument document, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        XWPFStyle style = document.getStyles().getStyle("Normal");

        if (style == null) {
            errorsCollector.addError(ErrorTemplates.noNormalStyle());
            return;
        }

        CTStyle ctStyle = style.getCTStyle();
        CTFonts fonts = ctStyle.getRPr().getRFontsArray(0);
        CTSpacing spacing = ctStyle.getPPr().getSpacing();
        CTInd ind = ctStyle.getPPr().getInd();

        // Font name and size
        String fontName = fonts.getAscii();

        if (!StringUtils.equals(fontName, "Times New Roman"))
            errorsCollector.addError(ErrorTemplates.wrongFont());

        // Line spacing
        int lineSpacingType = spacing.getLineRule().intValue();
        int lineSpacing = ((BigInteger) spacing.getLine()).intValue() / 20; // in points

        // First line indent
        int firstLineIndent = ((BigInteger) ind.getFirstLine()).intValue() / 20; // in points

        // Check if the style matches the expected values
        boolean isSpacingCorrect = lineSpacingType == 1 && lineSpacing == 18; // 1.5 line spacing in twips
        if (!isSpacingCorrect)
            errorsCollector.addError(ErrorTemplates.wrongSpacing());

        boolean isFirstLineIndentCorrect = firstLineIndent == 35; // 1.25 cm in twips
        if (!isFirstLineIndentCorrect)
            errorsCollector.addError(ErrorTemplates.wrongFirstLineIndent());

    }
}
