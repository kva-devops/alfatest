package com.example.alfabank.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

/**
 * DTO-class for sending a response from the api service https://openexchangerates.org
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatesResponseDTO {
    private String disclaimer;
    private String license;
    private long timestamp;
    private String base;
    private HashMap<String, Float> rates;

}
