package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class PictureNumberChecker implements DocxElementChecker.ParagraphChecker {
    public static final String PICTURE_NUMBER_STYLE = "Picturenumber";
    public static final Predicate<String> pictureNumberPattern =
            Pattern.compile("^Рисунок [1-9]\\d*(\\.[1-9]\\d*)?\\s{1,2}([–\\-])\\s{1,2}[А-ЯЇЄІҐ][^.]*\\.?").asPredicate();

    private static final String NO_PICTURE_NUMBER = "nopicturenumber";
    private static final String PICTURE_NUMBER_TEXT_ERR = "picturenumbertext";
    private static final String NONL_AFTER_PICTURE_NUMBER = "nonewlineafterpicturenumber";

    @Override
    public void checkElement(XWPFParagraph paragraph, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        var currentIndex = context.currentElementIndex();
        var bodyElements = context.document().getBodyElements();
        if (bodyElements.size() <= currentIndex + 1)
            return;

        for (XWPFRun run : paragraph.getRuns()) {
            if (run.getEmbeddedPictures().isEmpty())
                continue;

            IBodyElement nextElement = bodyElements.get(currentIndex + 1);
            if (nextElement instanceof XWPFParagraph nextParagraph) {
                boolean styledAsPictureNumber = StringUtils.equals(nextParagraph.getStyle(), PICTURE_NUMBER_STYLE);

                if (!styledAsPictureNumber) {
                    errorsCollector.addError(NO_PICTURE_NUMBER);
                    continue;
                }

                if (!pictureNumberPattern.test(nextParagraph.getText()))
                    errorsCollector.addError(PICTURE_NUMBER_TEXT_ERR, nextParagraph.getText());

                if (bodyElements.size() <= currentIndex + 2)
                    continue;

                IBodyElement afterPictureNumberElement = bodyElements.get(currentIndex + 2);
                if (afterPictureNumberElement instanceof XWPFParagraph afterPictureNumberParagraph) {
                    if (!StringUtils.isBlank(afterPictureNumberParagraph.getText()))
                        errorsCollector.addError(NONL_AFTER_PICTURE_NUMBER, nextParagraph.getText());
                } else
                    errorsCollector.addError(NONL_AFTER_PICTURE_NUMBER, nextParagraph.getText());
            } else
                errorsCollector.addError(NO_PICTURE_NUMBER);
        }

    }
}
