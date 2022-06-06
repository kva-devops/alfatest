package com.example.alfabank.models;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO-class for passing meta information from the response from the api service 'https://giphy.com'
 */
@Getter
@Setter
public class MetaInfoResponseDTO {
    private String msg;
    private int status;
    private String response_id;

    public MetaInfoResponseDTO() {
    }

    public MetaInfoResponseDTO(String msg, int status, String response_id) {
        this.msg = msg;
        this.status = status;
        this.response_id = response_id;
    }
}
