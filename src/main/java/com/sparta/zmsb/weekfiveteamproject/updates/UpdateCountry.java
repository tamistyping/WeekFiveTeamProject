package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

@Component
public class UpdateCountry {
    public static <T> CountryEntity updateCountry(T updatedValue, String flag, CountryEntity countryEntity) {
        switch (flag) {
            case "Name" -> countryEntity.setName(updatedValue.toString());
            case "Population" -> countryEntity.setPopulation(((Integer) updatedValue));
            case "Continent" -> countryEntity.setContinent(updatedValue.toString());
            case "Region" -> countryEntity.setRegion(updatedValue.toString());
            case "SurfaceArea" -> countryEntity.setSurfaceArea((BigDecimal) updatedValue);
            case "IndepYear" -> countryEntity.setIndepYear((Short) updatedValue);
            case "LifeExpectancy" -> countryEntity.setLifeExpectancy((BigDecimal) updatedValue);
            case "GNP" -> countryEntity.setGnp((BigDecimal) updatedValue);
            case "GNPOld" -> countryEntity.setGNPOld((BigDecimal) updatedValue);
            case "LocalName" -> countryEntity.setLocalName(updatedValue.toString());
            case "GovernmentForm" -> countryEntity.setGovernmentForm(updatedValue.toString());
            case "HeadOfState" -> countryEntity.setHeadOfState(updatedValue.toString());
            case "Capital" -> countryEntity.setCapital((Integer) updatedValue);
            case "Code2" -> countryEntity.setCode2(updatedValue.toString());
            default -> throw new InvalidParameterException("Unexpected value: " + flag);
        }
        return countryEntity;
    }
}
