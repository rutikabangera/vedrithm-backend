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

    /** @deprecated Use {@link #imageSlug} instead — kept for old clients only. */
    @Deprecated
    private String emoji;

    /**
     * Slug that maps to the inline SVG illustration in the Angular frontend.
     * Examples: "coconut-oil", "hibiscus", "bhringraj".
     */
    private String imageSlug;

    private String tag;
    private String description;
    private String originPlace;
    private Integer displayOrder;
    private List<String> benefits;      // parsed from semicolon-separated string
}
