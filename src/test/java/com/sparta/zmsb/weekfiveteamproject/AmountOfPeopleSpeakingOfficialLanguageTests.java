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
        String countryCode = "USA";
        int population = worldService.amountOfPeopleSpeakingOfficialLanguage(countryCode);
        System.out.println("Population speaking official language in " + countryCode + ": " + population);
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
