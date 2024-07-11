package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PercentageOfPopulationTests {

    @Autowired
    WorldService worldService;

    @Test
    @DisplayName("When creating City Repository List Size should be 500")
    void whenCreatingCityRepositoryListSizeShouldBe500() {
        int expected = 4079;
        List<CityEntity> cityRepoSize = worldService.allCities();
        int actual = cityRepoSize.size();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @DisplayName("Given tables of countries and cities then return country with most cities")
    void returnCountryWithMostCities() {

    }
}
