package com.mixfa.docx_checker_web.docxchecker;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

public interface DocxCheckingContext {
    ErrorsCollector errorsCollector();
    int currentElementIndex();
    XWPFDocument document();

    class ModifiableContext implements DocxCheckingContext {
        private final XWPFDocument document;
        private final ErrorsCollector errorsCollector;
        private int currentElementIndex = 0;

        public ModifiableContext(XWPFDocument document, ErrorsCollector errorsCollector) {
            this.document = document;
            this.errorsCollector = errorsCollector;
        }

        public void setCurrentElementIndex(int currentElementIndex) {
            this.currentElementIndex = currentElementIndex;
        }

        @Override
        public ErrorsCollector errorsCollector() {
            return errorsCollector;
        }

        @Override
        public int currentElementIndex() {
            return currentElementIndex;
        }

        @Override
        public XWPFDocument document() {
            return document;
        }
    }
}
