package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryLanguageRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<CountryEntity> countriesWithNoHeadOfState() {
        return countryRepository.findAll().stream()
                .filter(country -> country.getHeadOfState() == null || country.getHeadOfState().isBlank())
                .collect(Collectors.toList());
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

    public int amountOfPeopleSpeakingOfficialLanguage(String countryName) {
        List<CountryEntity> countries = allCountries();

        String countryCode = countries.stream()
                .filter(cE -> cE.getName().equals(countryName))
                .map(CountryEntity::getCode)
                .findFirst()
                .orElse("Name not found");

        if ("Name not found".equals(countryCode)) {
            return 0;
        }

        int population = countries.stream()
                .filter(cE -> cE.getCode().equals(countryCode))
                .findFirst()
                .map(CountryEntity::getPopulation)
                .orElse(0);

        List<CountrylanguageEntity> officialLanguages = officialLanguagesOfCountry(countryCode);
        Optional<CountrylanguageEntity> mostSpoken = mostSpokenLanguageInCountry(officialLanguages);

        if (mostSpoken.isPresent()) {
            return (int) (population * mostSpoken.get().getPercentage().doubleValue() / 100);
        } else {
            return 0;
        }
    }

    private List<CountrylanguageEntity> officialLanguagesOfCountry(String countryCode) {
        List<CountrylanguageEntity> languages = countryLanguageRepository.findAll();
        List<CountrylanguageEntity> officialLanguages = new ArrayList<>();
        for (CountrylanguageEntity language : languages) {
            if (language.getId().getCountryCode().equals(countryCode) && language.getIsOfficial().equals("T")) {
                officialLanguages.add(language);
            }
        }
        return officialLanguages;
    }

    private Optional<CountrylanguageEntity> mostSpokenLanguageInCountry(List<CountrylanguageEntity> languages) {
        if (languages.isEmpty()) {
            return Optional.empty();
        }
        CountrylanguageEntity mostSpokenLanguage = languages.get(0);
        for (CountrylanguageEntity language : languages) {
            if (language.getPercentage().compareTo(mostSpokenLanguage.getPercentage()) > 0) {
                mostSpokenLanguage = language;
            }
        }
        return Optional.of(mostSpokenLanguage);
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
}
