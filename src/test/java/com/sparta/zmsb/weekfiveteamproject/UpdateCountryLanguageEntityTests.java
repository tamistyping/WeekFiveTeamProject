package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import com.sparta.zmsb.weekfiveteamproject.updates.UpdateCountryLanguage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
public class UpdateCountryLanguageEntityTests {

    @Autowired
    WorldService worldService;



    @BeforeEach
    void setUp() {
        worldService.updateCountryLanguageEntity(UpdateCountryLanguage.createEntity(worldService.allLanguages().stream().filter(l -> l.getCountryCode().getCode().equals("ABW")).findFirst().get(), "T", "is official"));
        worldService.updateCountryLanguageEntity(UpdateCountryLanguage.createEntity(worldService.allLanguages().stream().filter(l -> l.getCountryCode().getCode().equals("ABW")).findFirst().get(), "5.3", "percentage"));
    }

    @Test
    @DisplayName("Check if update entity returns correct entity before pushing to database")
    void checkUpdateEntityReturnsCorrectEntityBeforeDatabaseUpdateIsOfficial() {
        CountrylanguageEntity expected = worldService.allLanguages().stream().filter(c-> c.getCountryCode().getCode().equals("ABW")).findFirst().get();
        expected.setIsOfficial("F");
        CountrylanguageEntity actual = UpdateCountryLanguage.createEntity(expected, "F", "5.3");
        Assertions.assertEquals(expected, actual);

    }

    @Test
    @DisplayName("Check if update entity returns correct entity before pushing to database")
    void checkUpdateEntityReturnsCorrectEntityBeforeDatabaseUpdatePercentage() {
        CountrylanguageEntity expected = worldService.allLanguages().stream().filter(c-> c.getCountryCode().getCode().equals("ABW")).findFirst().get();
        expected.setPercentage(BigDecimal.valueOf(10.0));
        CountrylanguageEntity actual = UpdateCountryLanguage.createEntity(expected, "T", "10.0");
        Assertions.assertEquals(expected, actual);

    }
}
