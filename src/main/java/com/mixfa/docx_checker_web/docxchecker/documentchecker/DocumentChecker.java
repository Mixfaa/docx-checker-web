package com.mixfa.docx_checker_web.docxchecker.documentchecker;


import com.mixfa.docx_checker_web.docxchecker.ElementChecker;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface DocumentChecker extends ElementChecker<XWPFDocument> {
    @Override
    default Class<XWPFDocument> targetElementType() {
        return XWPFDocument.class;
    }
}
