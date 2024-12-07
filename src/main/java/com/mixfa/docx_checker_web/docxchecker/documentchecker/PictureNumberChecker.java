package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class PictureNumberChecker implements DocumentChecker {
    public static final String PICTURE_NUMBER_STYLE = "Picturenumber";
    public static final Predicate<String> pictureNumberPattern =
            Pattern.compile("Рисунок \\d\\.\\d{1,3} – [A-ZА-ЯЄІЇ].+")
                    .asPredicate();

    private static final String NO_PICTURE_NUMBER = "nopicturenumber";
    private static final String PICTURE_NUMBER_TEXT_ERR = "picturenumbertext";
    private static final String NONL_AFTER_PICTURE_NUMBER = "nonewlineafterpicturenumber";

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            for (XWPFRun run : paragraph.getRuns()) {
                if (run.getEmbeddedPictures().isEmpty())
                    continue;

                if (paragraphs.size() <= i + 1)
                    continue;

                XWPFParagraph nextParagraph = paragraphs.get(i + 1);
                boolean styledAsPictureNumber = StringUtils.equals(nextParagraph.getStyle(), PICTURE_NUMBER_STYLE);

                if (!styledAsPictureNumber) {
                    errorsCollector.addError(NO_PICTURE_NUMBER);
                    continue;
                }

                if (!pictureNumberPattern.test(nextParagraph.getText()))
                    errorsCollector.addError(PICTURE_NUMBER_TEXT_ERR, nextParagraph.getText());

                if (paragraphs.size() >= i + 2)
                    continue;

                XWPFParagraph afterPictureNumberParagraph = paragraphs.get(i + 2);

                if (!StringUtils.isBlank(afterPictureNumberParagraph.getText()))
                    errorsCollector.addError(NONL_AFTER_PICTURE_NUMBER, nextParagraph.getText());
            }
        }
    }


}
