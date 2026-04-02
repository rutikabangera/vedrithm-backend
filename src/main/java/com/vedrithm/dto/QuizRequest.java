package com.vedrithm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class QuizRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter a valid 10-digit Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Hair type is required")
    private String hairType;

    @NotBlank(message = "Scalp type is required")
    private String scalpType;

    @NotEmpty(message = "At least one concern is required")
    private List<String> concerns;

    private String lifestyle;
}
