package com.sparta.zmsb.weekfiveteamproject.repositories;

import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryLanguageRepository extends JpaRepository<CountrylanguageEntity, Integer> {
}
