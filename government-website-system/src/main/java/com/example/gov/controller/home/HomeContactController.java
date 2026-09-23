package com.example.gov.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 联系我们控制器。
 */
@Controller
public class HomeContactController {

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "联系我们");
        return "home/contact";
    }
}
