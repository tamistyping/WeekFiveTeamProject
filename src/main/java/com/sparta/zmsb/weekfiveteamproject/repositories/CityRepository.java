package com.sparta.zmsb.weekfiveteamproject.repositories;

import com.sparta.zmsb.weekfiveteamproject.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {
}
