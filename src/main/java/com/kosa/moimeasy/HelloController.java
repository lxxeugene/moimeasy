package com.kosa.moimeasy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        System.out.println("첫 번째 스프링 부트 테스트~~~~@@");
        model.addAttribute("data", "Spring Boot");
        return "helloView";
    }
}
