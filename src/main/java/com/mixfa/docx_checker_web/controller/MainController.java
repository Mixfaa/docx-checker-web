package com.mixfa.docx_checker_web.controller;

import com.mixfa.docx_checker_web.docxchecker.documentchecker.DocumentChecker;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
public class MainController {
    private final DocxCheckerService docxCheckerService;
    private final List<String > documentCheckers;

    public MainController(DocxCheckerService docxCheckerService, List<DocumentChecker> documentCheckers) {
        this.docxCheckerService = docxCheckerService;
        this.documentCheckers = documentCheckers
                .stream()
                .map(checker -> checker.getClass().getSimpleName())
                .toList();
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("checkers", documentCheckers);
        return "index.html";
    }

    @PostMapping("/check-file")
    public String checkDocxReport(@RequestParam MultipartFile file, Model model) throws IOException {
        var errors = docxCheckerService.checkDocxFile(file.getInputStream(), Locale.ENGLISH);
        model.addAttribute("errors", errors);
        return "templates :: errors-list";
    }
}
