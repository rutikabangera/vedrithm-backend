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
import java.util.Collections;
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

	/*
	 * public QuizResponse generateRecommendation(QuizRequest request) { String
	 * primaryConcern = request.getConcerns().isEmpty() ? "hair_fall" :
	 * request.getConcerns().get(0);
	 * 
	 * RecommendationRule rule = ruleRepository
	 * .findByConcernKeyAndIsActiveTrue(primaryConcern) .orElseGet(() ->
	 * ruleRepository .findByConcernKeyAndIsActiveTrue("default") .orElseThrow(() ->
	 * new RuntimeException( "No recommendation rule found for: " +
	 * primaryConcern)));
	 * 
	 * String recommendation = rule.getRecommendationTemplate() .replace("{name}",
	 * request.getName()) .replace("{hairType}", formatLabel(request.getHairType()))
	 * .replace("{scalpType}", formatLabel(request.getScalpType()))
	 * .replace("{lifestyle}", formatLabel(request.getLifestyle() != null ?
	 * request.getLifestyle() : ""));
	 * 
	 * List<String> keyIngredients =
	 * Arrays.stream(rule.getKeyIngredients().split(";")) .map(String::trim)
	 * .filter(s -> !s.isEmpty()) .collect(Collectors.toList());
	 * 
	 * 
	 * 
	 * 
	 * QuizResponse response = QuizResponse.builder() .productName(rule.getBaseOil()
	 * + " + " + rule.getBoosterName()) .tagline(rule.getTagline())
	 * .recommendation(recommendation) // .keyIngredients(keyIngredients)
	 * .keyIngredients(Collections.emptyList()) .usageTip(rule.getUsageTip())
	 * .build();
	 * 
	 * QuizSubmission submission = new QuizSubmission();
	 * submission.setName(request.getName());
	 * submission.setMobileNumber(request.getMobileNumber());
	 * submission.setHairType(request.getHairType());
	 * submission.setScalpType(request.getScalpType());
	 * submission.setConcerns(request.getConcerns());
	 * submission.setLifestyle(request.getLifestyle());
	 * submission.setRecommendedProduct(rule.getProductName());
	 * submissionRepository.save(submission);
	 * 
	 * return response; }
	 */
    
    public QuizResponse generateRecommendation(QuizRequest request) {

        // 1. Resolve concern
        String primaryConcern = (request.getConcerns() == null || request.getConcerns().isEmpty())
                ? "hair_fall"
                : request.getConcerns().get(0);

        // 2. Fetch rule
        RecommendationRule rule = ruleRepository
                .findByConcernKeyAndIsActiveTrue(primaryConcern)
                .orElseGet(() -> ruleRepository
                        .findByConcernKeyAndIsActiveTrue("default")
                        .orElseThrow(() -> new RuntimeException(
                                "No recommendation rule found for: " + primaryConcern)));

        // 3. Prepare safe values
        String name = safe(request.getName());
        String hairType = formatLabel(request.getHairType());
        String scalpType = formatLabel(request.getScalpType());
        String lifestyle = formatLabel(request.getLifestyle());
        String concernText = formatConcern(primaryConcern);

        String baseOil = safe(rule.getBaseOil());
        String booster = safe(rule.getBoosterName());

        // 4. Build recommendation text (ALL placeholders handled)
        String recommendation = rule.getRecommendationTemplate()
                .replace("{name}", name)
                .replace("{hairType}", hairType)
                .replace("{scalpType}", scalpType)
                .replace("{lifestyle}", lifestyle)
                .replace("{concern}", concernText)
                .replace("{baseOil}", baseOil)
                .replace("{booster}", booster);

        // 5. Build product name (dynamic system)
        String productName = baseOil + " + " + booster;

        // 6. Build response (clean UI → no ingredients)
        QuizResponse response = QuizResponse.builder()
                .productName(productName)
                .tagline(rule.getTagline())
                .recommendation(recommendation)
                .keyIngredients(Collections.emptyList())
                .usageTip(rule.getUsageTip())
                .build();

        // 7. Save submission (updated to new system)
        QuizSubmission submission = new QuizSubmission();
        submission.setName(name);
        submission.setMobileNumber(request.getMobileNumber());
        submission.setHairType(request.getHairType());
        submission.setScalpType(request.getScalpType());
        submission.setConcerns(request.getConcerns());
        submission.setLifestyle(request.getLifestyle());
        submission.setRecommendedProduct(productName);
        submissionRepository.save(submission);

        return response;
    }
    
    private String formatLabel(String value) {
        if (value == null || value.isEmpty()) return "";
        return Arrays.stream(value.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
    private String safe(String value) {
        return value == null ? "" : value;
    }
    public List<LeadDto> getAllLeads() {
        return submissionRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getSubmittedAt().compareTo(a.getSubmittedAt()))
                .map(this::toLeadDto)
                .collect(Collectors.toList());
    }
    
    private String formatConcern(String concern) {
        return switch (concern) {
            case "hair_fall" -> "excessive hair fall";
            case "dandruff" -> "scalp flaking and dandruff";
            case "frizz" -> "dryness and frizz";
            case "greying" -> "premature greying";
            case "dull" -> "dull and lifeless hair";
            case "slow_growth" -> "slower-than-normal hair growth";
            case "healthy_hair" -> "overall hair nourishment";
            default -> formatLabel(concern);
        };
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

	/*
	 * private String formatLabel(String value) { if (value == null ||
	 * value.isBlank()) return ""; return value.replace("_", " "); }
	 */

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
