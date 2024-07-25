package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import com.sparta.zmsb.weekfiveteamproject.service.MpoUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class LoggedInController {

    private final UserRepository userRepository;
    private final MpoUserDetailsService mpoUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoggedInController(final UserRepository userRepository, MpoUserDetailsService mpoUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mpoUserDetailsService = mpoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/auth/my-account")
    public String myAccount(final Model model) {
        return "auth/my-account";
    }

}