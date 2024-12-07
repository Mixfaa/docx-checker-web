package com.mixfa.docx_checker_web;

import com.mixfa.docx_checker_web.service.DocxCheckerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;
import java.util.Locale;

@SpringBootApplication
public class DocxCheckerWebApplication {

//    @Bean
//    public CommandLineRunner runner(DocxCheckerService docxCheckerService) {
//        return _ -> {
//            try (var fis = new FileInputStream(
//                    "C:\\Users\\mishu\\Desktop\\test_file.docx"
//            )) {
//                docxCheckerService.checkDocxFile(fis, Locale.ENGLISH);
//            }
//        };
//    }

    public static void main(String[] args) {
        SpringApplication.run(DocxCheckerWebApplication.class, args);
    }
}
