package com.sparta.zmsb.weekfiveteamproject.controllers;

import com.sparta.zmsb.weekfiveteamproject.entities.KeyEntity;
import com.sparta.zmsb.weekfiveteamproject.service.KeyService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/keys")
public class KeyController {

    private final KeyService keyService;

    @Autowired
    public KeyController(final KeyService keyService) {
        this.keyService = keyService;
    }

    @GetMapping("/generate")
    public ResponseEntity<EntityModel<KeyEntity>> generateKey(@Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey ) {
        KeyEntity key = keyService.createApiKey();
        return new ResponseEntity<>(EntityModel.of(key).add(WebMvcLinkBuilder.linkTo(methodOn(KeyController.class).getAllKeys(apiKey)).withRel("keys")), HttpStatus.CREATED);
    }
    @GetMapping("/search")
    public ResponseEntity<EntityModel<List<KeyEntity>>> getAllKeys(@Parameter(name = "x-api-key", description = "header", required = true) @RequestHeader("x-api-key") String apiKey ) {
        return ResponseEntity.ok(EntityModel.of(keyService.getAllKeys()).add(WebMvcLinkBuilder.linkTo(KeyController.class).withSelfRel()));
    }

}
