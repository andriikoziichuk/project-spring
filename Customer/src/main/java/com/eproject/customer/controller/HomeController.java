package com.eproject.customer.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class HomeController {

    @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
    public String home(Model model,
                       Principal principal,
                       HttpSession httpSession) {
        if (principal != null) {
            httpSession.setAttribute("username", principal.getName());
        }
        return "home";
    }

    @GetMapping("/home")
    public String index(Model model) {

        return "index";
    }
}
