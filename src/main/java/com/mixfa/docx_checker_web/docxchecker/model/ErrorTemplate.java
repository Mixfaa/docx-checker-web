package com.mixfa.docx_checker_web.docxchecker.model;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

public record ErrorTemplate(
        String templateCode,
        Object[] args) {
    final static Locale defaultLocale = Locale.ENGLISH; // en
    final static String RESOURCE_NAME = "errors-resource";
    final static ResourceBundle DEFAULT_RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_NAME, defaultLocale);

    public ErrorTemplate(String templateCode) {
        this(templateCode, null);
    }

    public static ErrorTemplate createWithVararg(String templateCode, Object ... args) {
        return new ErrorTemplate(templateCode, args);
    }

    public String formatError(Locale locale) {
        locale = Objects.requireNonNullElse(locale, defaultLocale);
        if (locale.equals(defaultLocale))
            return DEFAULT_RESOURCE_BUNDLE.getString(templateCode).formatted(args);

        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
            if (!resourceBundle.containsKey(templateCode))
                resourceBundle = DEFAULT_RESOURCE_BUNDLE;
        } catch (MissingResourceException exception) {
            resourceBundle = DEFAULT_RESOURCE_BUNDLE;
        }

        var templateString = resourceBundle.getString(templateCode);
        return args == null ? templateString : templateString.formatted(args);
    }
}
