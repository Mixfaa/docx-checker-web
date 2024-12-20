package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class TableNumberChecker implements DocxElementChecker.BodyElementChecker {
    public static final String TABLE_NUMBER_STYLE = "Tablenumber";
    public static final Predicate<String> TABLE_NUMBER_PATTERN =
            Pattern.compile("Таблиця \\d\\.\\d{1,3} – [A-ZА-ЯЄІЇ].+")
                    .asPredicate();

    private static final String NO_TABLE_NUM_BEFORE_TABLE = "notablenum";
    private static final String TABLE_NUM_PATTERN_ERR = "tablenumpatternerr";
    private static final String NONL_BEFORE_TABLE_NUM = "nonewlinebeforetablenum";

    @Override
    public void checkElement(IBodyElement element, DocxCheckingContext context) {
        var currentIndex = context.elementIndex();
        if (currentIndex < 2) return;

        var errorsCollector = context.errorsCollector();
        List<IBodyElement> elements = context.document().getBodyElements();

        if (!(element instanceof XWPFTable))
            return;
        var prevElement = elements.get(currentIndex - 1);
        if (prevElement instanceof XWPFParagraph prevParagraph) {
            if (!StringUtils.equals(prevParagraph.getStyle(), TABLE_NUMBER_STYLE))
                errorsCollector.addError(NO_TABLE_NUM_BEFORE_TABLE);

            if (!TABLE_NUMBER_PATTERN.test(prevParagraph.getText()))
                errorsCollector.addError(TABLE_NUM_PATTERN_ERR, prevParagraph.getText());

            var prevprevElement = elements.get(currentIndex - 2);
            if (prevprevElement instanceof XWPFParagraph prevprevParagraph) {
                if (!StringUtils.isBlank(prevprevParagraph.getText()))
                    errorsCollector.addError(NONL_BEFORE_TABLE_NUM);
            }
        }
    }
}