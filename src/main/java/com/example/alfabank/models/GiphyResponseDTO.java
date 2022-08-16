package com.example.alfabank.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

/**
 * DTO-class for passing the response from the api service 'https://giphy.com'
 */
@Getter
@Setter
@NoArgsConstructor
public class GiphyResponseDTO {
    private HashMap<String, Object> data;
    private MetaInfoResponseDTO meta;
}
