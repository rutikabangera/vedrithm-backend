package com.vedrithm.controller;

import com.vedrithm.dto.LeadDto;
import com.vedrithm.dto.QuizRequest;
import com.vedrithm.dto.QuizResponse;
import com.vedrithm.model.RecommendationRule;
import com.vedrithm.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /** POST /api/quiz/recommend — submit quiz, get recommendation, save lead */
    @PostMapping("/recommend")
    public ResponseEntity<QuizResponse> recommend(@Valid @RequestBody QuizRequest request) {
        return ResponseEntity.ok(quizService.generateRecommendation(request));
    }

    /** GET /api/quiz/health */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Vedrithm API is running 🌿");
    }

    // ── Admin endpoints ──────────────────────────────────────────────────────

    /** GET /api/quiz/leads — all leads with name + mobile */
    @GetMapping("/leads")
    public ResponseEntity<List<LeadDto>> leads() {
        return ResponseEntity.ok(quizService.getAllLeads());
    }

    /** GET /api/quiz/leads/recent?limit=10 */
    @GetMapping("/leads/recent")
    public ResponseEntity<List<LeadDto>> recentLeads(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(quizService.getRecentLeads(limit));
    }

    /** GET /api/quiz/rules — all recommendation rules */
    @GetMapping("/rules")
    public ResponseEntity<List<RecommendationRule>> getRules() {
        return ResponseEntity.ok(quizService.getAllRules());
    }

    /** POST /api/quiz/rules — add new recommendation rule */
    @PostMapping("/rules")
    public ResponseEntity<RecommendationRule> createRule(
            @RequestBody RecommendationRule rule) {
        return ResponseEntity.ok(quizService.createRule(rule));
    }

    /** PUT /api/quiz/rules/{id} — update recommendation rule */
    @PutMapping("/rules/{id}")
    public ResponseEntity<RecommendationRule> updateRule(
            @PathVariable Long id, @RequestBody RecommendationRule rule) {
        return ResponseEntity.ok(quizService.updateRule(id, rule));
    }

    /** DELETE /api/quiz/rules/{id} */
    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        quizService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}
