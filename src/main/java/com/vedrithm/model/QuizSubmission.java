package com.vedrithm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "quiz_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @Column(name = "hair_type")
    private String hairType;

    @Column(name = "scalp_type")
    private String scalpType;

    @Column(name = "lifestyle")
    private String lifestyle;

    @ElementCollection
    @CollectionTable(name = "quiz_concerns", joinColumns = @JoinColumn(name = "submission_id"))
    @Column(name = "concern")
    private List<String> concerns;

    @Column(name = "recommended_product", length = 500)
    private String recommendedProduct;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
}
