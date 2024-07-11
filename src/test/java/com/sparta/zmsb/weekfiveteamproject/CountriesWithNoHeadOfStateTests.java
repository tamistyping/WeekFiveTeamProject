package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Transactional
@SpringBootTest
public class CountriesWithNoHeadOfStateTests {

    @Autowired
    private WorldService worldService;

    @Test
    void testGetCountriesWithoutAHeadOfStateReturnsAtLeast1Country() {
        List<CountryEntity> countriesWithoutAHeadOfState = worldService.countriesWithNoHeadOfState();
        Assertions.assertFalse(countriesWithoutAHeadOfState.isEmpty(), "There should be at least one country without a head of state");
    }

    @Test
    void testGetCountriesWithoutAHeadOfStateReturnsOnlyCountriesWithoutHeadsOfState() {
        List<CountryEntity> countriesWithoutAHeadOfState = worldService.countriesWithNoHeadOfState();

        boolean allCountriesWithoutHeadOfState = countriesWithoutAHeadOfState.stream()
                .allMatch(country -> country.getHeadOfState() == null || country.getHeadOfState().isBlank());

        Assertions.assertTrue(allCountriesWithoutHeadOfState, "All countries should have no head of state");
    }
}
