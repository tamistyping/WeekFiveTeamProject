package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import jakarta.transaction.Transactional;
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
    @DisplayName("When creating country repository list size should be 239")
    void whenCreatingCountryRepositoryListSizeShouldBe239() {
        int expected = 239;
        List<CountryEntity> countryRepoList = worldService.allCountries();
        int actual = countryRepoList.size();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @DisplayName("When creating countryLanguage repository list size should be 984")
    void whenCreatingCountryLanguageRepositoryListSizeShouldBe984() {
        int expected = 984;
        List<CountrylanguageEntity> countryLangRepoList = worldService.allLanguages();
        int actual = countryLangRepoList.size();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given tables of countries and cities then return country with most cities")
    void returnCountryWithMostCities() {
        String expected = "The country with the most cities is China.\n" +
                "China has a total of 363 cities.";
        String actual = worldService.whichCountryHasMostCities();
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given real country name and tables countries and cities return percent double of highest pop city")
    void returnPercentDoubleOfHighestPopCity() {
        System.out.println(worldService.percentageOfGivenCountriesPopulationThatLivesInTheLargestCity("France"));
    }

    @Test
    @Transactional
    @DisplayName("Given incorrect country name, return 0.0")
    void incorrectCountryName() {
        double expected = 0.0d;
        double actual = worldService.percentageOfGivenCountriesPopulationThatLivesInTheLargestCity("Chinaa");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given country name return country code of country")
    void returnCountryCodeOfCountry() {
        String expected = "CHN";
        String actual = worldService.getCountryCode("China", worldService.allCountries());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given incorrect country name return empty string")
    void returnEmptyCodeIfCountryNameDoesNotExist() {
        String expected = "";
        String actual = worldService.getCountryCode("Chinaa", worldService.allCountries());
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given country code exists return false as not empty")
    void returnCountryCodeExists() {
        boolean expected = false;
        boolean actual = worldService.doesCityExist(worldService.allCities(), "CHN");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given country code does not exist return true as empty")
    void returnCountryCodeDoesNotExist() {
        boolean expected = true;
        boolean actual = worldService.doesCityExist(worldService.allCities(), "");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given country code return city with highest population")
    void returnCountryCodeCityWithHighestPopulation() {
        int expected = 9696300;
        int actual = worldService.getBiggestCityPopulation(worldService.allCities(),"CHN");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given no country code return 0")
    void returnZeroIfCountryCodeEmpty(){
        int expected = 0;
        int actual = worldService.getBiggestCityPopulation(worldService.allCities(), "");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given correct country code return countries total population")
    void returnCountriesTotalPopulationIfCountryCodeExists() {
        int expected = 1277558000;
        int actual = worldService.getCountryTotalPopulation(worldService.allCountries(), "CHN");
        Assertions.assertEquals(expected,actual);
    }

    @Test
    @Transactional
    @DisplayName("Given empty country code return 1")
    void returnOneIfCountryCodeEmpty() {
        int expected = 1;
        int actual = worldService.getCountryTotalPopulation(worldService.allCountries(), "");
        Assertions.assertEquals(expected,actual);
    }
}
