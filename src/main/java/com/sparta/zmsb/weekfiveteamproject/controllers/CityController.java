package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final WorldService worldService;

    public CityController(WorldService worldService) {
        this.worldService = worldService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<CityEntity>> createCity(@RequestBody @Valid CityEntity cityEntity, HttpServletRequest request) {
        List<CountryEntity> countries = worldService.allCountries();

        countries = countries.stream().filter(c -> c.getCode().equals(cityEntity.getCountryCode().getCode())).toList();

        if (!countries.getFirst().getCode().equals(cityEntity.getCountryCode().getCode()) ) {
            try {
                throw new ResourceNotFoundException("Country with code: " + cityEntity.getCountryCode().getCode() + " does not exist");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        List<EntityModel<CityEntity>> cityEntityModel = Stream.of(worldService.createCity(cityEntity))
                .map(
                        city ->
                        {
                            List<Link> countryLinks =
                                    Stream.of(city.getCountryCode().getCode())
                                            .map(
                                                    code -> WebMvcLinkBuilder.linkTo(
                                                            methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName()))
                                            .toList();
                            Link selfLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getCity(city.getId())).withSelfRel();
                            Link relLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getAllCities()).withRel("city");
                            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
                        })
                .toList();

        URI location = URI.create(request.getRequestURL().toString() + "/" + cityEntity.getId());

        return ResponseEntity.created(location).body(cityEntityModel.getFirst());
    }

    @GetMapping
    public CollectionModel<EntityModel<CityEntity>> getAllCities() {
        List<EntityModel<CityEntity>> cities = worldService.allCities()
                .stream()
                .map(
                        city ->
                        {
                            List<Link> countryLinks =
                                    Stream.of(city.getCountryCode().getCode())
                                            .map(
                                                    code -> WebMvcLinkBuilder.linkTo(
                                                            methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName()))
                                            .toList();
                            Link selfLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getCity(city.getId())).withSelfRel();
                            Link relLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getAllCities()).withRel("city");
                            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
                        })
                .toList();
        return CollectionModel.of(cities, WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withSelfRel());
    }

    @GetMapping("/{id}")
    public CollectionModel<EntityModel<CityEntity>> getCity(@PathVariable @Valid Integer id) {
        List<EntityModel<CityEntity>> cityEntityModel = Stream.of(worldService.getCityById(id))
                .map(
                        city ->
                        {
                            List<Link> countryLinks =
                                    Stream.of(city.getCountryCode().getCode())
                                            .map(
                                                    code -> WebMvcLinkBuilder.linkTo(
                                                            methodOn(CountryController.class).getCountry(city.getCountryCode().getCode())).withRel(city.getCountryCode().getName()))
                                            .toList();
                            Link selfLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getCity(city.getId())).withSelfRel();
                            Link relLink = WebMvcLinkBuilder.linkTo(
                                    methodOn(CityController.class).getAllCities()).withRel("city");
                            return EntityModel.of(city, selfLink, relLink).add(countryLinks);
                        })
                .toList();
        return CollectionModel.of(cityEntityModel, WebMvcLinkBuilder.linkTo(methodOn(CityController.class).getAllCities()).withSelfRel());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<CityEntity>> updateCity(@PathVariable @Valid Integer id, @RequestBody @Valid CityEntity cityEntity) {
        List<CityEntity> cities = worldService.allCities();
        List<CountryEntity> countries = worldService.allCountries();

        countries = countries.stream().filter(c -> c.getCode().equals(cityEntity.getCountryCode().getCode())).toList();

        if (!id.equals(cityEntity.getId())) {
            try {
                throw new InvalidEndpointException("City ID: " + cityEntity.getId() + " does not match the endpoint ID: " + id);
            } catch (InvalidEndpointException e) {
                throw new RuntimeException(e);
            }
        } else if (!cities.contains(cityEntity)) {
            try {
                throw new ResourceNotFoundException("City ID: " + cityEntity.getId() + " does not exist");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else if (!countries.getFirst().getCode().equals(cityEntity.getCountryCode().getCode()) ) {
            try {
                throw new ResourceNotFoundException("Country with code: " + cityEntity.getCountryCode().getCode() + " does not exist");
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            worldService.updateCity(cityEntity);
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<CityEntity>>> deleteCity(@PathVariable @Valid Integer id) {
        if (worldService.getCityById(id) == null) {
            try {
                throw new InvalidEndpointException("Endpoint: " + id + " does not correspond to a City");
            } catch (InvalidEndpointException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.noContent().build();
    }


    // Temporary classes - waiting for Tam (MVP+)
    private static class ResourceNotFoundException extends Throwable {
        public ResourceNotFoundException(String s) {
        }
    }

    private static class InvalidEndpointException extends Throwable {
        public InvalidEndpointException(String s) {
        }
    }

}