package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final WorldService worldService;

    public CountryController(final WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CountryEntity>>> getAllCountries() {
        List<EntityModel<CountryEntity>> countries = worldService.allCountries()
                .stream().map(
                country -> {
                    List<Link> citiesLinks = worldService.allCities().stream().filter(cities -> cities.getCountryCode().getCode().equals(country.getCode())).toList()
                            .stream().map(
                                    city -> WebMvcLinkBuilder.linkTo(
                                            methodOn(CityController.class).getCityById(city.getCountryCode())).withRel(city.getName())).toList();
                    List<Link> languagesLinks = worldService.allLanguages().stream().filter(lang -> lang.getCountryCode().getCode().equals(country.getCode())).toList()
                            .stream().map(
                                    lang -> WebMvcLinkBuilder.linkTo(
                                            methodOn(CountryLanguageController.class).getCountryLanguageById(lang.getCountryCode())).withRel(lang.getId().getLanguage())).toList();
                    Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel();
                    Link relink = WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getAllCountries()).withRel("country");
                    return EntityModel.of(country, selfLink, relink).add(citiesLinks).add(languagesLinks);
                })
                .toList();
        return new ResponseEntity(CollectionModel.of(countries,
                WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getAllCountries()).withSelfRel()), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CountryEntity>> getCountry(@PathVariable final String id) {
        if(id.length()!=3){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EntityModel<CountryEntity> country = EntityModel.of(worldService.getCountry(id));
        if(country.getContent() == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND); // maybe bad request, might need to refactor some validation for Strings longer or shorter than 3
        }
        else {
            return new ResponseEntity<>(country.add(citiesLinks(country.getContent())).add(languagesLinks(country.getContent())), HttpStatus.OK);
        }
    }

    private List<Link> citiesLinks(CountryEntity country){
        return worldService.allCities().stream().filter(cities -> cities.getCountryCode().getCode().equals(country.getCode())).toList()
                .stream().map(
                        city -> WebMvcLinkBuilder.linkTo(
                                methodOn(CityController.class).getCityById(city.getCountryCode())).withRel(city.getName())).toList();
    }
    private List<Link> languagesLinks(CountryEntity country){
        return worldService.allLanguages().stream().filter(lang -> lang.getCountryCode().getCode().equals(country.getCode())).toList()
                .stream().map(
                        lang -> WebMvcLinkBuilder.linkTo(
                                methodOn(CountryLanguageController.class).getCountryLanguageById(lang.getCountryCode())).withRel(lang.getId().getLanguage())).toList();
    }
}
