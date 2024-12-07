package com.mixfa.docx_checker_web.docxchecker.documentchecker;


import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class IndentBeforePictureChecker implements DocumentChecker {
    private static final String NONL_BEFORE_PICTURE = "nonewlinebeforepicture";

    @Override
    public void checkElement(XWPFDocument document, ErrorsCollector errorsCollector) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (int i = 1; i < paragraphs.size(); i++) {  // Start from index 1 to check previous paragraph
            XWPFParagraph paragraph = paragraphs.get(i);
            for (XWPFRun run : paragraph.getRuns()) {
                // Check if the run contains pictures
                if (!run.getEmbeddedPictures().isEmpty()) {
                    XWPFParagraph previousParagraph = paragraphs.get(i - 1);
                    boolean isNewLineBeforePicture = isNewLineBeforePicture(previousParagraph);

                    if (!isNewLineBeforePicture) errorsCollector.addError(NONL_BEFORE_PICTURE);
                }
            }
        }
    }

    private static boolean isNewLineBeforePicture(XWPFParagraph previousParagraph) {
        String previousText = previousParagraph.getText().trim();
        return previousText.isEmpty() || previousParagraph.getRuns().stream().anyMatch(run -> !run.getEmbeddedPictures().isEmpty());
    }
}
