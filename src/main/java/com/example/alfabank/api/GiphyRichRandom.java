package com.example.alfabank.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * interface for getting JsonNode-object containing an GIF image with tag: 'rich' from API https://giphy.com
 */
@FeignClient(value = "giphyRichRandom", url = "${giphy.url.rich}")
public interface GiphyRichRandom {
    @GetMapping()
    JsonNode getGiphyObject();
}
