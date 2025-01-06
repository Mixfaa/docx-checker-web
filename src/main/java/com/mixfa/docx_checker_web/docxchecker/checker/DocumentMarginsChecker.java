package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Objects;

import static com.mixfa.docx_checker_web.misc.Utils.smToTwips;

@Component
public class DocumentMarginsChecker implements DocxElementChecker.DocumentChecker {


    @Override
    public void checkElement(XWPFDocument document, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        var sectPtr = document.getDocument().getBody().getSectPr();
        if (sectPtr == null) {
            errorsCollector.addError(ErrorTemplates.sectPtrNotFound());
            return;
        }

        var margins = sectPtr.getPgMar();
        if (margins == null) {
            errorsCollector.addError(ErrorTemplates.marginsNotFound());
            return;
        }

        BigInteger leftMargin = (BigInteger) margins.getLeft();
        BigInteger rightMargin = (BigInteger) margins.getRight();
        BigInteger topMargin = (BigInteger) margins.getTop();
        BigInteger bottomMargin = (BigInteger) margins.getBottom();

        if (!Objects.equals(leftMargin, smToTwips(3)))
            errorsCollector.addError(ErrorTemplates.leftMarginInvalid());
        if (!Objects.equals(rightMargin, smToTwips(1.5)))
            errorsCollector.addError(ErrorTemplates.rightMarginInvalid());
        if (!Objects.equals(topMargin, smToTwips(2.0)))
            errorsCollector.addError(ErrorTemplates.topMarginInvalid());
        if (!Objects.equals(bottomMargin, smToTwips(2.0)))
            errorsCollector.addError(ErrorTemplates.bottomMarginInvalid());
    }
}

