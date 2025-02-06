package com.toy.backend.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui/index.html";

        /* Swagger API TEST Link
            http://localhost:8080/swagger-ui/index.html
         */
    }
}
