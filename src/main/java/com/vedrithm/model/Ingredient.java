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

    @Column(nullable = false, length = 10)
    private String emoji;

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
