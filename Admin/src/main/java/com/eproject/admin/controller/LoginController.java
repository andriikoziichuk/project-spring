package com.eproject.admin.controller;

import com.eproject.library.dto.AdminDTO;
import com.eproject.library.model.Admin;
import com.eproject.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof  AnonymousAuthenticationToken)
            return "redirect:/login";

        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("adminDTO", new AdminDTO());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
                              BindingResult result,
                              Model model) {

        try {

            if(result.hasErrors()){
                model.addAttribute("adminDTO", adminDTO);
                return "register";
            }
            String username = adminDTO.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDTO", adminDTO);
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";
            }
            if(adminDTO.getPassword().equals(adminDTO.getRepeatPassword())){
                adminDTO.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
                adminService.save(adminDTO);
                model.addAttribute("success", "Register successfully!");
                model.addAttribute("adminDTO", adminDTO);
            }else{
                model.addAttribute("adminDTO", adminDTO);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");
                return "register";
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong!");
        }

        return "register";
    }
}
