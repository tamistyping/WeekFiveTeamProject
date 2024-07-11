package com.sparta.zmsb.weekfiveteamproject.updates;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.CityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CityUpdate {

    @Autowired
    private CityRepository cityRepository;


    @Transactional
    public CityEntity updateCity(Integer id, String name, String district, Integer population) {
        Optional<CityEntity> cityOptional = cityRepository.findById(id);

        if (cityOptional.isPresent()) {
            CityEntity city = cityOptional.get();
            if (name != null) city.setName(name);
            if (district != null) city.setDistrict(district);
            if (population != null) city.setPopulation(population);
            return cityRepository.save(city);
        } else {
            throw new RuntimeException("City with id " + id + " not found");
        }
    }
}
