package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Controller
@RequestMapping("/countries")
public class CountryController {

    private final WorldService worldService;

    public CountryController(final WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping
    public String getAllCountries(Model model) {
        List<CountryEntity> countries = worldService.allCountries();
        model.addAttribute("countries", countries);
        return "countries/list";
    }

    @GetMapping("/{id}")
    public String getCountry(@PathVariable String id, Model model) {
        if (id.length() != 3) {
            return "redirect:/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/countries?error=not_found";
        }

        model.addAttribute("country", country);
        return "countries/details";
    }

    @GetMapping("/new")
    public String createCountryForm(Model model) {
        model.addAttribute("country", new CountryEntity());
        return "countries/new";
    }

    @PostMapping("/new")
    public String createCountry(@ModelAttribute @Valid CountryEntity country, RedirectAttributes redirectAttributes) {
        Optional<CountryEntity> existingCountry = worldService.allCountries()
                .stream()
                .filter(c -> c.getCode().equals(country.getCode()))
                .findFirst();

        if (existingCountry.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Country code already exists");
            return "redirect:/countries/new";
        }

        worldService.createNewCountry(country);
        redirectAttributes.addFlashAttribute("success", "Country created successfully");
        return "redirect:/countries";
    }

    @GetMapping("/edit/{id}")
    public String editCountryForm(@PathVariable String id, Model model) {
        if (id.length() != 3) {
            return "redirect:/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/countries?error=not_found";
        }

        model.addAttribute("country", country);
        return "countries/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCountry(@PathVariable String id, @ModelAttribute @Valid CountryEntity country, RedirectAttributes redirectAttributes) {
        if (id.length() != 3) {
            return "redirect:/countries?error=invalid_id";
        }

        CountryEntity existingCountry = worldService.getCountry(id);

        if (existingCountry == null) {
            return "redirect:/countries?error=not_found";
        }

        if (!Objects.equals(id, country.getCode())) {
            redirectAttributes.addFlashAttribute("error", "Country code mismatch");
            return "redirect:/countries/edit/" + id;
        }

        worldService.updateCountry(country);
        redirectAttributes.addFlashAttribute("success", "Country updated successfully");
        return "redirect:/countries";
    }

    @GetMapping("/delete/{id}")
    public String deleteCountryForm(@PathVariable String id, Model model) {
        if (id.length() != 3) {
            return "redirect:/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/countries?error=not_found";
        }

        model.addAttribute("country", country);
        return "countries/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCountryConfirmed(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (id.length() != 3) {
            return "redirect:/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/countries?error=not_found";
        }

        worldService.deleteCountry(country);
        redirectAttributes.addFlashAttribute("success", "Country deleted successfully");
        return "redirect:/countries";
    }
}



