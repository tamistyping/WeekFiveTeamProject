package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.aspectj.weaver.World;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GetNumberOfCitiesThatCountryWithHighestNumberOfCitiesHasTests {

    @Autowired
    WorldService worldService;

    @Test
    @DisplayName("Check that getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas returns correctly")
    void getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHasReturnsCorrectly() {
        long expected = 363;
        long actual = worldService.getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas();
        Assertions.assertEquals(expected, actual);
    }

}
