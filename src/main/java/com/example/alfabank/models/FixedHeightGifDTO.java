package com.example.alfabank.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO-class for passing a GIF object and extracting the desired fields
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FixedHeightGifDTO {
    private String url;
    private String width;
    private String height;
    private String size;
    private String mp4;
    private String mp4_size;
    private String webp;
    private String webp_size;

}
