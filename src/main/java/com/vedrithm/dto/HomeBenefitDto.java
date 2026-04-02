package com.vedrithm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeBenefitDto {
    private Long id;
    private String icon;
    private String title;
    private String description;
    private Integer displayOrder;
}
