package com.mixfa.docx_checker_web.docxchecker;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;

import java.util.Collection;

public interface ErrorsCollector {
    void addError(ErrorTemplate error);

    Collection<ErrorTemplate> getErrors();
}
