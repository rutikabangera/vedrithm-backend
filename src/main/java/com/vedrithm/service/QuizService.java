package com.vedrithm.service;

import com.vedrithm.dto.QuizRequest;
import com.vedrithm.dto.QuizResponse;
import com.vedrithm.dto.LeadDto;
import com.vedrithm.model.QuizSubmission;
import com.vedrithm.model.RecommendationRule;
import com.vedrithm.repository.QuizSubmissionRepository;
import com.vedrithm.repository.RecommendationRuleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizSubmissionRepository submissionRepository;
    private final RecommendationRuleRepository ruleRepository;

    public QuizService(QuizSubmissionRepository submissionRepository,
                       RecommendationRuleRepository ruleRepository) {
        this.submissionRepository = submissionRepository;
        this.ruleRepository = ruleRepository;
    }

    public QuizResponse generateRecommendation(QuizRequest request) {
        String primaryConcern = request.getConcerns().isEmpty()
                ? "hair_fall"
                : request.getConcerns().get(0);

        RecommendationRule rule = ruleRepository
                .findByConcernKeyAndIsActiveTrue(primaryConcern)
                .orElseGet(() -> ruleRepository
                        .findByConcernKeyAndIsActiveTrue("default")
                        .orElseThrow(() -> new RuntimeException(
                                "No recommendation rule found for: " + primaryConcern)));

        String recommendation = rule.getRecommendationTemplate()
                .replace("{name}", request.getName())
                .replace("{hairType}", formatLabel(request.getHairType()))
                .replace("{scalpType}", formatLabel(request.getScalpType()))
                .replace("{lifestyle}", formatLabel(request.getLifestyle() != null ? request.getLifestyle() : ""));

        List<String> keyIngredients = Arrays.stream(rule.getKeyIngredients().split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        QuizResponse response = QuizResponse.builder()
                .productName(rule.getProductName())
                .tagline(rule.getTagline())
                .recommendation(recommendation)
                .keyIngredients(keyIngredients)
                .usageTip(rule.getUsageTip())
                .build();

        QuizSubmission submission = new QuizSubmission();
        submission.setName(request.getName());
        submission.setMobileNumber(request.getMobileNumber());
        submission.setHairType(request.getHairType());
        submission.setScalpType(request.getScalpType());
        submission.setConcerns(request.getConcerns());
        submission.setLifestyle(request.getLifestyle());
        submission.setRecommendedProduct(rule.getProductName());
        submissionRepository.save(submission);

        return response;
    }

    public List<LeadDto> getAllLeads() {
        return submissionRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .map(this::toLeadDto)
                .collect(Collectors.toList());
    }

    public List<LeadDto> getRecentLeads(int limit) {
        return submissionRepository.findTop20ByOrderBySubmittedAtDesc()
                .stream()
                .limit(limit)
                .map(this::toLeadDto)
                .collect(Collectors.toList());
    }

    public List<RecommendationRule> getAllRules() {
        return ruleRepository.findByIsActiveTrueOrderByConcernKeyAsc();
    }

    public RecommendationRule createRule(RecommendationRule rule) {
        return ruleRepository.save(rule);
    }

    public RecommendationRule updateRule(Long id, RecommendationRule updated) {
        RecommendationRule existing = ruleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found: " + id));
        existing.setConcernKey(updated.getConcernKey());
        existing.setProductName(updated.getProductName());
        existing.setTagline(updated.getTagline());
        existing.setRecommendationTemplate(updated.getRecommendationTemplate());
        existing.setKeyIngredients(updated.getKeyIngredients());
        existing.setUsageTip(updated.getUsageTip());
        existing.setIsActive(updated.getIsActive());
        return ruleRepository.save(existing);
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }

    private String formatLabel(String value) {
        if (value == null || value.isBlank()) return "";
        return value.replace("_", " ");
    }

    private LeadDto toLeadDto(QuizSubmission s) {
        return LeadDto.builder()
                .id(s.getId())
                .name(s.getName())
                .mobileNumber(s.getMobileNumber())
                .hairType(s.getHairType())
                .scalpType(s.getScalpType())
                .lifestyle(s.getLifestyle())
                .concerns(s.getConcerns())
                .recommendedProduct(s.getRecommendedProduct())
                .submittedAt(s.getSubmittedAt())
                .build();
    }
}
