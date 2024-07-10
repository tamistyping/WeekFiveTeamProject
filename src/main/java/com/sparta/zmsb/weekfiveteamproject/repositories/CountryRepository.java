package com.sparta.zmsb.weekfiveteamproject.repositories;

import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
}
