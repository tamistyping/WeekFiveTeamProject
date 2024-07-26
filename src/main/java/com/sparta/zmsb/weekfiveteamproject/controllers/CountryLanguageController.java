package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.exceptions.ResourceNotFoundException;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/auth/languages")
public class CountryLanguageController {


    private final WorldService worldService;

    @Autowired
    public CountryLanguageController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping
    public String getLanguages(Model model) {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        model.addAttribute("languages", languages);
        model.addAttribute("searchPage", false);
        return "auth/country_language_templates/languages";
    }

    @GetMapping("/search")
    public String searchLanguages(@RequestParam("countryCode") String countryCode, @RequestParam("languageName") String languageString, RedirectAttributes redirectAttributes) {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        for (CountrylanguageEntity language : languages) {
            if (language.getId().getCountryCode().equalsIgnoreCase(countryCode) && language.getId().getLanguage().equalsIgnoreCase(languageString)) {
                redirectAttributes.addAttribute("countryCode", language.getId().getCountryCode());
                redirectAttributes.addAttribute("language", language.getId().getLanguage());
                return "redirect:/auth/languages/search/{countryCode}/{language}";
            }
        }
        return "redirect:/auth/languages";
    }

    @GetMapping("/search/{countryCode}/{language}")
    public String searchLanguage(@PathVariable String countryCode, @PathVariable String language, Model model) {
        CountrylanguageEntityId countrylanguageEntityId = new CountrylanguageEntityId();
        countrylanguageEntityId.setCountryCode(countryCode);
        countrylanguageEntityId.setLanguage(language);

        List<CountrylanguageEntity> languages = worldService.allLanguages().stream().filter(l -> l.getId().getCountryCode().equals(countryCode) && l.getId().getLanguage().equals(language)).toList();

        if (languages.isEmpty()) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }

        model.addAttribute("languages", languages);
        model.addAttribute("searchPage", true);

        return "auth/country_language_templates/languages";
    }

    @GetMapping("/create")
    public String createLanguage(Model model) {
        model.addAttribute("language", new CountrylanguageEntity());
        model.addAttribute("create", true);
        model.addAttribute("update", false);
        return "auth/country_language_templates/create_language";
    }

    @PostMapping("/create")
    public String createLanguagePost(@RequestParam String countryCode, @RequestParam String language,
                                     @RequestParam String official, @RequestParam BigDecimal percentage) {
        CountryEntity country = worldService.getCountry(countryCode);
        if (country == null) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }

        CountrylanguageEntity countrylanguageEntity = new CountrylanguageEntity();

        CountrylanguageEntityId countrylanguageEntityId = new CountrylanguageEntityId();
        countrylanguageEntityId.setLanguage(language);
        countrylanguageEntityId.setCountryCode(countryCode);

        countrylanguageEntity.setId(countrylanguageEntityId);
        countrylanguageEntity.setCountryCode(country);
        countrylanguageEntity.setIsOfficial(official);
        countrylanguageEntity.setPercentage(percentage);

        worldService.createCountryLanguageEntityEntry(countrylanguageEntity);

        return "redirect:/auth/languages/create?";
    }

    @GetMapping("/update/{countryCode}/{language}")
    public String updateLanguage(@PathVariable String countryCode, @PathVariable String language, Model model) {
        List<CountrylanguageEntity> languages = worldService.allLanguages().stream().filter(l -> l.getId().getCountryCode().equalsIgnoreCase(countryCode) && l.getId().getLanguage().equalsIgnoreCase(language)).toList();
        if (languages.isEmpty()) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }
        model.addAttribute("language", languages.getFirst());
        model.addAttribute("update", true);
        model.addAttribute("create", false);
        return "auth/country_language_templates/create_language";
    }

    @PostMapping("/update/{countryCode}/{language}")
    public String updateLanguagePost(@PathVariable String countryCode, @PathVariable String language, @RequestParam String official, @RequestParam BigDecimal percentage) {
        CountrylanguageEntity countrylanguageEntity = new CountrylanguageEntity();

        CountrylanguageEntityId cleId = new CountrylanguageEntityId();
        cleId.setCountryCode(countryCode);
        cleId.setLanguage(language);

        countrylanguageEntity.setId(cleId);
        countrylanguageEntity.setIsOfficial(official);
        countrylanguageEntity.setPercentage(percentage);

        worldService.updateCountryLanguageEntity(countrylanguageEntity);

        return "redirect:/auth/languages/update/" + countryCode + "/" + language;
    }

    @PostMapping("/delete/{countryCode}/{language}")
    public String deleteLanguage(@PathVariable String countryCode, @PathVariable String language) {
        List<CountrylanguageEntity> languages = worldService.allLanguages().stream().filter(l -> l.getId().getCountryCode().equals(countryCode) && l.getId().getLanguage().equals(language)).toList();
        if (languages.isEmpty()) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }

        worldService.deleteCountryLanguageEntity(languages.getFirst());
        return "redirect:/auth/languages";
    }

}
