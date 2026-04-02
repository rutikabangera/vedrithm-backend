package com.vedrithm.controller;

import com.vedrithm.dto.IngredientDto;
import com.vedrithm.model.Ingredient;
import com.vedrithm.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /** GET /api/ingredients — all active ingredients (used by frontend) */
    @GetMapping
    public ResponseEntity<List<IngredientDto>> getActive() {
        return ResponseEntity.ok(ingredientService.getAllActive());
    }

    /** GET /api/ingredients/all — including inactive (admin use) */
    @GetMapping("/all")
    public ResponseEntity<List<IngredientDto>> getAll() {
        return ResponseEntity.ok(ingredientService.getAll());
    }

    /** GET /api/ingredients/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.getById(id));
    }

    /** POST /api/ingredients — add new ingredient */
    @PostMapping
    public ResponseEntity<IngredientDto> create(@RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.create(ingredient));
    }

    /** PUT /api/ingredients/{id} — update ingredient */
    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> update(
            @PathVariable Long id, @RequestBody Ingredient ingredient) {
        return ResponseEntity.ok(ingredientService.update(id, ingredient));
    }

    /** DELETE /api/ingredients/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ingredientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/ingredients/{id}/toggle — activate/deactivate */
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<IngredientDto> toggle(@PathVariable Long id) {
        return ResponseEntity.ok(ingredientService.toggleActive(id));
    }
}
