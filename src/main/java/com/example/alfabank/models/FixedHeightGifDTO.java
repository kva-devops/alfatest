package com.example.alfabank.models;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO-class for passing a GIF object and extracting the desired fields
 */
@Getter
@Setter
public class FixedHeightGifDTO {
    private String url;
    private String width;
    private String height;
    private String size;
    private String mp4;
    private String mp4_size;
    private String webp;
    private String webp_size;

    public FixedHeightGifDTO() {
    }

    public FixedHeightGifDTO(String url, String width, String height, String size, String mp4, String mp4_size, String webp, String webp_size) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.size = size;
        this.mp4 = mp4;
        this.mp4_size = mp4_size;
        this.webp = webp;
        this.webp_size = webp_size;
    }
}
