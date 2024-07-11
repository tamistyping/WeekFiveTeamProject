package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
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
                        CountryLanguageRepository countryLanguageRepository) {

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

    public float percentageOfGivenCountriesPopulationThatLivesInTheLargestCity(String countryName){

        //change to map for key city name, value percentage?

        List<CityEntity> cities = allCities();
        List<CountryEntity> countries = allCountries();

        String countryCode = countries.stream()
                .filter(cE -> cE.getName().equals(countryName))
                .map(CountryEntity :: getCode)
                .findFirst().orElse("Name not found");

        int countryTotalPopulation = countries.stream()
                .filter(ce -> ce.getCode().equals(countryCode))
                .map(CountryEntity::getPopulation)
                .findFirst().orElse(0);

        int biggestCityPopulation = cities.stream()
                .filter(ciEn -> ciEn.getCountryCode().equals(countryCode))
                .max(Comparator.comparingInt(CityEntity::getPopulation))
                .get().getPopulation();

        try{
            return (float) biggestCityPopulation /countryTotalPopulation*100;
        }
        catch(ArithmeticException e){
            e.printStackTrace();
            return 0;
        }
    }

    public List<CountryEntity> allCountries(){
        return countryRepository.findAll();
    }
    public List<CityEntity> allCities(){
        return cityRepository.findAll();
    }

    public CityRepository getCityRepository() {
        return cityRepository;
    }
    public CountryRepository getCountryRepository() {
        return countryRepository;
    }
    public CountryLanguageRepository getCountryLanguageRepository() {
        return countryLanguageRepository;
    }

    public ArrayList<CityEntity> getSmallestDistrictsByPopulation() {
        List<CityEntity> cities = allCities();
        ArrayList<CityEntity> sortedCities;
        ArrayList<CityEntity> sortedSmallestDistricts = new ArrayList<>();
        Comparator<CityEntity> comparator = Comparator.comparing(CityEntity::getPopulation);
        sortedCities = cities.stream().sorted(comparator).collect(Collectors.toCollection(ArrayList<CityEntity>::new));
        for (int i = 0; i < 5; i++) {
            sortedSmallestDistricts.add(sortedCities.get(i));
        }

        return sortedSmallestDistricts;
    }

    public int getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas() {
        return 1;
    }
}
