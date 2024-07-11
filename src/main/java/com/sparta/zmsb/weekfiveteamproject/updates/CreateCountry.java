package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CreateCountry {

    public static CountryEntity createCountry(String code, String name,
                                              String continent, String region, BigDecimal surfaceArea,
                                              Short indepYear, Integer population, BigDecimal lifeExpectancy,
                                              BigDecimal gnp, BigDecimal gnpOld, String localName,
                                              String governmentForm, String headOfState, Integer capital,
                                              String code2){

        CountryEntity country = new CountryEntity();

        country.setCode(code);
        country.setName(name);
        country.setContinent(continent);
        country.setRegion(region);
        country.setSurfaceArea(surfaceArea);
        country.setIndepYear(indepYear);
        country.setPopulation(population);
        country.setLifeExpectancy(lifeExpectancy);
        country.setGnp(gnp);
        country.setGNPOld(gnpOld);
        country.setLocalName(localName);
        country.setGovernmentForm(governmentForm);
        country.setHeadOfState(headOfState);
        country.setCapital(capital);
        country.setCode2(code2);

        return country;
    }
}
