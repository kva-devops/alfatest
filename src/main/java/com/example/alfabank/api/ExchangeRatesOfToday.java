package com.example.alfabank.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * interface for getting JsonNode object with currency rates of today from API https://openexchangerates.org
 */
@FeignClient(value = "ratesToday", url = "${rates.url.today}")
public interface ExchangeRatesOfToday {
    @GetMapping()
    JsonNode getRates();
}
