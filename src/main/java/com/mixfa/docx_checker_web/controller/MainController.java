package com.mixfa.docx_checker_web.controller;

import com.mixfa.docx_checker_web.service.DocxCheckerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Controller
public class MainController {
    private final DocxCheckerService docxCheckerService;

    public MainController(DocxCheckerService docxCheckerService) {
        this.docxCheckerService = docxCheckerService;
    }

    @GetMapping("/")
    public String homePage() {
        return "index.html";
    }

    @PostMapping("/check-file")
    public String checkDocxReport(@RequestParam MultipartFile file, Model model) {
        var errors = docxCheckerService.checkDocxFile(file, Locale.ENGLISH);
        model.addAttribute("errors", errors);
        return "templates :: errors-list";
    }
}
