package com.mixfa.docx_checker_web.docxchecker;

import com.mixfa.docx_checker_web.docxchecker.error.ErrorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ErrorsCollector {
    void addError(ErrorTemplate error);

    default void addError(String errorTemplateCode, Object... args) {
        addError(new ErrorTemplate(errorTemplateCode, args));
    }

    Collection<ErrorTemplate> getErrors();

    class ListErrorsCollector implements ErrorsCollector {
        private final List<ErrorTemplate> errors = new ArrayList<>();

        @Override
        public void addError(ErrorTemplate error) {
            errors.add(error);
        }

        @Override
        public Collection<ErrorTemplate> getErrors() {
            return errors;
        }
    }
}
