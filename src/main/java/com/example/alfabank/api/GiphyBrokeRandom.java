package com.example.alfabank.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * interface for getting JsonNode-object containing random GIF image with tag: 'broke' from API https://giphy.com
 */
@FeignClient(value = "giphyBrokeRandom", url = "${giphy.url.broke}")
public interface GiphyBrokeRandom {
    @GetMapping()
    JsonNode getGiphyObject();
}
