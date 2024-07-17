package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.logging.AppLogger;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryLanguageRepository;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import com.sparta.zmsb.weekfiveteamproject.updates.CityUpdate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class WorldService {

    private static final Logger logger = AppLogger.getLogger();

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CountryLanguageRepository countryLanguageRepository;

    @Autowired
    public WorldService(CityRepository cityRepository, CountryRepository countryRepository,
                        CountryLanguageRepository countryLanguageRepository) {
        logger.info("WorldService constructor initialised");
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.countryLanguageRepository = countryLanguageRepository;
    }

    // Delete
    @Transactional
    public void deleteCity(Integer id) {
        logger.info("Entered deleteCity method");
        Optional<CityEntity> cityOptional = cityRepository.findById(id);

        if (cityOptional.isPresent()) {
            cityRepository.delete(cityOptional.get());
        } else {
            logger.info("Encountered error: city not found");
            throw new RuntimeException("City with id " + id + " not found");
        }
    }

    // Requirement
    public List<CountryEntity> countriesWithNoHeadOfState() {
        logger.info("Entered getCountriesWithNoHeadOfState method");
        return countryRepository.findAll().stream()
                .filter(country -> country.getHeadOfState() == null || country.getHeadOfState().isBlank())
                .collect(Collectors.toList());
    }

    // Requirement
    @Transactional
    public String whichCountryHasMostCities() {
        logger.info("Entered whichCountryHasMostCities method");
        List<CityEntity> cities = allCities();
        Map<String, Long> mostCitiesCount = cities.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCountryCode().getName()
                        , Collectors.counting()));

        return mostCitiesCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("");
    }

    // Requirement
    @Transactional
    public double percentageOfGivenCountriesPopulationThatLivesInTheLargestCity(String countryName) {
        logger.info("Entered percentageOfGivenCountriesPopulationThatLivesInTheLargestCity method");
        List<CityEntity> cities = allCities();
        List<CountryEntity> countries = allCountries();

        String countryCode = getCountryCode(countryName, countries);
        int countryTotalPopulation = getCountryTotalPopulation(countries, countryCode);
        int biggestCityPopulation = getBiggestCityPopulation(cities, countryCode);
        return (double) biggestCityPopulation / countryTotalPopulation * 100;
    }

    // Read
    public String getCountryCode(String countryName, List<CountryEntity> countries) {
        logger.info("Entered getCountryCode method");
        return countries.stream()
                .filter(cE -> cE.getName().equals(countryName))
                .map(CountryEntity::getCode)
                .findFirst().orElse("");
    }

    // Requirement
    public int getBiggestCityPopulation(List<CityEntity> cities, String countryCode) {
        logger.info("Entered getBiggestCityPopulation method");
        if (!doesCityExist(cities, countryCode)) {
            return cities.stream()
                    .filter(ciEn -> ciEn.getCountryCode().getCode().equals(countryCode))
                    .max(Comparator.comparingInt(CityEntity::getPopulation))
                    .get().getPopulation();
        } else return 0;
    }

    // Read
    public int getCountryTotalPopulation(List<CountryEntity> countries, String countryCode) {
        logger.info("Entered getCountryTotalPopulation method");
        return countries.stream()
                .filter(ce -> ce.getCode().equals(countryCode))
                .map(CountryEntity::getPopulation)
                .findFirst().orElse(1);
    }

    // Read
    public boolean doesCityExist(List<CityEntity> cities, String countryCode) {
        logger.info("Entered doesCityExist method");
        return cities.stream()
                .filter(ciEn -> ciEn.getCountryCode().getCode().equals(countryCode))
                .max(Comparator.comparingInt(CityEntity::getPopulation))
                .isEmpty();
    }

    // Requirement
    public int amountOfPeopleSpeakingOfficialLanguage(String countryName) {
        logger.info("Entered amountOfPeopleSpeakingOfficialLanguage method");
        List<CountryEntity> countries = allCountries();

        String countryCode = countries.stream()
                .filter(cE -> cE.getName().equals(countryName))
                .map(CountryEntity::getCode)
                .findFirst()
                .orElse("Name not found");

        if ("Name not found".equals(countryCode)) {
            logger.info("Encountered error: country code not found");
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
            logger.info("Encountered error: language not found");
            return 0;
        }
    }

    // Requirement
    private List<CountrylanguageEntity> officialLanguagesOfCountry(String countryCode) {
        logger.info("Entered officialLanguagesOfCountry method");
        List<CountrylanguageEntity> languages = countryLanguageRepository.findAll();
        List<CountrylanguageEntity> officialLanguages = new ArrayList<>();
        for (CountrylanguageEntity language : languages) {
            if (language.getId().getCountryCode().equals(countryCode) && language.getIsOfficial().equals("T")) {
                officialLanguages.add(language);
            }
        }
        return officialLanguages;
    }

    // Requirement
    private Optional<CountrylanguageEntity> mostSpokenLanguageInCountry(List<CountrylanguageEntity> languages) {
        logger.info("Entered mostSpokenLanguageInCountry method");
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

    // Read
    public List<CountryEntity> allCountries() {
        return countryRepository.findAll();
    }

    //Create
    public CityEntity createCity(CityEntity city) {
        return cityRepository.saveAndFlush(city);
    }
    // Read
    public List<CityEntity> allCities() {
        return cityRepository.findAll();
    }
    public CityEntity getCityById(Integer id) {
        return cityRepository.findById(id).orElse(null);
    }
    //Update
    public CityEntity updateCity(CityEntity city) {
        return new CityUpdate().updateCity(city.getId(), city.getName(), city.getDistrict(), city.getPopulation());
    }

    // Read
    public List<CountrylanguageEntity> allLanguages() {
        return countryLanguageRepository.findAll();
    }

    // Requirement
    public String getSmallestDistrictsByPopulation() {
        logger.info("Entered getSmallestDistrictsByPopulation method");
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

    // Requirement
    @Transactional
    public long getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas() {
        logger.info("Entered getNumberOfCitiesThatCountryWithHighestNumberOfCitiesHas method");
        List<CityEntity> cities = allCities();
        Map<String, Long> mostCitiesCount = cities.stream()
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
    @Transactional
    public void createCountryLanguageEntityEntry(CountrylanguageEntity countrylanguageEntity) {
        logger.info("Entered createCountryLanguageEntityEntry method");
        countryLanguageRepository.saveAndFlush(countrylanguageEntity);
    }
    // Read
    @Transactional
    public ArrayList<CountrylanguageEntity> getCountryLanguagesByCountryCode(CountrylanguageEntityId countrylanguageEntityId) {
        logger.info("Entered getCountryLanguagesByCountryCode method");
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
    @Modifying
    @Transactional
    public void updateCountryLanguageEntity(CountrylanguageEntity countrylanguageEntity) {
        logger.info("Entered updateCountryLanguageEntity method");
        countryLanguageRepository.saveAndFlush(countrylanguageEntity);
    }
    // Delete
    @Transactional
    public void deleteCountryLanguageEntity(CountrylanguageEntity countrylanguageEntity) {
        logger.info("Entered deleteCountryLanguageEntity method");
        countryLanguageRepository.delete(countrylanguageEntity);
    }

    // Create
    @Transactional
    public void createNewCountry(CountryEntity country) {
        logger.info("Entered createNewCountry method");
        countryRepository.saveAndFlush(country);
    }

    // Delete
    @Transactional
    public void deleteCountry(CountryEntity country) {
        logger.info("Entered deleteCountry method");
        countryRepository.delete(country);
    }

    // Read
    @Transactional
    public CountryEntity getCountry(String countryCode) {
        logger.info("Entered getCountry method");
        List<CountryEntity> allCountries = countryRepository.findAll();
        return allCountries.stream().filter(ce->ce.getCode().equals(countryCode)).findFirst().orElse(null);
    }

    // Update
    @Transactional
    public void updateCountry(CountryEntity country){
        logger.info("Entered updateCountry method");
        countryRepository.saveAndFlush(country);
    }
}
