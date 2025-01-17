package com.mixfa.docx_checker_web.controller;

import com.mixfa.docx_checker_web.docxchecker.DocxElementChecker;
import com.mixfa.docx_checker_web.misc.Utils;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import com.mixfa.docx_checker_web.service.impl.SyncDocxCheckerServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@Controller
public class MainController {
    private final DocxCheckerService docxCheckerService;
    private final List<String> documentCheckers;

    public MainController(SyncDocxCheckerServiceImpl docxCheckerService, List<DocxElementChecker<?>> checkers) {
        this.docxCheckerService = docxCheckerService;
        this.documentCheckers = checkers
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
    public String checkDocxReport(@RequestParam MultipartFile file, @RequestParam String locale, Model model) {
        try {
            var errors = docxCheckerService.checkDocxFile(file.getInputStream());
            var errorsEnhanced = Utils.makeUserFriendlyErrorMessages(errors, Locale.forLanguageTag(locale));

            model.addAttribute("errors", errorsEnhanced);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            model.addAttribute("errors", List.of("Internal server error"));
        }
        return "templates :: errors-list";
    }
}
