package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.KeyEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.KeyRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KeyService {

    private final KeyRepository keyRepository;
    private final EntityManager entityManager;

    @Autowired
    public KeyService(KeyRepository keyRepository, EntityManager entityManager) {
        this.entityManager = entityManager;
        this.keyRepository = keyRepository;

    }

    public KeyEntity createApiKey() {
        KeyEntity key = new KeyEntity();
        key.setApiKey(generateApiKey());
        keyRepository.save(key);
        return key;
    }

    public boolean isValidApiKey(String apiKey) {
        List<KeyEntity> keys = keyRepository.findAll();
        for (KeyEntity key : keys) {
            if (key.getApiKey().equals(apiKey)) {
                return true;
            }
        }
        return false;
    }

    public List<KeyEntity> getAllKeys() {
        return keyRepository.findAll();
    }

    private String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
