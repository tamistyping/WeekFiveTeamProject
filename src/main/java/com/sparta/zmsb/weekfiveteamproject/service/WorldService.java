package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryLanguageRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorldService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryLanguageRepository countryLanguageRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryRepository countryRepository,
                        CountryLanguageRepository countryLanguageRepository
                        ) {

        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryLanguageRepository = countryLanguageRepository;

    }

    public String whichCountryHasMostCities(){

        List<CityEntity> cities = allCities();
        Map<String , Long> mostCitiesCount = cities.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCountryCode().getName()
                        , Collectors.counting()));

        return mostCitiesCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("");
    }

    public double percentageOfGivenCountriesPopulationThatLivesInTheLargestCity(String countryName){

        List<CityEntity> cities = allCities();
        List<CountryEntity> countries = allCountries();

        String countryCode = getCountryCode(countryName, countries);
        int countryTotalPopulation = getCountryTotalPopulation(countries, countryCode);
        int biggestCityPopulation = getBiggestCityPopulation(cities, countryCode);
        return (double) biggestCityPopulation /countryTotalPopulation*100;
    }

    public String getCountryCode(String countryName, List<CountryEntity> countries) {
        return countries.stream()
                .filter(cE -> cE.getName().equals(countryName))
                .map(CountryEntity :: getCode)
                .findFirst().orElse("");
    }

    public int getBiggestCityPopulation(List<CityEntity> cities, String countryCode) {

        if(!doesCityExist(cities,countryCode)){
            return cities.stream()
                    .filter(ciEn -> ciEn.getCountryCode().getCode().equals(countryCode))
                    .max(Comparator.comparingInt(CityEntity::getPopulation))
                    .get().getPopulation();
        }
        else return 0;
    }

    public int getCountryTotalPopulation(List<CountryEntity> countries, String countryCode) {
        return countries.stream()
                .filter(ce -> ce.getCode().equals(countryCode))
                .map(CountryEntity::getPopulation)
                .findFirst().orElse(1);
    }

    public boolean doesCityExist(List<CityEntity> cities ,String countryCode){
        return cities.stream()
                .filter(ciEn -> ciEn.getCountryCode().getCode().equals(countryCode))
                .max(Comparator.comparingInt(CityEntity::getPopulation))
                .isEmpty();
    }

    public List<CountryEntity> allCountries(){
        return countryRepository.findAll();
    }
    public List<CityEntity> allCities(){
        return cityRepository.findAll();
    }
    public List<CountrylanguageEntity> allLanguages(){
        return countryLanguageRepository.findAll();
    }
}
