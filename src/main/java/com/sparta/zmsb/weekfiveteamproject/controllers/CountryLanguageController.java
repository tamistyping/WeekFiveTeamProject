package com.sparta.zmsb.weekfiveteamproject.controllers;


import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.exceptions.ResourceNotFoundException;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/api/languages")
public class CountryLanguageController {

    @Autowired
    private final WorldService worldService;

    public CountryLanguageController(WorldService worldService) {
        this.worldService = worldService;
    }

//    @GetMapping("/search/all-languages")
//    public ResponseEntity<List<EntityModel<String>>> getAllUniqueLanguages(){
//        List<EntityModel<String>> allLanguages = worldService.getAllLanguages().stream()
//                .map(language -> EntityModel.of(language, countriesLinks(language)
//                        )).toList();
//        return new ResponseEntity<>(allLanguages, HttpStatus.OK);
//    }

//

//    @GetMapping("/search")
//    public ResponseEntity<List<EntityModel<CountrylanguageEntity>>> getAllLanguages() {
//        List<CountrylanguageEntity> languages = worldService.allLanguages();
//        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
//                .map(language -> EntityModel.of(language,
//                        linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages")))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(languageModels);
//    }



    @GetMapping
    public String getLanguages(Model model) {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        model.addAttribute("languages", languages);
        model.addAttribute("searchPage", false);
        return "country_language_templates/languages";
    }

    @GetMapping("/search")
    public String searchLanguages(@RequestParam("countryCode") String countryCode, @RequestParam("languageName") String languageString, RedirectAttributes redirectAttributes) {
        List<CountrylanguageEntity> languages = worldService.allLanguages();
        for (CountrylanguageEntity language : languages) {
            if (language.getId().getCountryCode().equalsIgnoreCase(countryCode) && language.getId().getLanguage().equalsIgnoreCase(languageString)) {
                redirectAttributes.addAttribute("countryCode", language.getId().getCountryCode());
                redirectAttributes.addAttribute("language", language.getId().getLanguage());
                return "redirect:/api/languages/search/{countryCode}/{language}";
            }
        }
        return "redirect:/api/languages";
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

        return "country_language_templates/languages";
    }

//

//    @GetMapping("/search/{id}")
//    public ResponseEntity<CollectionModel<EntityModel<CountrylanguageEntity>>> getLanguageByCountryCode(@PathVariable String id) {
//        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(id);
//
//        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
//                .map(language -> EntityModel.of(language,
//                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getLanguageByCountryCode(id)).withSelfRel(),
//                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages")))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(CollectionModel.of(languageModels).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
//    }



//    @GetMapping("/search/{id}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> getLanguageByCountryCodeAndLanguage(@PathVariable String id, @PathVariable String language) {
//
//        Optional<CountrylanguageEntity> returnLanguage = worldService.getCountryLanguagesByCountryCode(id)
//                .stream().filter(cLE -> cLE.getId().getLanguage()
//                        .equals(language)).toList().stream().findFirst();
//
//        if (returnLanguage.isEmpty()){
//            return new ResponseEntity<>(/*new ResourceNotFoundException,*/HttpStatus.NOT_FOUND);
//        }
//        else{
//
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCodeAndLanguage(id, language)).withSelfRel();
//            Link parentLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages");
//            return ResponseEntity.ok(EntityModel.of(returnLanguage.get(), selfLink, parentLink).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
//        }
//    }

//    @PostMapping("/secure/new")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> createLanguage(
//            @Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey,
//            @RequestBody @Valid CountrylanguageEntity newEntity, HttpServletRequest request) {
//
//
//        CountrylanguageEntity savedEntity = worldService.saveCountryLanguage(newEntity);
//
//        EntityModel<CountrylanguageEntity> entityModel = EntityModel.of(savedEntity,
//                linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCode(savedEntity.getId().getCountryCode())).withRel("language"),
//                linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages"));
//
//        URI location = URI.create(request.getRequestURL().toString() + "/" + newEntity.getCountryCode().getCode() + "/" + savedEntity.getId().getLanguage());
//        return ResponseEntity.created(location).body(entityModel);
//    }


    @GetMapping("/create")
    public String createLanguage(Model model) {
        model.addAttribute("language", new CountrylanguageEntity());
        model.addAttribute("create", true);
        model.addAttribute("update", false);
        return "country_language_templates/create_language";
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

        return "redirect:/api/languages/create?";
    }


//    @PutMapping("/secure/update/{countryCode}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> updateLanguage(
//            @PathVariable String countryCode,
//            @PathVariable String language,
//            @RequestBody CountrylanguageEntity newEntity
//    ){
//        boolean isLanguagePresent = false;
//        if(!countryCode.equals(newEntity.getCountryCode().getCode())){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(countryCode);
//        for(CountrylanguageEntity lang : languages){
//            if(lang.getId().getLanguage().equals(language)){
//                isLanguagePresent = true;
//                worldService.deleteCountryLanguageEntity(lang);
//            }
//        }
//        if(isLanguagePresent){
//            worldService.updateCountryLanguageEntity(newEntity);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @DeleteMapping("/secure/delete/{countrycode}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> deleteLanguage(
//            @Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey,
//            @PathVariable String countrycode, @PathVariable String language) {
//        if(countrycode.length() != 3){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        CountryEntity c = worldService.getCountry(countrycode);
//        if(c==null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<CountrylanguageEntity> countrylanguageEntities = worldService.getCountryLanguagesByCountryCode(countrycode);
//        if(countrylanguageEntities.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        for(CountrylanguageEntity countrylanguageEntity : countrylanguageEntities){
//            if(countrylanguageEntity.getId().getLanguage().equals(language)){
//                worldService.deleteCountryLanguageEntity(countrylanguageEntity);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private List<Link> countriesLinks(String language){
//        return worldService.getAllCountriesByLanguage(language).stream().map(
//                country -> WebMvcLinkBuilder.linkTo(
//                        methodOn(CountryController.class).getCountry(country.getCode())).withRel(country.getName())).toList();
//    }

//    @GetMapping("/search/all-languages")
//    public ResponseEntity<List<String>> getAllUniqueLanguages(){
//
////        List<EntityModel<String>> allLanguages = worldService.getAllLanguages().stream()
////                .map(language -> EntityModel.of(language, countriesLinks(language)
////                        )).toList();
//        return new ResponseEntity<>(worldService.getAllLanguages(), HttpStatus.OK);
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<EntityModel<CountrylanguageEntity>>> getAllLanguages() {
//        List<CountrylanguageEntity> languages = worldService.allLanguages();
//        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
//                .map(language -> EntityModel.of(language,
//                        linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages")))
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(languageModels);
//    }
//
//    @GetMapping("/search/{id}")
//    public ResponseEntity<CollectionModel<EntityModel<CountrylanguageEntity>>> getLanguageByCountryCode(@PathVariable String id) {
//        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(id);
//
//        List<EntityModel<CountrylanguageEntity>> languageModels = languages.stream()
//                .map(language -> EntityModel.of(language,
//                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getLanguageByCountryCode(id)).withSelfRel(),
//                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages")))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(CollectionModel.of(languageModels).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
//    }
//    @GetMapping("/search/{id}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> getLanguageByCountryCodeAndLanguage(@PathVariable String id, @PathVariable String language) {
//
//        Optional<CountrylanguageEntity> returnLanguage = worldService.getCountryLanguagesByCountryCode(id)
//                .stream().filter(cLE -> cLE.getId().getLanguage()
//                        .equals(language)).toList().stream().findFirst();
//
//        if (returnLanguage.isEmpty()){
//            return new ResponseEntity<>(/*new ResourceNotFoundException,*/HttpStatus.NOT_FOUND);
//        }
//        else{
//
//            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCodeAndLanguage(id, language)).withSelfRel();
//            Link parentLink = WebMvcLinkBuilder.linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("country languages");
//            return ResponseEntity.ok(EntityModel.of(returnLanguage.get(), selfLink, parentLink).add(WebMvcLinkBuilder.linkTo(methodOn(CountryController.class).getCountry(id)).withRel("parent-country")));
//        }
//    }
//
//    @PostMapping("/secure/new")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> createLanguage(
//            @Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey,
//            @RequestBody @Valid CountrylanguageEntity newEntity, HttpServletRequest request) {
//
//
//        CountrylanguageEntity savedEntity = worldService.saveCountryLanguage(newEntity);
//
//        EntityModel<CountrylanguageEntity> entityModel = EntityModel.of(savedEntity,
//                linkTo(methodOn(CountryLanguageController.class).getLanguageByCountryCode(savedEntity.getId().getCountryCode())).withRel("language"),
//                linkTo(methodOn(CountryLanguageController.class).getAllLanguages()).withRel("all-languages"));
//
//        URI location = URI.create(request.getRequestURL().toString() + "/" + newEntity.getCountryCode().getCode() + "/" + savedEntity.getId().getLanguage());
//        return ResponseEntity.created(location).body(entityModel);
//    }
//    @PutMapping("/secure/update/{countryCode}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> updateLanguage(
//            @PathVariable String countryCode,
//            @PathVariable String language,
//            @RequestBody CountrylanguageEntity newEntity
//    ){
//        boolean isLanguagePresent = false;
//        if(!countryCode.equals(newEntity.getCountryCode().getCode())){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        List<CountrylanguageEntity> languages = worldService.getCountryLanguagesByCountryCode(countryCode);
//        for(CountrylanguageEntity lang : languages){
//            if(lang.getId().getLanguage().equals(language)){
//                isLanguagePresent = true;
//                worldService.deleteCountryLanguageEntity(lang);
//            }
//        }
//        if(isLanguagePresent){
//            worldService.updateCountryLanguageEntity(newEntity);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

//    @DeleteMapping("/secure/delete/{countrycode}/{language}")
//    public ResponseEntity<EntityModel<CountrylanguageEntity>> deleteLanguage(
//            @Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey,
//            @PathVariable String countrycode, @PathVariable String language) {
//        if(countrycode.length() != 3){
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        CountryEntity c = worldService.getCountry(countrycode);
//        if(c==null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<CountrylanguageEntity> countrylanguageEntities = worldService.getCountryLanguagesByCountryCode(countrycode);
//        if(countrylanguageEntities.isEmpty()){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        for(CountrylanguageEntity countrylanguageEntity : countrylanguageEntities){
//            if(countrylanguageEntity.getId().getLanguage().equals(language)){
//                worldService.deleteCountryLanguageEntity(countrylanguageEntity);
//            }
//        }
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private List<Link> countriesLinks(String language){
//        return worldService.getAllCountriesByLanguage(language).stream().map(
//                country -> WebMvcLinkBuilder.linkTo(
//                        methodOn(CountryController.class).getCountry(country.getCode())).withRel(country.getName())).toList();
//    }

    @GetMapping("/update/{countryCode}/{language}")
    public String updateLanguage(@PathVariable String countryCode, @PathVariable String language, Model model) {
        List<CountrylanguageEntity> languages = worldService.allLanguages().stream().filter(l -> l.getId().getCountryCode().equalsIgnoreCase(countryCode) && l.getId().getLanguage().equalsIgnoreCase(language)).toList();
        if (languages.isEmpty()) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }
        model.addAttribute("language", languages.getFirst());
        model.addAttribute("update", true);
        model.addAttribute("create", false);
        return "country_language_templates/create_language";
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

        return "redirect:/api/languages/update/" + countryCode + "/" + language;
    }

    @PostMapping("/delete/{countryCode}/{language}")
    public String deleteLanguage(@PathVariable String countryCode, @PathVariable String language) {
        List<CountrylanguageEntity> languages = worldService.allLanguages().stream().filter(l -> l.getId().getCountryCode().equals(countryCode) && l.getId().getLanguage().equals(language)).toList();
        if (languages.isEmpty()) {
            throw new ResourceNotFoundException("Language with country code: " + countryCode + " and language: " + language + " not found");
        }

        worldService.deleteCountryLanguageEntity(languages.getFirst());
        return "redirect:/api/languages";
    }

}
