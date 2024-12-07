package com.mixfa.docx_checker_web.docxchecker;

import org.apache.poi.xwpf.usermodel.IBodyElement;

public interface ElementChecker<T> {
    void checkElement(T element, ErrorsCollector errorsCollector);

    default boolean supports(IBodyElement element) {
        return targetElementType().isInstance(element);
    }

    Class<T> targetElementType();
}
