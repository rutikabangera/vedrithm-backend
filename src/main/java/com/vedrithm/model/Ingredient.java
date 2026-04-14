package com.vedrithm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "sanskrit_name")
    private String sanskritName;

    /**
     * Legacy emoji field — kept for backward compatibility but no longer
     * rendered on the frontend. The revamped UI uses inline SVG illustrations
     * keyed by {@link #imageSlug}.
     */
    @Column(nullable = false, length = 10)
    private String emoji;

    /**
     * Slug that maps to a built-in SVG illustration in the Angular frontend.
     * Examples: "coconut-oil", "hibiscus", "bhringraj".
     * Falls back to LOWER(REPLACE(name, ' ', '-')) if not explicitly set.
     */
    @Column(name = "image_slug", length = 100)
    private String imageSlug;

    @Column(nullable = false, length = 100)
    private String tag;                  // e.g. "Base Oil", "Growth Stimulant"

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(name = "origin_place")
    private String originPlace;          // e.g. "Kerala, South India"

    @Column(name = "display_order")
    private Integer displayOrder;        // Controls ordering on frontend

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Stored as semicolon-separated string for simplicity
    @Column(name = "benefits", length = 1000)
    private String benefits;            // e.g. "Prevents protein loss;Deep penetration;Anti-microbial"

    // Comma-separated concern keys this ingredient targets
    // e.g. "hair_fall,slow_growth"
    @Column(name = "concern_tags", length = 500)
    private String concernTags;
}
