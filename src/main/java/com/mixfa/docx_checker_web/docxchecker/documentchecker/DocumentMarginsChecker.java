package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

import static com.mixfa.docx_checker_web.misc.Utils.smToTwips;

@Component
public class DocumentMarginsChecker implements DocumentChecker {
    private final static String SECTPTR_NOT_FOUND = "sectptrnf";
    private final static String MARGINS_NOT_FOUND = "marginsnotfound";
    private final static String LEFT_MARGIN_INVALID = "leftmargininvdalid";
    private final static String RIGHT_MARGIN_INVALID = "rightmargininvalid";
    private final static String TOP_MARGIN_INVALID = "topmargininvalid";
    private final static String BOTTOM_MARGIN_INVALID = "bottommargininvalid";

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
        var sectPtr = document.getDocument().getBody().getSectPr();
        if (sectPtr == null) {
            errorsCollector.addError(SECTPTR_NOT_FOUND);
            return;
        }

        var margins = sectPtr.getPgMar();
        if (margins == null) {
            errorsCollector.addError(MARGINS_NOT_FOUND);
            return;
        }

        BigInteger leftMargin = (BigInteger) margins.getLeft();
        BigInteger rightMargin = (BigInteger) margins.getRight();
        BigInteger topMargin = (BigInteger) margins.getTop();
        BigInteger bottomMargin = (BigInteger) margins.getBottom();

        if (!Objects.equals(leftMargin, smToTwips(3)))
            errorsCollector.addError(LEFT_MARGIN_INVALID);
        if (!Objects.equals(rightMargin, smToTwips(1.5)))
            errorsCollector.addError(RIGHT_MARGIN_INVALID);
        if (!Objects.equals(topMargin, smToTwips(2.0)))
            errorsCollector.addError(TOP_MARGIN_INVALID);
        if (!Objects.equals(bottomMargin, smToTwips(2.0)))
            errorsCollector.addError(BOTTOM_MARGIN_INVALID);
    }
}

