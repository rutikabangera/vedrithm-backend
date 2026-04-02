package com.vedrithm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizResponse {
    private String productName;
    private String tagline;
    private String recommendation;
    private List<String> keyIngredients;
    private String usageTip;
}
