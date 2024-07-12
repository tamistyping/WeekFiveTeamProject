package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AmountOfPeopleSpeakingOfficialLanguageTests {

    @Autowired
    private WorldService worldService;

    @Test
    void testAmountOfPeopleSpeakingOfficialLanguage() {
        String countryName = "United States";
        int population = worldService.amountOfPeopleSpeakingOfficialLanguage(countryName);
        System.out.println("Population speaking official language in " + countryName + ": " + population);
        Assertions.assertEquals(239943734, population, "Population should be 239943734");
    }

    @Test
    void testAmountOfPeopleSpeakingOfficialLanguageInFrance() {
        String countryName = "France";
        int population = worldService.amountOfPeopleSpeakingOfficialLanguage(countryName);
        System.out.println("Population speaking official language in " + countryName + ": " + population);
        Assertions.assertNotEquals(0, population, "Population should not be zero");
    }

    @Test
    @Transactional
    @DisplayName("Return 0 when country code does not exist")
    void calculatePeopleSpeakingOfficialLanguage_NonExistentCountryCode() {
        String nonExistentCountryCode = "XLS";
        int expectedPopulation = 0;
        int actualPopulation = worldService.amountOfPeopleSpeakingOfficialLanguage(nonExistentCountryCode);
        Assertions.assertEquals(expectedPopulation, actualPopulation);
    }
}
