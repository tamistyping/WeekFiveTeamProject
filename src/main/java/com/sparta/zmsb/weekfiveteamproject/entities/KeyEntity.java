package com.sparta.zmsb.weekfiveteamproject.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "ApiKeys", schema = "world")
public class KeyEntity {
    @Id
    @Size(min = 36, max = 36)
    @Column(name = "ApiKey", nullable = false, length = 36)
    private String apiKey;

    public @Size(min = 36, max = 36) String getApiKey() {
        return apiKey;
    }

    public void setApiKey(@Size(min = 36, max = 36) String apiKey) {
        this.apiKey = apiKey;
    }
}
