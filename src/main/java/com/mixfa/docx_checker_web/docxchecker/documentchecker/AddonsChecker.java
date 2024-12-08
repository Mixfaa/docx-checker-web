package com.mixfa.docx_checker_web.docxchecker.documentchecker;

import com.mixfa.docx_checker_web.docxchecker.ElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorsCollector;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class AddonsChecker implements ElementChecker<XWPFParagraph> {
    private final static Predicate<String> ADDON_CHAR_PATTERN = Pattern.compile(
            "^(Додаток|ДОДАТОК) [АБВГДЕЖКЛМНПРСТУФХЦШЩЮ]\n"
    ).asPredicate();


    private static final String ADDON_TITLE_STYLE = "Heading1";
    private static final String ADDON_TITLE_NOT_MATCHES = "addontitleinvalid";
    private static final String ADDON_STYLE_NOT_MATCHES = "addonstyleinvalid";

    @Override
    public void checkElement(XWPFParagraph paragraph, ErrorsCollector errorsCollector) {
        var paragraphTextLC = paragraph.getText().toLowerCase();
        var paragraphText = paragraph.getText();
        if (!paragraphTextLC.startsWith("додаток"))
            return;


        if (!ADDON_CHAR_PATTERN.test(paragraphText))
            errorsCollector.addError(ADDON_TITLE_NOT_MATCHES, paragraphText);

        if (!StringUtils.equals(ADDON_TITLE_STYLE, paragraph.getStyle()))
            errorsCollector.addError(ADDON_STYLE_NOT_MATCHES);
    }

    @Override
    public Class<XWPFParagraph> targetElementType() {
        return XWPFParagraph.class;
    }
}
