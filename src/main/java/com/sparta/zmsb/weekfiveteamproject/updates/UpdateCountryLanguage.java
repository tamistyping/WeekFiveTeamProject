package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UpdateCountryLanguage {

    @Autowired
    WorldService worldService;

    public static CountrylanguageEntity createEntity(CountrylanguageEntity entity, String value, String flag) {
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
