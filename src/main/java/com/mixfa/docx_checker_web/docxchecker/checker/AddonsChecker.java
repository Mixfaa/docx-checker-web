package com.mixfa.docx_checker_web.docxchecker.checker;

import com.mixfa.docx_checker_web.docxchecker.DocxCheckingContext;
import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.docxchecker.ErrorTemplates;
import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class AddonsChecker implements DocxElementChecker<XWPFParagraph> {
    private final static Predicate<String> ADDON_CHAR_PATTERN = Pattern.compile("^(Додаток|ДОДАТОК) [АБВГДЕЖКЛМНПРСТУФХЦШЩЮ]").asPredicate();

    private static final List<String> ADDON_STYLES = List.of("Heading1", "1", "Heading1,1_Topic");



    @Override
    public void checkElement(XWPFParagraph paragraph, DocxCheckingContext context) {
        var paragraphText = paragraph.getText();
        var paragraphTextLC = paragraphText.toLowerCase();
        if (!paragraphTextLC.startsWith("додаток")) return;

        var errorsCollector = context.errorsCollector();
        if (!ADDON_CHAR_PATTERN.test(paragraphText)) errorsCollector.addError(ErrorTemplates.addonTitleInvalid(paragraphText));

        var paragraphStyle = paragraph.getStyle();
        var styleMatches = ADDON_STYLES.stream().anyMatch((style) -> StringUtils.equals(style, paragraphStyle));
        if (!styleMatches) errorsCollector.addError(ErrorTemplates.addonStyleNotMatches());
    }

    @Override
    public Class<XWPFParagraph> targetElementType() {
        return XWPFParagraph.class;
    }
}

