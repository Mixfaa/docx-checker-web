package com.mixfa.docx_checker_web.misc;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    private Utils() {
    }

    public static BigInteger smToTwips(double sm) {
        return BigInteger.valueOf(Math.round(sm * 567.0));
    }


    public static Map<ErrorTemplate, Long> countSimilarErrors(Collection<ErrorTemplate> errors) {
        return errors.stream()
                .collect(
                        Collectors.toMap(
                                (k) -> k,
                                (v) -> errors.stream().filter(err -> err.equals(v)).count(),
                                Math::max
                        )
                );
    }

    public static List<String> makeUserFriendlyErrorMessages(Map<ErrorTemplate, Long> errors, Locale locale) {
        return errors
                .entrySet()
                .stream()
                .map(entry -> entry.getKey().formatError(locale) + " x " + entry.getValue())
                .toList();
    }

    public static List<String> makeUserFriendlyErrorMessages(Collection<ErrorTemplate> errors, Locale locale) {
        return makeUserFriendlyErrorMessages(countSimilarErrors(errors), locale);
    }
}
