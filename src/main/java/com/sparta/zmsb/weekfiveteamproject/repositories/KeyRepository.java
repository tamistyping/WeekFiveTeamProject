package com.sparta.zmsb.weekfiveteamproject.repositories;

import com.sparta.zmsb.weekfiveteamproject.entities.KeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyRepository extends JpaRepository<KeyEntity, Integer> {
}
