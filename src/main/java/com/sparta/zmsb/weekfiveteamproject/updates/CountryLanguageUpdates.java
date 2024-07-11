package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import com.sparta.zmsb.weekfiveteamproject.service.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryLanguageUpdates {

    @Autowired
    WorldService worldService;

    public CountrylanguageEntity createEntity(Integer id, String value, String flag) {

        return new CountrylanguageEntity();
    }
}
