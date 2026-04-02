package com.vedrithm.service;

import com.vedrithm.dto.SiteConfigDto;
import com.vedrithm.model.SiteConfig;
import com.vedrithm.repository.SiteConfigRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SiteConfigService {

    private final SiteConfigRepository repository;

    public SiteConfigService(SiteConfigRepository repository) {
        this.repository = repository;
    }

    public String get(String key, String defaultValue) {
        return repository.findById(key)
                .map(SiteConfig::getConfigValue)
                .orElse(defaultValue);
    }

    public SiteConfigDto getPublicConfig() {
        Map<String, String> all = repository.findAll()
                .stream()
                .collect(Collectors.toMap(SiteConfig::getConfigKey, SiteConfig::getConfigValue));

        return SiteConfigDto.builder()
                .whatsappNumber(all.getOrDefault("whatsapp_number", "919999999999"))
                .brandTagline(all.getOrDefault("brand_tagline", "Ancient Ayurvedic wisdom for modern hair care"))
                .heroTitle(all.getOrDefault("hero_title", "Rooted in Nature. Nourished by Vedas."))
                .heroSubtitle(all.getOrDefault("hero_subtitle",
                        "A sacred blend of 8 time-honoured Ayurvedic herbs, cold-pressed to restore your hair's natural vitality."))
                .brandStory(all.getOrDefault("brand_story",
                        "Vedrithm was born from a deep reverence for India's ancient Ayurvedic heritage."))
                .instagramUrl(all.getOrDefault("instagram_url", "https://instagram.com/vedrithm"))
                .build();
    }

    /** Admin: update a config value */
    public SiteConfig update(String key, String value) {
        SiteConfig config = repository.findById(key)
                .orElse(new SiteConfig(key, value, ""));
        config.setConfigValue(value);
        return repository.save(config);
    }

    /** Admin: update multiple config values */
    public void updateBatch(Map<String, String> updates) {
        updates.forEach((key, value) -> {
            SiteConfig config = repository.findById(key)
                    .orElse(new SiteConfig(key, value, ""));
            config.setConfigValue(value);
            repository.save(config);
        });
    }
}
