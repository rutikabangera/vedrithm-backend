package com.vedrithm.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LeadDto {
    private Long id;
    private String name;
    private String mobileNumber;
    private String hairType;
    private String scalpType;
    private String lifestyle;
    private List<String> concerns;
    private String recommendedProduct;
    private LocalDateTime submittedAt;
}
