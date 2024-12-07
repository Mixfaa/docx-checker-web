package com.mixfa.docx_checker_web.docxchecker.error;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public record ErrorTemplate(
        String templateCode,
        Object[] args) {
    final static Locale defaultLocale = Locale.ENGLISH; // en
    final static String RESOURCE_NAME = "errors-resource";

    public String formatError(Locale locale) {
        ResourceBundle resourceBundle;
        try {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, locale);
        } catch (MissingResourceException exception) {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_NAME, defaultLocale);
        }

        return resourceBundle.getString(templateCode).formatted(args);
    }
}
