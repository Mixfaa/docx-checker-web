package com.mixfa.docx_checker_web.docxchecker.model;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public record ErrorTemplate(
        String templateCode,
        Object[] args) {
    final static Locale defaultLocale = Locale.ENGLISH; // en
    final static String RESOURCE_NAME = "errors-resource";
    final static ResourceBundle DEFAULT_RESOURCE_BUNDLE = ResourceBundle.getBundle(RESOURCE_NAME, defaultLocale);

    public String formatError(Locale locale) {
        if (locale.equals(defaultLocale))
            return DEFAULT_RESOURCE_BUNDLE.getString(templateCode).formatted(args);

        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        } catch (MissingResourceException exception) {
            resourceBundle = DEFAULT_RESOURCE_BUNDLE;
        }

        return resourceBundle.getString(templateCode).formatted(args);
    }
}
