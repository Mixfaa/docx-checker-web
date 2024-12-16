package com.mixfa.docx_checker_web.docxchecker;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public interface DocxElementChecker<T> {
    void checkElement(T element, DocxCheckingContext context);

    default boolean supports(Object element) {
        return targetElementType().isInstance(element);
    }

    Class<T> targetElementType();

    interface ParagraphChecker extends DocxElementChecker<XWPFParagraph> {
        @Override
        default Class<XWPFParagraph> targetElementType() {
            return XWPFParagraph.class;
        }
    }
    interface BodyElementChecker extends DocxElementChecker<IBodyElement> {
        @Override
        default Class<IBodyElement> targetElementType() {
            return IBodyElement.class;
        }
    }
}
