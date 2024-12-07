package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.*;

/**
 * NOT Implemented
 */
public class NumberingChecker implements DocumentChecker {
    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
//        var hfPolicy = document.getHeaderFooterPolicy();
//        var fpHeader = hfPolicy.getFirstPageHeader();
//        var fpFooter = hfPolicy.getFirstPageFooter();
//
//        var pgs = document.getDocument().getBody()
//                .getSectPr()
//                .getPgNumType().getStart().intValue();
//
//        System.out.println(pgs);
//
//        if (fpHeader != null)
//            errorsCollector.addError("Header found on first page");
//
//        if (fpFooter != null)
//            errorsCollector.addError("Footer found on first page");
    }
}
