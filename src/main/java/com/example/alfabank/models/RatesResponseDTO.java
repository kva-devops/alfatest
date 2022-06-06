package com.example.alfabank.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * DTO-class for sending a response from the api service https://openexchangerates.org
 */
@Getter
@Setter
public class RatesResponseDTO {
    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private HashMap<String, Float> rates;

    public RatesResponseDTO() {
    }

    public RatesResponseDTO(String disclaimer, String license, long timestamp, String base, HashMap<String, Float> rates) {
        this.disclaimer = disclaimer;
        this.license = license;
        this.timestamp = timestamp;
        this.base = base;
        this.rates = rates;
    }
}
