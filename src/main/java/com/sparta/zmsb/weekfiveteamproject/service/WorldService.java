package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryLanguageRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
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

    @Transactional
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

    public String getSmallestDistrictsByPopulation() {
        List<CityEntity> cities = allCities();
        LinkedHashMap<String, Integer> districtPopulations = new LinkedHashMap<>();

        for (CityEntity city : cities) {
            if (districtPopulations.containsKey(city.getDistrict())) {
                districtPopulations.put(city.getDistrict(), districtPopulations.get(city.getDistrict()) + city.getPopulation());
            } else {
                districtPopulations.put(city.getDistrict(), city.getPopulation());
            }
        }

        ArrayList<Map.Entry<String, Integer>> sortedDistricts = new ArrayList<>(districtPopulations.entrySet());
        sortedDistricts.sort(Map.Entry.comparingByValue());

        StringBuilder smallestDistricts = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            smallestDistricts.append(sortedDistricts.get(i).getKey()).append(", ");
        }

        smallestDistricts.delete(smallestDistricts.length() - 2, smallestDistricts.length());

        return String.valueOf(smallestDistricts);
    }

    @Transactional
    public long getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas() {
        List<CityEntity> cities = allCities();
        Map<String , Long> mostCitiesCount = cities.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCountryCode().getName()
                        , Collectors.counting()));

        Optional<Long> cityCount = mostCitiesCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getValue);

        if (cityCount.isPresent()) {
            return cityCount.get();
        }
        return 0;
    }
}
