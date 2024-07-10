package com.sparta.zmsb.weekfiveteamproject.repositories;

import com.sparta.zmsb.weekfiveteamproject.entities.CountrylanguageEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryLanguageIDRepository extends JpaRepository<CountrylanguageEntityId, Integer> {
}
