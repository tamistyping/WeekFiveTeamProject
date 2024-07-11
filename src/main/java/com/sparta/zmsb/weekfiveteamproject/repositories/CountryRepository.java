package com.sparta.zmsb.weekfiveteamproject.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.sparta.zmsb.weekfiveteamproject.entities.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
}
