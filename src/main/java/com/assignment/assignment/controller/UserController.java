package com.assignment.assignment.controller;


import com.assignment.assignment.model.User;
import com.assignment.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "admin_login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/employees/";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute(new User());
        return "admin_registration";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") User registrationDto) {
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
}
