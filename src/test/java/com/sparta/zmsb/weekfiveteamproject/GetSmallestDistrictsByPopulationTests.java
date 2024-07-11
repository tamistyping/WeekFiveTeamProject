package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GetSmallestDistrictsByPopulationTests {

    @Autowired
    WorldService worldService;

    @Test
    @DisplayName("Check that getSmallestDistrictsByPopulation returns only 5 districts")
    void checkThatGetSmallestDistrictsByPopulationOnlyReturnsFiveDistricts() {
        int expected = 5;
        int actual = worldService.getSmallestDistrictsByPopulation().split(", ").length;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check that getSmallestDistrictsByPopulation returns districts with the smallest population")
    void checkThatGetSmallestDistrictsByPopulationReturnsSmallestDistricts() {
        String expected = "West Island, Fakaofo, Home Island, Wallis, Länsimaa";
        String actual = worldService.getSmallestDistrictsByPopulation();
        Assertions.assertEquals(expected, actual);
    }

}
