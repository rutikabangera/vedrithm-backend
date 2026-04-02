package com.vedrithm.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class IngredientDto {
    private Long id;
    private String name;
    private String sanskritName;
    private String emoji;
    private String tag;
    private String description;
    private String originPlace;
    private Integer displayOrder;
    private List<String> benefits;      // parsed from semicolon-separated string
}
