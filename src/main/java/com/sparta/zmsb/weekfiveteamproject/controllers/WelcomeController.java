package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.UserEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import com.sparta.zmsb.weekfiveteamproject.service.MpoUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WelcomeController {

    private final UserRepository userRepository;
    private final MpoUserDetailsService mpoUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public WelcomeController(final UserRepository userRepository, MpoUserDetailsService mpoUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mpoUserDetailsService = mpoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserEntity());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserEntity user, Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "login";
        }

        if(mpoUserDetailsService.validateUser(user.getUsername())){
            model.addAttribute("userError", "Username does not exist");
            model.addAttribute("registerRedirect");
            return "login";
        }

        if(mpoUserDetailsService.validateUserPassword(user.getUsername(), user.getPassword())){
            return "auth/home"; //change to logged in landing page
        }

        model.addAttribute("passwordError", "Password does not match.");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserEntity userEntity, Errors errors, Model model) {

        if(mpoUserDetailsService.validateNewUsername(userEntity)) {
            model.addAttribute("nameerror", "Username is already in use.");
            if(mpoUserDetailsService.validateNewEmail(userEntity)){
                model.addAttribute("emailerror", "Email is already in use.");
            }
            if(mpoUserDetailsService.validateNewPassword(userEntity.getPassword())){
                model.addAttribute("passworderror", "Password must be 8 characters or longer.");
            }
            return "register";
        }

        if(mpoUserDetailsService.validateNewEmail(userEntity)){
            model.addAttribute("emailerror", "Email is already in use.");
            if(mpoUserDetailsService.validateNewPassword(userEntity.getPassword())) {
                model.addAttribute("passworderror", "Password must be 8 characters or longer.");
                return "register";
            }
        }

        if(mpoUserDetailsService.validateNewPassword(userEntity.getPassword())){
            model.addAttribute("passworderror", "Password must be 8 characters or longer.");
            return "register";
        }

        if(errors.hasErrors()) {
            return "register";
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles("ROLE_USER");
        userRepository.save(userEntity);
        return "redirect:/index"; //change to logged in landing page

    }

    @GetMapping("/auth/home")
    public String homePage(Model model) {
        return "auth/home";
    }
}