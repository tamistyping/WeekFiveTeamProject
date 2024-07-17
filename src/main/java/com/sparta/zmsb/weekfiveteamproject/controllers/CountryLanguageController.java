package com.sparta.zmsb.weekfiveteamproject.controllers;


import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<EntityModel<CountrylanguageEntity>>> getAllLanguages() {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
                .map(language -> EntityModel.of(language,
                        linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(languageModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<CountrylanguageEntity>>> getLanguageByCountryCode(@PathVariable String id) {
        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(id);

        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
                .map(language -> EntityModel.of(language,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getLanguageByCountryCode(id)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(languageModels).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
    }
    @GetMapping("/{id}/{language}")
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

    @PostMapping("/{countryCode}/{newLanguage}")
    public ResponseEntity<EntityModel<CountrylanguageEntity>> updateLanguage(
            @PathVariable String countryCode,
            @PathVariable String newLanguage,
            @RequestBody CountrylanguageEntity newEntity) {

        CountrylanguageEntityId newId = new CountrylanguageEntityId();
        newId.setCountryCode(countryCode);
        newId.setLanguage(newLanguage);

        newEntity.setId(newId);

        CountrylanguageEntity savedEntity = worldService.saveCountryLanguage(newEntity);

        EntityModel<CountrylanguageEntity> entityModel = EntityModel.of(savedEntity,
                linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCode(newId.getCountryCode())).withRel("language"),
                linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages"));

        return ResponseEntity.ok(entityModel);
    }

    //todo get all languages independent of country

}
