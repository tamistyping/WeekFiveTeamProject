package com.sparta.zmsb.weekfiveteamproject.service;

import com.sparta.zmsb.weekfiveteamproject.entities.KeyEntity;
import com.sparta.zmsb.weekfiveteamproject.repositories.KeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KeyService {

    private final KeyRepository keyRepository;


    @Autowired
    public KeyService(KeyRepository keyRepository) {

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
