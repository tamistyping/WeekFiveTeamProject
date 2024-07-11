package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import com.sparta.zmsb.weekfiveteamproject.updates.UpdateCountry;
import com.sparta.zmsb.weekfiveteamproject.updates.CreateCountry;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;

@SpringBootTest
public class PercentageOfPopulationTests {

    @Autowired
    WorldService worldService;

    @BeforeEach
    void setUp() {
        worldService.updateCountry(UpdateCountry.updateCountry("China","Name", worldService.getCountry("CHN")));
    }

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
        String expected = "China";
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

    @Test
    @Transactional
    @DisplayName("Given update country method, change China name to input")
    void updateCountryCheckNameChange() {
        String input = "Chhiiiiiiinnaaaaa";
        String flag = "Name";
        System.out.println(worldService.getCountry("CHN").toString());
        worldService.updateCountry(UpdateCountry.updateCountry(input,flag, worldService.getCountry("CHN")));
        System.out.println(worldService.getCountry("CHN").toString());
    }

    @Test
    @Transactional
    @DisplayName("Given update country method has invalid flag, throw exception")
    void updateCountryWithInvalidFlag() {
        String input = "Chiiiiiiinnaaaaa";
        String flag = "Nam";
        Throwable exception = Assertions.assertThrows(InvalidParameterException.class, ()-> worldService.updateCountry(UpdateCountry.updateCountry(input,flag, worldService.getCountry("CHN"))));
        Assertions.assertEquals("Unexpected value: Nam", exception.getMessage());
    }

    @Test
    @DisplayName("Given new country created, return new country values")
    void createCountryTestForValues(){
        CountryEntity newCountry = CreateCountry.createCountry("ZZZ", "Zedlandia","Europe","Western Europe",
                BigDecimal.valueOf(2001304.35), (short) 2024, 12345678,BigDecimal.valueOf(90.00),BigDecimal.valueOf(5402398183.00),
                null, "Zedlandiaa", "Republic", "Captain Z", null, "ZZ" );
        System.out.println(newCountry.toString());
    }

    @Test
    @Transactional
    @DisplayName("Given create country method, create a country")
    void checkCreateAndDeleteCountryMethod() {
        worldService.createNewCountry(CreateCountry.createCountry("ZZZ", "Zedlandia", "Europe", "Western Europe",
                BigDecimal.valueOf(2001304.35), (short) 2024, 12345678, BigDecimal.valueOf(90.00), BigDecimal.valueOf(54023.00),
                null, "Zedlandiaa", "Republic", "Captain Z", null, "ZZ"));
        System.out.println(worldService.getCountry("ZZZ"));
        worldService.deleteCountry(worldService.getCountry("ZZZ"));
        System.out.println(worldService.getCountry("ZZZ"));
    }
}
