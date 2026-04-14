package com.vedrithm.service;

import com.vedrithm.dto.IngredientDto;
import com.vedrithm.model.Ingredient;
import com.vedrithm.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public List<IngredientDto> getAllActive() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<IngredientDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public IngredientDto getById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
    }

    public IngredientDto create(Ingredient ingredient) {
        return toDto(repository.save(ingredient));
    }

    public IngredientDto update(Long id, Ingredient updated) {
        Ingredient existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
        existing.setName(updated.getName());
        existing.setSanskritName(updated.getSanskritName());
        existing.setEmoji(updated.getEmoji());
        existing.setImageSlug(updated.getImageSlug());   // ← new field
        existing.setTag(updated.getTag());
        existing.setDescription(updated.getDescription());
        existing.setOriginPlace(updated.getOriginPlace());
        existing.setDisplayOrder(updated.getDisplayOrder());
        existing.setIsActive(updated.getIsActive());
        existing.setBenefits(updated.getBenefits());
        existing.setConcernTags(updated.getConcernTags());
        return toDto(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public IngredientDto toggleActive(Long id) {
        Ingredient ing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found: " + id));
        ing.setIsActive(!ing.getIsActive());
        return toDto(repository.save(ing));
    }

    private IngredientDto toDto(Ingredient ing) {
        List<String> benefitList = (ing.getBenefits() != null && !ing.getBenefits().isBlank())
                ? Arrays.asList(ing.getBenefits().split(";"))
                : List.of();

        // Derive imageSlug: use stored value, or auto-generate from name
        String slug = (ing.getImageSlug() != null && !ing.getImageSlug().isBlank())
                ? ing.getImageSlug()
                : ing.getName().toLowerCase().replace(" ", "-");

        return IngredientDto.builder()
                .id(ing.getId())
                .name(ing.getName())
                .sanskritName(ing.getSanskritName())
                .emoji(ing.getEmoji())          // kept for backward compat
                .imageSlug(slug)                // ← new field exposed to frontend
                .tag(ing.getTag())
                .description(ing.getDescription())
                .originPlace(ing.getOriginPlace())
                .displayOrder(ing.getDisplayOrder())
                .benefits(benefitList)
                .build();
    }
}
