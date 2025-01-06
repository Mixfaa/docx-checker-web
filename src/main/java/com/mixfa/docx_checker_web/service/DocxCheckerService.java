package com.mixfa.docx_checker_web.service;

import com.mixfa.docx_checker_web.docxchecker.model.ErrorTemplate;

import java.io.InputStream;
import java.util.Collection;

public interface DocxCheckerService {
    Collection<ErrorTemplate> checkDocxFile(InputStream docxFile);
}
