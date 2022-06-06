package com.example.alfabank.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * interface for getting JsonNode object with currency rates of yesterday from API https://openexchangerates.org
 */
@FeignClient(value = "ratesYesterday", url = "${rates.url.yesterday}")
public interface ExchangeRatesOfYesterday {

    @GetMapping()
    JsonNode getRates();
}
