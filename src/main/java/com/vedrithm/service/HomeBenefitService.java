package com.vedrithm.service;

import com.vedrithm.dto.HomeBenefitDto;
import com.vedrithm.model.HomeBenefit;
import com.vedrithm.repository.HomeBenefitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeBenefitService {

    private final HomeBenefitRepository repository;

    public HomeBenefitService(HomeBenefitRepository repository) {
        this.repository = repository;
    }

    public List<HomeBenefitDto> getAllActive() {
        return repository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<HomeBenefitDto> getAll() {
        return repository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public HomeBenefitDto create(HomeBenefit benefit) {
        return toDto(repository.save(benefit));
    }

    public HomeBenefitDto update(Long id, HomeBenefit updated) {
        HomeBenefit existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefit not found: " + id));
        existing.setIcon(updated.getIcon());
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setDisplayOrder(updated.getDisplayOrder());
        existing.setIsActive(updated.getIsActive());
        return toDto(repository.save(existing));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private HomeBenefitDto toDto(HomeBenefit b) {
        return HomeBenefitDto.builder()
                .id(b.getId())
                .icon(b.getIcon())
                .title(b.getTitle())
                .description(b.getDescription())
                .displayOrder(b.getDisplayOrder())
                .build();
    }
}
