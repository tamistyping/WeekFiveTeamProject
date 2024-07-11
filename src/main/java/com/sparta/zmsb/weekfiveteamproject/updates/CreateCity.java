package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;

public class CreateCity {

    public static CityEntity createCity(String name, CountryEntity countryCode, String district, Integer population) {
        CityEntity city = new CityEntity();
        city.setName(name);
        city.setCountryCode(countryCode);
        city.setDistrict(district);
        city.setPopulation(population);
        return city;
    }
}