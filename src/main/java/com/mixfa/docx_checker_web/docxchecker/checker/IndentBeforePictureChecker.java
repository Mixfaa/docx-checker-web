package com.mixfa.docx_checker_web.docxchecker.checker;


import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndentBeforePictureChecker implements DocxElementChecker.ParagraphChecker {

    @Override
    public void checkElement(XWPFParagraph paragraph, DocxCheckingContext context) {
        var errorsCollector = context.errorsCollector();
        var currentIndex = context.elementIndex();
        if (currentIndex == 0) return;

        List<IBodyElement> elements = context.document().getBodyElements();

        for (XWPFRun run : paragraph.getRuns()) {
            if (run.getEmbeddedPictures().isEmpty())
                continue;

            var previousElement = elements.get(currentIndex - 1);
            if (previousElement instanceof XWPFParagraph previousParagraph) {
                boolean isNewLineBeforePicture = isNewLineBeforePicture(previousParagraph);
                if (!isNewLineBeforePicture) errorsCollector.addError(ErrorTemplates.noNewLineBeforePicture());
            }
        }
    }

    private static boolean isNewLineBeforePicture(XWPFParagraph previousParagraph) {
        String previousText = previousParagraph.getText().trim();
        return previousText.isEmpty() || previousParagraph.getRuns().stream().anyMatch(run -> !run.getEmbeddedPictures().isEmpty());
    }
}
