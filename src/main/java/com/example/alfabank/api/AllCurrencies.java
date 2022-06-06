package com.example.alfabank.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

/**
 * interface for getting JsonNode object all currencies from API https://openexchangerates.org
 */
@FeignClient(value = "allCurrencies", url = "${rates.url.currencies}")
public interface AllCurrencies {
    @GetMapping()
    HashMap<String, String> getAllCurrencies();
}
