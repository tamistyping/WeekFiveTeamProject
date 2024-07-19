package com.sparta.zmsb.weekfiveteamproject.controllers;


import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/languages")
public class CountryLanguageController {

    @Autowired
    private final WorldService worldService;

    public CountryLanguageController(WorldService worldService) {
        this.worldService = worldService;
    }

    @GetMapping("/search/all-languages")
    public ResponseEntity<List<EntityModel<String>>> getAllUniqueLanguages(){
        List<EntityModel<String>> allLanguages = worldService.getAllLanguages().stream()
                .map(language -> EntityModel.of(language, countriesLinks(language)
                        )).toList();
        return new ResponseEntity<>(allLanguages, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EntityModel<CountrylanguageEntity>>> getAllLanguages() {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
                .map(language -> EntityModel.of(language,
                        linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(languageModels);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<CollectionModel<EntityModel<CountrylanguageEntity>>> getLanguageByCountryCode(@PathVariable String id) {
        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(id);

        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
                .map(language -> EntityModel.of(language,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getLanguageByCountryCode(id)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(languageModels).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
    }
    @GetMapping("/search/{id}/{language}")
    public ResponseEntity<EntityModel<CountrylanguageEntity>> getLanguageByCountryCodeAndLanguage(@PathVariable String id, @PathVariable String language) {

        Optional<CountrylanguageEntity> returnLanguage = worldService.getCountryLanguagesByCountryCode(id)
                .stream().filter(cLE -> cLE.getId().getLanguage()
                        .equals(language)).toList().stream().findFirst();

        if (returnLanguage.isEmpty()){
            return new ResponseEntity<>(/*new ResourceNotFoundException,*/HttpStatus.NOT_FOUND);
        }
        else{

            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCodeAndLanguage(id, language)).withSelfRel();
            Link parentLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages");
            return ResponseEntity.ok(EntityModel.of(returnLanguage.get(), selfLink, parentLink).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
        }
    }

    @PostMapping("/secure/new")
    public ResponseEntity<EntityModel<CountrylanguageEntity>> createLanguage(
            @RequestBody @Valid CountrylanguageEntity newEntity, HttpServletRequest request) {


        CountrylanguageEntity savedEntity = worldService.saveCountryLanguage(newEntity);

        EntityModel<CountrylanguageEntity> entityModel = EntityModel.of(savedEntity,
                linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCode(savedEntity.getId().getCountryCode())).withRel("language"),
                linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages"));

        URI location = URI.create(request.getRequestURL().toString() + "/" + newEntity.getCountryCode().getCode() + "/" + savedEntity.getId().getLanguage());
        return ResponseEntity.created(location).body(entityModel);
    }
    @PutMapping("/secure/update/{countryCode}/{language}")
    public ResponseEntity<EntityModel<CountrylanguageEntity>> updateLanguage(
            @PathVariable String countryCode,
            @PathVariable String language,
            @RequestBody CountrylanguageEntity newEntity
    ){
        boolean isLanguagePresent = false;
        if(!countryCode.equals(newEntity.getCountryCode().getCode())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(countryCode);
        for(CountrylanguageEntity lang : languages){
            if(lang.getId().getLanguage().equals(language)){
                isLanguagePresent = true;
                worldService.deleteCountryLanguageEntity(lang);
            }
        }
        if(isLanguagePresent){
            worldService.updateCountryLanguageEntity(newEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/secure/delete/{countrycode}/{language}")
    public ResponseEntity<EntityModel<CountrylanguageEntity>> deleteLanguage(@PathVariable String countrycode, @PathVariable String language) {
        if(countrycode.length() != 3){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CountryEntity c = worldService.getCountry(countrycode);
        if(c==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<CountrylanguageEntity> countrylanguageEntities = worldService.getCountryLanguagesByCountryCode(countrycode);
        if(countrylanguageEntities.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for(CountrylanguageEntity countrylanguageEntity : countrylanguageEntities){
            if(countrylanguageEntity.getId().getLanguage().equals(language)){
                worldService.deleteCountryLanguageEntity(countrylanguageEntity);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<Link> countriesLinks(String language){
        return worldService.getAllCountriesByLanguage(language).stream().map(
                country -> WebMvcLinkBuilder.linkTo(
                        methodOn(CountryController.class).getCountry(country.getCode())).withRel(country.getName())).toList();
    }

}
