package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("auth/countries")
public class CountryController {

    private final WorldService worldService;


    public CountryController(final WorldService worldService, CityRepository cityRepository) {
        this.worldService = worldService;
    }

    @GetMapping
    public String getAllCountries(Model model) {
        List<CountryEntity> allCountries = worldService.allCountries();
        model.addAttribute("countries", allCountries);
        return "auth/countries/list";
    }

    @GetMapping("/most-cities")
    public String getCountriesWithMostCities(Model model) {
        List<CountryEntity> countriesWithMostCities = worldService.countriesWithMostCities();
        model.addAttribute("countries", countriesWithMostCities);
        return "auth/countries/list";
    }

    @GetMapping("/details/{id}")
    public String viewCountryDetails(@PathVariable String id, Model model) {
        if (id.length() != 3) {
            return "redirect:/auth/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/auth/countries?error=not_found";
        }

        int speakingPopulation = worldService.amountOfPeopleSpeakingOfficialLanguage(id);
        int totalPopulation = country.getPopulation();
        double percentageSpeaking = totalPopulation > 0 ? (speakingPopulation * 100.0) / totalPopulation : 0;

        String percentageInLargestCity = worldService.percentageOfGivenCountriesPopulationThatLivesInTheLargestCity(country.getName());

        System.out.println("Percentage in Largest City: " + percentageInLargestCity);

        model.addAttribute("country", country);
        model.addAttribute("speakingPopulation", speakingPopulation);
        model.addAttribute("percentageSpeaking", percentageSpeaking);
        model.addAttribute("percentageInLargestCity", percentageInLargestCity);

        return "auth/countries/detail";
    }


    @GetMapping("/no-head-of-state")
    public String getCountriesWithNoHeadOfState(Model model) {
        List<CountryEntity> countriesWithoutHeadOfState = worldService.countriesWithNoHeadOfState();
        model.addAttribute("countries", countriesWithoutHeadOfState);
        return "auth/countries/list";
    }

    @GetMapping("/search")
    public String searchCountryById(@RequestParam("id") String id, Model model) {
        if (id.length() != 3) {
            model.addAttribute("error", "Invalid country ID. It should be 3 characters long.");
            return "auth/countries/list";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            model.addAttribute("error", "Country not found.");
            return "auth/countries/list";
        }

        model.addAttribute("countries", List.of(country));
        return "auth/countries/list";
    }

    @GetMapping("/new")
    public String createCountryForm(Model model) {
        model.addAttribute("country", new CountryEntity());
        return "auth/countries/new";
    }

    @PostMapping("/new")
    public String createCountry(@Valid @ModelAttribute("country") CountryEntity country, Errors errors) {
        if (errors.hasErrors()) {
            return "auth/countries/new";
        }

        try {
            worldService.createNewCountry(country);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/auth/countries/new?error=true";
        }

        return "redirect:/auth/countries";
    }

    @GetMapping("/edit/{code}")
    public String editCountryForm(@PathVariable("code") String code, Model model) {
        CountryEntity country = worldService.getCountry(code);
        model.addAttribute("country", country);
        return "auth/countries/edit";
    }

    @PostMapping("/edit/{code}")
    public String editCountry(@PathVariable("code") String code, @ModelAttribute CountryEntity country, BindingResult result) {
        if (result.hasErrors()) {
            return "auth/countries/edit";
        }
        worldService.updateCountry(country);
        return "redirect:/auth/countries";
    }


    @GetMapping("/delete/{id}")
    public String deleteCountryForm(@PathVariable String id, Model model) {
        if (id.length() != 3) {
            return "redirect:/auth/countries?error=invalid_id";
        }

        CountryEntity country = worldService.getCountry(id);

        if (country == null) {
            return "redirect:/auth/countries?error=not_found";
        }

        model.addAttribute("country", country);
        return "redirect:/auth/countries";
    }

    @PostMapping("/delete/{id}")
    public String deleteCountry(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            CountryEntity country = worldService.getCountry(id);
            if (country != null) {
                worldService.deleteCountry(country);
                redirectAttributes.addFlashAttribute("success", "Country deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Country not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error occurred while deleting country: " + e.getMessage());
        }

        return "redirect:/auth/countries";
    }


}





