package com.mixfa.docx_checker_web.docxchecker;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface DocxCheckingContext {
    ErrorsCollector errorsCollector();
    int elementIndex();
    XWPFDocument document();
}
