package com.mixfa.docx_checker_web.docxchecker;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ErrorsCollector {
    void addError(ErrorTemplate error);

    Collection<ErrorTemplate> getErrors();

    class ListErrorsCollector implements ErrorsCollector {
        private final List<ErrorTemplate> errors = new ArrayList<>();

        @Override
        public void addError(ErrorTemplate error) {
            errors.add(error);
        }

        @Override
        public Collection<ErrorTemplate> getErrors() {
            return Collections.unmodifiableList(errors);
        }
    }
}
