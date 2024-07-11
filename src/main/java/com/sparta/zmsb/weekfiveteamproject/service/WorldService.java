package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryLanguageRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
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

    public List<CountryEntity> countriesWithNoHeadOfState() {
        return countryRepository.findAll().stream()
                .filter(country -> country.getHeadOfState() == null || country.getHeadOfState().isBlank())
                .collect(Collectors.toList());
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
    @Transactional
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
    public List<CountrylanguageEntity> allLanguages(){
        return countryLanguageRepository.findAll();
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

    // CRUD for CountryLanguageEntity
    // Create
    public void createCountryLanguageEntityEntry(CountrylanguageEntity countrylanguageEntity) {
        countryLanguageRepository.saveAndFlush(countrylanguageEntity);
    }
    // Read
    public ArrayList<CountrylanguageEntity> getCountryLanguagesByCountryCode(CountrylanguageEntityId countrylanguageEntityId) {
        ArrayList<CountrylanguageEntity> countryLanguageEntities = new ArrayList<>();
        List<CountrylanguageEntity> allLanguages = countryLanguageRepository.findAll();
        for (CountrylanguageEntity countrylanguageEntity : allLanguages) {
            if (countrylanguageEntityId.getCountryCode().equals(countrylanguageEntity.getCountryCode())) {
                countryLanguageEntities.add(countrylanguageEntity);
            }
        }

        return countryLanguageEntities;
    }
    // Update
    public void updateCountryLanguageEntity(CountrylanguageEntity countrylanguageEntity) {
        countryLanguageRepository.saveAndFlush(countrylanguageEntity);
    }
    // Delete
    public void deleteCountryLanguageEntity(CountrylanguageEntity countrylanguageEntity) {
        countryLanguageRepository.delete(countrylanguageEntity);
    }
}
