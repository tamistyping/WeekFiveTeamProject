package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.exceptions.InvalidEndpointException;
import com.sparta.zmsb.weekfiveteamproject.exceptions.ResourceNotFoundException;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import io.swagger.v3.oas.annotations.Parameter;
import com.sparta.zmsb.weekfiveteamproject.exceptions.InvalidInputException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/cities")
public class CityController {
  
    private final WorldService worldService;

    @Autowired
    public CityController(WorldService worldService) {
        this.worldService = worldService;
    }

// @PostMapping("/secure/new")
//    public ResponseEntity<EntityModel<CityEntity>> createCity(@Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey, @RequestBody @Valid CityEntity cityEntity, HttpServletRequest request) {
//        List<CountryEntity> countries = worldService.allCountries();
//
//        countries = countries.stream().filter(c -> c.getCode().equals(cityEntity.getCountryCode().getCode())).toList();
//
//        if (countries.isEmpty()) {
//            throw new ResourceNotFoundException("Country with code: " + cityEntity.getCountryCode().getCode() + " does not exist");
//        }
//
//        List<EntityModel<CityEntity>> cityEntityModel = Stream.of(worldService.createCity(cityEntity)).map(city -> {
//            List<Link> countryLinks = Stream.of(city.getCountryCode().getCode()).map(code -> WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName())).toList();
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getCity(city.getId())).withSelfRel();
//            Link relLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withRel("city");
//            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
//        }).toList();
//
//        URI location = URI.create(request.getRequestURL().toString() + "/" + cityEntity.getId());
//
//        return ResponseEntity.created(location).body(cityEntityModel.getFirst());
//
//        // Change this to point to a particular template
//        // return "city_templates/create_city";
//    }
//
    @GetMapping("/secure/new")
    public String createCity(Model model) {
        model.addAttribute("city", new CityEntity());
        return "city_templates/create_city";
    }
    @PostMapping("/secure/new")
    public String createCityPost(@Valid @ModelAttribute("city") CityEntity cityEntity, Model model, Errors errors) {
        if (errors.hasErrors()) {
            return "city_templates/create_city";
        }

        List<CountryEntity> countries = worldService.allCountries();

        countries = countries.stream().filter(c -> c.getCode().equals(cityEntity.getCountryCode().getCode())).toList();

        if (countries.isEmpty()) {
            throw new ResourceNotFoundException("Country with code: " + cityEntity.getCountryCode().getCode() + " does not exist");
        }

        worldService.createCity(cityEntity);

        return "city_templates/create_city";
    }

//    @GetMapping("/search")
//    public CollectionModel<EntityModel<CityEntity>> getAllCities() {
//        List<EntityModel<CityEntity>> cities = worldService.allCities().stream().map(city -> {
//            List<Link> countryLinks = Stream.of(city.getCountryCode().getCode()).map(code -> WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName())).toList();
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getCity(city.getId())).withSelfRel();
//            Link relLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withRel("city");
//            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
//        }).toList();
//
//        return CollectionModel.of(cities, WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withSelfRel());
//
//    }

//    @GetMapping("/search")
//    public String getAllCities(Model model) {
//        List<EntityModel<CityEntity>> cities = worldService.allCities().stream().map(city -> {
//            List<Link> countryLinks = Stream.of(city.getCountryCode().getCode()).map(code -> WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName())).toList();
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getCity(city.getId(), model)).withSelfRel();
//            Link relLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities(model)).withRel("city");
//            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
//        }).toList();
//
//        model.addAttribute("cities", cities);
//
//        return "city_templates/cities";
//    }

    @GetMapping("/search")
    public String getAllCities(Model model) {
        List<CityEntity> cities = worldService.allCities();
        model.addAttribute("cities", cities);
        return "city_templates/cities";
    }

//    @GetMapping("/search/{id}")
//    public CollectionModel<EntityModel<CityEntity>> getCity(@PathVariable @Valid Integer id) {
//
//        CityEntity cityEntity = worldService.getCityById(id);
//        if (cityEntity == null) {
//            throw new ResourceNotFoundException("City with ID: " + id + " not found");
//        }
//
//        List<EntityModel<CityEntity>> cityEntityModel = Stream.of(worldService.getCityById(id)).map(city -> {
//            List<Link> countryLinks = Stream.of(city.getCountryCode().getCode()).map(code -> WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName())).toList();
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getCity(city.getId())).withSelfRel();
//            Link relLink = WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withRel("city");
//            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
//        }).toList();
//        return CollectionModel.of(cityEntityModel, WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withSelfRel());
//    }
//
    @GetMapping("/search/{id}")
    public String getCity(@PathVariable @Valid Integer id, Model model) {

        CityEntity cityEntity = worldService.getCityById(id);
        if (cityEntity == null) {
            throw new ResourceNotFoundException("City with ID: " + id + " not found");
        }

        CityEntity cityEntityModel = worldService.getCityById(id);

        model.addAttribute("city", cityEntityModel);

        return "city_templates/city";
    }



//    @GetMapping("/districts-with-lowest-population")
//    public String getDistrictsWithLowestPopulation() {
//        return worldService.getSmallestDistrictsByPopulation();
//    }
//
    @GetMapping("/districts-with-lowest-population")
    public String getDistrictsWithLowestPopulation(Model model) {
        model.addAttribute("smallestDistricts", worldService.getSmallestDistrictsByPopulation());
        return "city_templates/districts_with_lowest_population";
    }
//
//    @PutMapping("/secure/update/{id}")
//    public ResponseEntity<EntityModel<CityEntity>> updateCity(@Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey, @PathVariable @Valid Integer id, @RequestBody @Valid CityEntity cityEntity) {
//
//        if (!id.equals(cityEntity.getId())) {
//            throw new InvalidInputException("City ID in path: " + id + " does not match ID in payload: " + cityEntity.getId());
//        }
//
//        List<CityEntity> cities = worldService.allCities();
//        List<CountryEntity> countries = worldService.allCountries();
//
//        countries = countries.stream().filter(c -> c.getCode().equals(cityEntity.getCountryCode().getCode())).toList();
//        cities = cities.stream().filter(c -> c.getId().equals(cityEntity.getId())).toList();
//
//        if (!id.equals(cityEntity.getId())) {
//            throw new InvalidEndpointException("City ID: " + cityEntity.getId() + " does not match the endpoint ID: " + id);
//        } else if (cities.isEmpty()) {
//            throw new ResourceNotFoundException("City ID: " + cityEntity.getId() + " does not exist");
//        } else if (countries.isEmpty()) {
//            throw new ResourceNotFoundException("Country with code: " + cityEntity.getCountryCode().getCode() + " does not exist");
//        } else {
//            worldService.updateCity(cityEntity);
//            return ResponseEntity.noContent().build();
//        }
//    }

//    @DeleteMapping("/secure/delete/{id}")
//    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> deleteCity(@Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey, @PathVariable @Valid Integer id) {
//        CityEntity city = worldService.getCityById(id);
//        if (city == null) {
//            throw new ResourceNotFoundException("City with ID: " + id + " not found");
//        }
//
//        worldService.deleteCity(id);
//
//        return ResponseEntity.noContent().build();
//    }

    @GetMapping("/secure/delete")
    public String deleteCity() {;
        return "city_templates/delete_city";
    }

    @DeleteMapping("/secure/delete")
    public String deleteCityDelete(@Valid @ModelAttribute("cityID") Integer id, Errors errors) {
        if (errors.hasErrors()) {
            return "city_templates/delete_city";
        }
        CityEntity city = worldService.getCityById(id);
        if (city == null) {
            throw new ResourceNotFoundException("City with ID: " + id + " not found");
        }

        worldService.deleteCity(id);

        return "city_templates/delete_city";
    }
}