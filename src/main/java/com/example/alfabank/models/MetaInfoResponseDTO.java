package com.example.alfabank.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-class for passing meta information from the response from the api service 'https://giphy.com'
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetaInfoResponseDTO {
    private String msg;
    private int status;
    private String response_id;
}
