package com.vedrithm.controller;

import com.vedrithm.dto.SiteConfigDto;
import com.vedrithm.model.SiteConfig;
import com.vedrithm.service.SiteConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class SiteConfigController {

    private final SiteConfigService service;

    public SiteConfigController(SiteConfigService service) {
        this.service = service;
    }

    /**
     * GET /api/config/public
     * Used by the Angular frontend on load to get WhatsApp number,
     * hero text, brand story etc — all from DB, no code change needed.
     */
    @GetMapping("/public")
    public ResponseEntity<SiteConfigDto> getPublicConfig() {
        return ResponseEntity.ok(service.getPublicConfig());
    }

    /**
     * PUT /api/config/{key}
     * Update a single config key.
     * e.g. PUT /api/config/whatsapp_number  body: { "value": "919876543210" }
     */
    @PutMapping("/{key}")
    public ResponseEntity<SiteConfig> updateOne(
            @PathVariable String key,
            @RequestBody Map<String, String> body) {
        String value = body.get("value");
        if (value == null || value.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.update(key, value));
    }

    /**
     * PUT /api/config/batch
     * Update multiple config values in one call.
     * Body: { "whatsapp_number": "919876543210", "brand_tagline": "My new tagline" }
     */
    @PutMapping("/batch")
    public ResponseEntity<Void> updateBatch(@RequestBody Map<String, String> updates) {
        service.updateBatch(updates);
        return ResponseEntity.ok().build();
    }
}
