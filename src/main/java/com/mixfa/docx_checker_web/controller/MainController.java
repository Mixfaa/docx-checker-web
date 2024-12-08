package com.mixfa.docx_checker_web.controller;

import com.mixfa.docx_checker_web.docxchecker.documentchecker.DocumentChecker;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final DocxCheckerService docxCheckerService;
    private final List<String> documentCheckers;

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
    public String checkDocxReport(@RequestParam MultipartFile file, Model model) {
        try {
            var errors = docxCheckerService.checkDocxFile(file.getInputStream(), Locale.ENGLISH);
            var errorsEnhanced = errors.stream()
                    .collect(
                            Collectors.toMap(
                                    (k) -> k,
                                    (v) -> errors.stream().filter(err -> err.equals(v)).count(),
                                    (k1, k2) -> k1
                            )
                    )
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + " x" + entry.getValue())
                    .toList();

            model.addAttribute("errors", errorsEnhanced);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            model.addAttribute("errors", List.of("Internal server error"));
        }
        return "templates :: errors-list";
    }
}
