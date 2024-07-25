package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.UserEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.UserRepository;
import com.sparta.zmsb.weekfiveteamproject.service.MpoUserDetailsService;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoggedInController {

    private final UserRepository userRepository;
    private final MpoUserDetailsService mpoUserDetailsService;
    private final WorldService worldService;
    private final PasswordEncoder passwordEncoder;

    public LoggedInController(final UserRepository userRepository, MpoUserDetailsService mpoUserDetailsService, PasswordEncoder passwordEncoder, WorldService worldService) {
        this.userRepository = userRepository;
        this.mpoUserDetailsService = mpoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.worldService = worldService;
    }

    @GetMapping("/auth/my-account")
    public String myAccount(final Model model) {
        return "auth/my-account";
    }

    @GetMapping("/auth/secure/manage-users")
    public String secureManageAccounts(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "auth/secure/manage-users";
    }

    @PostMapping("auth/secure/manage-users")
    public String grantAdmin(Model model, @RequestParam Long userId, @RequestParam String role) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        mpoUserDetailsService.changeRole(user, role);
        return "redirect:/auth/secure/manage-users";
    }
    @PostMapping("auth/secure/delete-user")
    public String deleteUser(Model model, @RequestParam Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        mpoUserDetailsService.deleteUser(user);
        return "redirect:/auth/secure/manage-users";
    }
    @GetMapping("auth/countries/")
    public String countries(Model model) {
        List<CountryEntity> countries = worldService.allCountries();
        model.addAttribute("countries", countries);
        return "/auth/countries/list";
    }
    //todo cities and country languages
}