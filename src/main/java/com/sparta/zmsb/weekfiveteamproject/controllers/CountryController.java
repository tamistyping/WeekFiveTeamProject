package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import com.sparta.zmsb.weekfiveteamproject.updates.UpdateCountry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Objects;

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
                        this::getCountryEntityModel)
                .toList();
        return new ResponseEntity<>(CollectionModel.of(countries,
                WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getAllCountries()).withSelfRel()), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<CountryEntity>> getCountry(@PathVariable final String id) {
        if(id.length()!=3){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        EntityModel<CountryEntity> country = EntityModel.of(worldService.getCountry(id));
        if(country.getContent() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(country.add(citiesLinks(country.getContent())).add(languagesLinks(country.getContent())), HttpStatus.OK);
        }
    }

    @GetMapping("/with-no-head-of-state")
    public ResponseEntity<CollectionModel<EntityModel<CountryEntity>>> getCountriesWithNoHeadOfStates() {
        List<EntityModel<CountryEntity>> countries = worldService.countriesWithNoHeadOfState().stream()
                .map(this::getCountryEntityModel).toList();
        return new ResponseEntity<>(CollectionModel.of(countries,WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountriesWithNoHeadOfStates()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/country-with-most-cities")
    public ResponseEntity<EntityModel<CountryEntity>> getCountryWithMostCities() {
        EntityModel<CountryEntity> country = EntityModel.of
                (worldService.getCountry(worldService
                        .getCountryCode(worldService.whichCountryHasMostCities()
                                , worldService.allCountries())));
        return new ResponseEntity<>(country.add(citiesLinks(country.getContent())), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<EntityModel<CountryEntity>> createCountry(@RequestBody @Valid CountryEntity country, HttpServletRequest request) {
        worldService.createNewCountry(country);
        URI location = URI.create(request.getRequestURL().toString() + "/" + country.getCode());
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel();
        return ResponseEntity.created(location).body(EntityModel.of(country).add(selfLink));
    }

    @PutMapping("/{id}") //No HATEOAS as no content return
    public ResponseEntity<EntityModel<CountryEntity>> updateCountry(@RequestBody @Valid CountryEntity country, @PathVariable final String id) {

        if(id.length()!=3){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CountryEntity correspondingCountry = worldService.getCountry(id);
        if(correspondingCountry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!Objects.equals(id, country.getCode())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        worldService.updateCountry(country);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}") //No HATEOAS as no content return
    public ResponseEntity<CountryEntity> deleteCountry(@PathVariable final String id) {
        if(id.length()!=3){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CountryEntity country = worldService.getCountry(id);
        if(country == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            worldService.deleteCountry(country);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
    private EntityModel<CountryEntity> getCountryEntityModel(CountryEntity country) {
        List<Link> citiesLinks = citiesLinks(country);
        List<Link> languagesLinks = languagesLinks(country);
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(country.getCode())).withSelfRel();
        Link relink = WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getAllCountries()).withRel("country");
        return EntityModel.of(country, selfLink, relink).add(citiesLinks).add(languagesLinks);
    }
}