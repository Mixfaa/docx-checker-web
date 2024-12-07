package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class TableNumberChecker implements DocumentChecker {
    public static final String TABLE_NUMBER_STYLE = "Tablenumber";
    public static final Predicate<String> tableNumberPattern =
            Pattern.compile("Таблиця \\d\\.\\d{1,3} – [A-ZА-ЯЄІЇ].+")
                    .asPredicate();

    private static final String NO_TABLE_NUM_BEFORE_TABLE = "notablenum";
    private static final String TABLE_NUM_PATTERN_ERR = "tablenumpatternerr";
    private static final String NONL_BEFORE_TABLE_NUM = "nonewlinebeforetablenum";

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {

        List<IBodyElement> elements = document.getBodyElements();

        for (int i = 0; i < elements.size(); i++) {
            var element = elements.get(i);

            if (!(element instanceof XWPFTable))
                continue;
            if (i == 0)
                continue;

            var prevElement = elements.get(i - 1);
            if (prevElement instanceof XWPFParagraph prevParagraph) {
                if (!StringUtils.equals(prevParagraph.getStyle(), TABLE_NUMBER_STYLE))
                    errorsCollector.addError(NO_TABLE_NUM_BEFORE_TABLE);

                if (!tableNumberPattern.test(prevParagraph.getText()))
                    errorsCollector.addError(TABLE_NUM_PATTERN_ERR, prevParagraph.getText());

                if (i == 1)
                    continue;

                var prevprevElement = elements.get(i - 2);
                if (prevprevElement instanceof XWPFParagraph prevprevParagraph) {
                    if (!StringUtils.isBlank(prevprevParagraph.getText()))
                        errorsCollector.addError(NONL_BEFORE_TABLE_NUM);
                }
            }
        }
    }
}