package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.exceptions.ResourceNotFoundException;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/api/cities/auth")
public class CityController {
  
    private final WorldService worldService;

    @Autowired
    public CityController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/create")
    public String createCity(Model model) {
        model.addAttribute("city", new CityEntity());
        model.addAttribute("create", true);
        model.addAttribute("update", false);
        return "auth/city_templates/create_city";
    }
    @PostMapping("/create")
    public String createCityPost(@RequestParam String name, @RequestParam String countryCode,
                                 @RequestParam String district, @RequestParam Integer population) {
        CountryEntity country = worldService.getCountry(countryCode);
        if (country == null) {
            throw new ResourceNotFoundException("Country with code: " + countryCode + " does not exist");
        }

        CityEntity cityEntity = new CityEntity();
        cityEntity.setName(name);
        cityEntity.setCountryCode(country);
        cityEntity.setDistrict(district);
        cityEntity.setPopulation(population);

        worldService.createCity(cityEntity);

        return "redirect:/api/cities/auth/create?";
    }

    @GetMapping
    public String getAllCities(Model model) {
        List<CityEntity> cities = worldService.allCities();
        model.addAttribute("cities", cities);
        model.addAttribute("searchPage", false);
        model.addAttribute("smallestDistricts", null);
        return "auth/city_templates/cities";
    }

    @GetMapping("/search")
    public String searchCity(@RequestParam("cityName") String cityName, RedirectAttributes redirectAttributes) {
        List<CityEntity> cities = worldService.allCities();
        for (CityEntity city : cities) {
            if (city.getName().equalsIgnoreCase(cityName)) {
                redirectAttributes.addAttribute("id", city.getId());
                return "redirect:/api/cities/auth/search/{id}";
            }
        }
        return "redirect:/api/cities/auth";
    }

    @GetMapping("/search/{id}")
    public String getCity(@PathVariable Integer id, Model model) {
        CityEntity cityEntity = worldService.getCityById(id);
        if (cityEntity == null) {
            throw new ResourceNotFoundException("City with ID: " + id + " not found");
        }
        model.addAttribute("cities", List.of(cityEntity));
        model.addAttribute("searchPage", true);
        model.addAttribute("smallestDistricts", null);
        return "auth/city_templates/cities";
    }

    @GetMapping("/districts-with-lowest-population")
    public String getDistrictsWithLowestPopulation(Model model) {
        List<CityEntity> cities = worldService.allCities();
        model.addAttribute("cities", cities);
        model.addAttribute("smallestDistricts", worldService.getSmallestDistrictsByPopulation());
        model.addAttribute("searchPage", false);
        model.addAttribute("update", false);
        model.addAttribute("create", false);
        return "auth/city_templates/cities";
    }

    @GetMapping("/update/{id}")
    public String updateCity(@PathVariable Integer id, Model model) {
        CityEntity city = worldService.getCityById(id);
        model.addAttribute("city", city);
        model.addAttribute("update", true);
        model.addAttribute("create", false);
        model.addAttribute("smallestDistricts", null);
        return "auth/city_templates/create_city";
    }

    @PostMapping("/update/{id}")
    public String updateCityPost(@PathVariable Integer id, @RequestParam String name, @RequestParam String countryCode,
                                 @RequestParam String district, @RequestParam Integer population) {

        CountryEntity country = worldService.getCountry(countryCode);
        if (country == null) {
            throw new ResourceNotFoundException("Country with code: " + countryCode + " does not exist");
        }

        CityEntity city = new CityEntity();
        city.setId(id);
        city.setName(name);
        city.setCountryCode(worldService.getCountry(countryCode));
        city.setDistrict(district);
        city.setPopulation(population);

        worldService.updateCity(city);
        return "redirect:/api/cities/auth/update/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteCityDelete(@PathVariable Integer id) {
        CityEntity city = worldService.getCityById(id);
        if (city == null) {
            throw new ResourceNotFoundException("City with ID: " + id + " not found");
        }

        worldService.deleteCity(id);

        return "redirect:/api/cities/auth";
    }
}