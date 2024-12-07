package com.mixfa.docx_checker_web.docxchecker.bodyelementchecker;

import com.mixfa.docx_checker_web.docxchecker.ElementChecker;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public interface BodyElementChecker<T extends IBodyElement> extends ElementChecker<T> {
    abstract class XWPFParagraphChecker implements BodyElementChecker<XWPFParagraph> {
        @Override
        final public Class<XWPFParagraph> targetElementType() {
            return XWPFParagraph.class;
        }
    }
}
