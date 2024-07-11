package com.sparta.zmsb.weekfiveteamproject;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import com.sparta.zmsb.weekfiveteamproject.updates.CityUpdate;
import com.sparta.zmsb.weekfiveteamproject.updates.CreateCity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class CRUDTestsForCityEntity {
    @Autowired
    private WorldService worldService;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private WorldService worldServiceWithMocks;

    @InjectMocks
    private CityUpdate cityUpdate;

    @InjectMocks
    private CreateCity createCity;

    @Test
    @DisplayName("Check that updateCity updates the city correctly")
    void updateCityUpdatesCorrectly() {
        CityEntity existingCity = new CityEntity();
        existingCity.setId(5);
        existingCity.setName("OldName");
        existingCity.setDistrict("OldDistrict");
        existingCity.setPopulation(1000);

        Mockito.when(cityRepository.findById(5)).thenReturn(Optional.of(existingCity));

        CityEntity updatedCityMock = new CityEntity();
        updatedCityMock.setId(5);
        updatedCityMock.setName("NewName");
        updatedCityMock.setDistrict("NewDistrict");
        updatedCityMock.setPopulation(2000);

        Mockito.when(cityRepository.save(Mockito.any(CityEntity.class))).thenReturn(updatedCityMock);

        CityEntity updatedCity = cityUpdate.updateCity(5, "NewName", "NewDistrict", 2000);

        Assertions.assertNotNull(updatedCity, "The updated city should not be null");
        Assertions.assertEquals("NewName", updatedCity.getName());
        Assertions.assertEquals("NewDistrict", updatedCity.getDistrict());
        Assertions.assertEquals(2000, updatedCity.getPopulation());

        Mockito.verify(cityRepository).save(Mockito.any(CityEntity.class));
    }


    @Test
    @DisplayName("Check that deleteCity deletes the city correctly")
    void deleteCityDeletesCorrectly() {
        CityEntity city = new CityEntity();
        city.setId(1);

        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.of(city));

        worldServiceWithMocks.deleteCity(1);

        Mockito.verify(cityRepository).delete(city);
    }

    @Test
    @DisplayName("Check that updateCity throws exception if city not found")
    void updateCityThrowsExceptionIfNotFound() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            cityUpdate.updateCity(1, "NewName", "NewDistrict", 2000);
        });
    }

    @Test
    @DisplayName("Check that deleteCity throws exception if city not found")
    void deleteCityThrowsExceptionIfNotFound() {
        Mockito.when(cityRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> {
            worldServiceWithMocks.deleteCity(1);
        });
    }

    @Test
    @DisplayName("Check that createCity creates a new city entity")
    void createCityCreatesNewCityEntity() {
        String cityName = "Utopia";
        String districtName = "UTP";
        int population = 5353;

        Mockito.when(cityRepository.save(Mockito.any(CityEntity.class))).thenAnswer(invocation -> {
            CityEntity city = invocation.getArgument(0);
            city.setId(1);
            return city;
        });

        CountryEntity countryCode = new CountryEntity();
        CityEntity createdCity = CreateCity.createCity(cityName, countryCode, districtName, population);

        Assertions.assertEquals(cityName, createdCity.getName());
        Assertions.assertEquals(districtName, createdCity.getDistrict());
        Assertions.assertEquals(population, createdCity.getPopulation());
    }
}
