package com.mixfa.docx_checker_web.docxchecker;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class ListErrorsCollector implements ErrorsCollector {
    private final List<ErrorTemplate> errors;

    public ListErrorsCollector() {
        errors = new ArrayList<>();
    }

    private ListErrorsCollector(List<ErrorTemplate> list) {
        this.errors = list;
    }

    public static ListErrorsCollector newThreadSafe() {
        return new ListErrorsCollector(new CopyOnWriteArrayList<>());
    }

    public static ListErrorsCollector newWithListImpl(Supplier<List<ErrorTemplate>> implSupplier) {
        return new ListErrorsCollector(implSupplier.get());
    }

    @Override
    public void addError(ErrorTemplate error) {
        errors.add(error);
    }

    @Override
    public Collection<ErrorTemplate> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
