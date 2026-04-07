package com.vedrithm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "recommendation_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Primary key for matching: concern value from quiz
    @Column(name = "concern_key", nullable = false, unique = true, length = 100)
    private String concernKey;           // e.g. "hair_fall", "dandruff", "greying"

    @Column(name = "product_name", nullable = false, length = 300)
    private String productName;

    @Column(name = "tagline", nullable = false, length = 300)
    private String tagline;

    // {name}, {hairType}, {scalpType} are interpolated at runtime
    @Column(name = "recommendation_template", nullable = false, length = 3000)
    private String recommendationTemplate;

    // Semicolon-separated list of key ingredients to highlight
    @Column(name = "key_ingredients", nullable = false, length = 1000)
    private String keyIngredients;

    @Column(name = "usage_tip", nullable = false, length = 1000)
    private String usageTip;

    @Column(name = "is_active")
    private Boolean isActive = true;
    
    
    @Column(name = "base_oil", nullable = false, length = 1000)
    private String baseOil;
    
    @Column(name = "booster_name", nullable = false, length = 1000)
    private String boosterName;
}
