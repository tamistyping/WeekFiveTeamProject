package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CountryLanguageUpdates {

    @Autowired
    WorldService worldService;

    public CountrylanguageEntity createEntity(CountrylanguageEntityId id, String value, String flag) {
        List<CountryEntity> countries = worldService.allCountries();
        CountrylanguageEntity entity = worldService.allLanguages().stream().filter(c -> c.getId().equals(id)).findFirst().get();

        switch (flag.toLowerCase()) {
            case "is official":
                entity.setIsOfficial(value);
                break;
            case "percentage":
                entity.setPercentage(BigDecimal.valueOf(Double.parseDouble(value)));
                break;
            default:
                break;
        }
        return entity;
    }
}
