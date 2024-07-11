package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CountryRepository;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CountryUpdate {

    @Autowired
    private WorldService worldService;
    @Autowired
    private CountryRepository countryRepository;

    public <T> CountryEntity updateEntity(String countryCode, T updatedValue, String flag){
        CountryEntity countryEntity = worldService.getCountry(countryCode);
         switch (flag){
            case "Name" -> countryEntity.setName(updatedValue.toString());
            case "Code" -> countryEntity.setCode(updatedValue.toString());
            case "population" -> countryEntity.setPopulation(((Integer)updatedValue));
            case "Continent" -> countryEntity.setContinent(updatedValue.toString());
            case "Region" -> countryEntity.setRegion(updatedValue.toString());
            case "SurfaceArea" -> countryEntity.setSurfaceArea((BigDecimal) updatedValue);
            case "IndepYear" -> countryEntity.setIndepYear((Short) updatedValue);
            case "LifeExpectancy" -> countryEntity.setLifeExpectancy((BigDecimal) updatedValue);
            case "GNP" -> countryEntity.setGnp((BigDecimal) updatedValue);
            case "GNPOld" -> countryEntity.setGNPOld((BigDecimal) updatedValue);
            case "LocalName" -> countryEntity.setLocalName(updatedValue.toString());
            case "GovenrmentForm" -> countryEntity.setGovernmentForm(updatedValue.toString());
            case "HeadOfState" -> countryEntity.setHeadOfState(updatedValue.toString());
            case "Capital" -> countryEntity.setCapital((Integer) updatedValue);
            case "Code2" -> countryEntity.setCode2(updatedValue.toString());
            default -> throw new IllegalStateException("Unexpected value: " + flag);
        }
        return countryEntity;
    }

    public CountryEntity createCountry(String countryCode, )


}
