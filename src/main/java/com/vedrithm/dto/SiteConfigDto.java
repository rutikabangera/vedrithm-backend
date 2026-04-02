package com.vedrithm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SiteConfigDto {
    private String whatsappNumber;
    private String brandTagline;
    private String heroTitle;
    private String heroSubtitle;
    private String brandStory;
    private String instagramUrl;
}
