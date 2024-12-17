package com.mixfa.docx_checker_web;

import com.mixfa.docx_checker_web.docxchecker.error.ErrorTemplate;
import com.mixfa.docx_checker_web.service.DocxCheckerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@SpringBootApplication
public class DocxCheckerWebApplication {

    @Bean
    public CommandLineRunner runner(DocxCheckerService docxCheckerService) {
        return _ -> {
            var file = Path.of("C:\\Users\\mishu\\Desktop\\test_file.docx").toFile();
            if (!file.exists()) return;
            try (var fis = new FileInputStream(file)) {
                for (ErrorTemplate t : docxCheckerService.checkDocxFile(fis))
                    System.out.println(t.formatError(Locale.ENGLISH));
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(DocxCheckerWebApplication.class, args);
    }
}
