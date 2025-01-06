package com.mixfa.docx_checker_web.docxchecker;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class ErrorTemplates {
    private static final String NO_TABLE_NUM_BEFORE_TABLE = "notablenum";
    private static final String TABLE_NUM_PATTERN_ERR = "tablenumpatternerr";
    private static final String NONL_BEFORE_TABLE_NUM = "nonewlinebeforetablenum";
    private static final String ADDON_TITLE_NOT_MATCHES = "addontitleinvalid";
    private static final String ADDON_STYLE_NOT_MATCHES = "addonstyleinvalid";
    private final static String SECTPTR_NOT_FOUND = "sectptrnf";
    private final static String MARGINS_NOT_FOUND = "marginsnotfound";
    private final static String LEFT_MARGIN_INVALID = "leftmargininvdalid";
    private final static String RIGHT_MARGIN_INVALID = "rightmargininvalid";
    private final static String TOP_MARGIN_INVALID = "topmargininvalid";
    private final static String BOTTOM_MARGIN_INVALID = "bottommargininvalid";
    private static final String NONL_BEFORE_PICTURE = "nonewlinebeforepicture";
    private static final String NO_NORMAL_STYLE = "nonormalstyle";
    private static final String WRONG_FONT = "wrongfont";
    private static final String WRONG_SPACING = "wrongspacing";
    private static final String WRONG_FIRST_LINE_INDENT = "wrongindent";
    private static final String MORE_THAN_ONE_NUMERIC_STYLE_USED = "morethanonenumstyleused";
    private static final String NO_PICTURE_NUMBER = "nopicturenumber";
    private static final String PICTURE_NUMBER_TEXT_ERR = "picturenumbertext";
    private static final String NONL_AFTER_PICTURE_NUMBER = "nonewlineafterpicturenumber";
    private static final String REQUIRED_HEADER_NOT_FOUND = "reqheadernotfound";

    @Getter
    private static final ErrorTemplate noTableNumberBeforeTable = new ErrorTemplate(NO_TABLE_NUM_BEFORE_TABLE);

    @Getter
    private static final ErrorTemplate noNewlineBeforeTableNumber = new ErrorTemplate(NONL_BEFORE_TABLE_NUM);

    @Getter
    private static final ErrorTemplate addonStyleNotMatches = new ErrorTemplate(ADDON_STYLE_NOT_MATCHES);

    @Getter
    private static final ErrorTemplate sectPtrNotFound = new ErrorTemplate(SECTPTR_NOT_FOUND);

    @Getter
    private static final ErrorTemplate marginsNotFound = new ErrorTemplate(MARGINS_NOT_FOUND);

    @Getter
    private static final ErrorTemplate leftMarginInvalid = new ErrorTemplate(LEFT_MARGIN_INVALID);

    @Getter
    private static final ErrorTemplate rightMarginInvalid = new ErrorTemplate(RIGHT_MARGIN_INVALID);

    @Getter
    private static final ErrorTemplate topMarginInvalid = new ErrorTemplate(TOP_MARGIN_INVALID);

    @Getter
    private static final ErrorTemplate bottomMarginInvalid = new ErrorTemplate(BOTTOM_MARGIN_INVALID);

    @Getter
    private static final ErrorTemplate noNewLineBeforePicture = new ErrorTemplate(NONL_BEFORE_PICTURE);

    @Getter
    private static final ErrorTemplate noNormalStyle = new ErrorTemplate(NO_NORMAL_STYLE);

    @Getter
    private static final ErrorTemplate wrongFont = new ErrorTemplate(WRONG_FONT);

    @Getter
    private static final ErrorTemplate wrongSpacing = new ErrorTemplate(WRONG_SPACING);

    @Getter
    private static final ErrorTemplate wrongFirstLineIndent = new ErrorTemplate(WRONG_FIRST_LINE_INDENT);

    @Getter
    private static final ErrorTemplate moreThanOneNumericStyleUsed = new ErrorTemplate(MORE_THAN_ONE_NUMERIC_STYLE_USED);

    @Getter
    private static final ErrorTemplate noPictureNumber = new ErrorTemplate(NO_PICTURE_NUMBER);

    public static ErrorTemplate addonTitleInvalid(String addonTitle) {
        return ErrorTemplate.createWithVararg(ADDON_TITLE_NOT_MATCHES, addonTitle);
    }

    public static ErrorTemplate tableNumberPatternError(String tableNumber) {
        return ErrorTemplate.createWithVararg(TABLE_NUM_PATTERN_ERR, tableNumber);
    }

    public static ErrorTemplate pictureNumberNoMatchesPattern(String pictureNumber) {
        return ErrorTemplate.createWithVararg(PICTURE_NUMBER_TEXT_ERR, pictureNumber);
    }

    public static ErrorTemplate noNewlineAfterPictureNumber(String pictureNumber) {
        return ErrorTemplate.createWithVararg(NONL_AFTER_PICTURE_NUMBER, pictureNumber);
    }

    public static ErrorTemplate requiredHeaderNotFound(String header) {
        return ErrorTemplate.createWithVararg(REQUIRED_HEADER_NOT_FOUND, header);
    }
}
