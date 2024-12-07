package com.mixfa.docx_checker_web.docxchecker;

public interface ElementChecker<T> {
    void checkElement(T element, ErrorsCollector errorsCollector);

    default boolean supports(Object element) {
        return targetElementType().isInstance(element);
    }

    Class<T> targetElementType();
}
