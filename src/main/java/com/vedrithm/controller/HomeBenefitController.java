package com.vedrithm.controller;

import com.vedrithm.dto.HomeBenefitDto;
import com.vedrithm.model.HomeBenefit;
import com.vedrithm.service.HomeBenefitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class HomeBenefitController {

    private final HomeBenefitService service;

    public HomeBenefitController(HomeBenefitService service) {
        this.service = service;
    }

    /** GET /api/benefits — active benefits for home page */
    @GetMapping
    public ResponseEntity<List<HomeBenefitDto>> getActive() {
        return ResponseEntity.ok(service.getAllActive());
    }

    /** GET /api/benefits/all — all including inactive (admin) */
    @GetMapping("/all")
    public ResponseEntity<List<HomeBenefitDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /** POST /api/benefits */
    @PostMapping
    public ResponseEntity<HomeBenefitDto> create(@RequestBody HomeBenefit benefit) {
        return ResponseEntity.ok(service.create(benefit));
    }

    /** PUT /api/benefits/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<HomeBenefitDto> update(
            @PathVariable Long id, @RequestBody HomeBenefit benefit) {
        return ResponseEntity.ok(service.update(id, benefit));
    }

    /** DELETE /api/benefits/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
