-- ============================================================
-- Vedrithm Database - Full Setup + Seed Data
-- Run once: mysql -u root -p < init.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS vedrithm_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE vedrithm_db;

-- ─── Tables (JPA auto-creates, but explicit here for clarity) ──────────────

CREATE TABLE IF NOT EXISTS site_config (
    config_key   VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(2000) NOT NULL,
    description  VARCHAR(300)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS ingredients (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    sanskrit_name  VARCHAR(255),
    emoji          VARCHAR(10) NOT NULL,
    tag            VARCHAR(100) NOT NULL,
    description    TEXT NOT NULL,
    origin_place   VARCHAR(200),
    display_order  INT DEFAULT 0,
    is_active      BOOLEAN DEFAULT TRUE,
    benefits       TEXT,
    concern_tags   VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS home_benefits (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    icon          VARCHAR(10) NOT NULL,
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(500) NOT NULL,
    display_order INT DEFAULT 0,
    is_active     BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS recommendation_rules (
    id                        BIGINT AUTO_INCREMENT PRIMARY KEY,
    concern_key               VARCHAR(100) NOT NULL UNIQUE,
    product_name              VARCHAR(300) NOT NULL,
    tagline                   VARCHAR(300) NOT NULL,
    recommendation_template   TEXT NOT NULL,
    key_ingredients           TEXT NOT NULL,
    usage_tip                 TEXT NOT NULL,
    is_active                 BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS quiz_submissions (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(150) NOT NULL,
    mobile_number       VARCHAR(20) NOT NULL,
    hair_type           VARCHAR(100),
    scalp_type          VARCHAR(100),
    lifestyle           VARCHAR(100),
    recommended_product VARCHAR(500),
    submitted_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_submitted_at (submitted_at),
    INDEX idx_mobile (mobile_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS quiz_concerns (
    submission_id BIGINT NOT NULL,
    concern       VARCHAR(100),
    FOREIGN KEY (submission_id) REFERENCES quiz_submissions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ─── SITE CONFIG SEED ──────────────────────────────────────────────────────
-- To change WhatsApp number: UPDATE site_config SET config_value='91XXXXXXXXXX' WHERE config_key='whatsapp_number';

INSERT INTO site_config (config_key, config_value, description) VALUES
('whatsapp_number',  '919999999999',                                          'Owner WhatsApp number — country code + number, no + or spaces'),
('brand_tagline',    'Ancient Ayurvedic wisdom for modern hair care',          'Short tagline shown in footer and meta'),
('hero_title',       'Rooted in Nature. Nourished by Vedas.',                 'Main hero heading on home page'),
('hero_subtitle',    'A sacred blend of 8 time-honoured Ayurvedic herbs, cold-pressed to restore your hair''s natural vitality from root to tip.', 'Hero subheading'),
('brand_story',      'Vedrithm was born from a deep reverence for India''s ancient Ayurvedic heritage. The name — a blend of Veda (knowledge) and Rhythm — embodies our belief that nature has its own perfect cadence, one that nurtures life in balance. Our herbal hair oil is crafted using traditional cold-press methods, preserving the full potency of each ingredient. No harsh chemicals. No synthetic fragrances. Just pure, intentional herbal formulation — the way nature intended.', 'Brand story paragraph on home page'),
('instagram_url',    'https://instagram.com/vedrithm',                        'Instagram profile link'),
('product_volume',   '100 ml',                                                'Product volume shown on home page badge'),
('owner_name',       'Vedrithm Team',                                         'Owner name used in WhatsApp messages')
ON DUPLICATE KEY UPDATE config_value = VALUES(config_value);

-- ─── INGREDIENTS SEED ──────────────────────────────────────────────────────

TRUNCATE TABLE ingredients;

INSERT INTO ingredients (name, sanskrit_name, emoji, tag, description, origin_place, display_order, is_active, benefits, concern_tags) VALUES
('Coconut Oil',  'Narikela Taila', '🥥', 'Base Oil',
 'The cornerstone of our formula. Pure virgin coconut oil penetrates the hair shaft deeper than any other oil, providing unmatched moisture retention and protein preservation. Its unique molecular structure allows it to pass through the hair cortex, nourishing from within rather than just coating the surface.',
 'Kerala, South India', 1, TRUE,
 'Prevents protein loss from hair;Deep shaft penetration;Anti-microbial scalp protection;Reduces hygral fatigue;Retains natural moisture',
 'frizz,hair_fall,dull'),

('Hibiscus', 'Japakusuma', '🌺', 'Growth Stimulant',
 'Revered in Ayurveda as the flower of hair care. Rich in amino acids, vitamins A and C, hibiscus directly nourishes hair follicles. Its natural mucilage conditions and detangles, while its deep red pigments add a natural lustre to dull hair.',
 'Tamil Nadu, India', 2, TRUE,
 'Stimulates dormant follicles;Controls hair fall;Conditions and softens;Adds natural colour depth;Rich in Vitamin A and C',
 'hair_fall,slow_growth,dull'),

('Fenugreek', 'Methi', '🌰', 'Scalp Healer',
 'Fenugreek seeds are a powerhouse of proteins, nicotinic acid, and lecithin. They rebuild damaged hair, soothe irritated scalps, and create a protective coating on each strand. Its high iron and protein content make it particularly effective for dry, brittle hair prone to breakage.',
 'Rajasthan, India', 3, TRUE,
 'Rebuilds damaged follicles;Prevents dandruff;Rich in hair proteins and lecithin;Reduces scalp inflammation;Balances scalp pH',
 'dandruff,frizz,slow_growth'),

('Amla', 'Amalaki', '🫐', 'Grey Reversal',
 'Indian Gooseberry — nature''s richest source of Vitamin C at 600–700 mg per fruit, 20 times more than an orange. Amla strengthens hair from within, delays premature greying by preserving melanin production, and its powerful antioxidants protect follicles from oxidative stress.',
 'Uttar Pradesh, India', 4, TRUE,
 'Delays premature greying;Strengthens hair shaft;Highest natural Vitamin C;Boosts scalp circulation;Powerful antioxidant protection',
 'greying,hair_fall,dull'),

('Bhringraj', 'Eclipta Alba', '🌿', 'King of Herbs',
 'Known as the King of Hair Herbs in Ayurveda. Bhringraj has been scientifically studied and shown to have properties comparable to hair growth medications, without any side effects. It stimulates dormant follicles, improves scalp microcirculation, and has a proven calming effect on the nervous system — important as stress is a leading cause of hair loss.',
 'Assam, India', 5, TRUE,
 'Clinically studied hair regrowth;Reduces hair thinning;Deep follicle nourishment;Calms stress-related hair loss;Improves scalp blood flow',
 'hair_fall,slow_growth,greying'),

('Curry Leaves', 'Kadi Patta', '🍃', 'Strengthener',
 'Beyond the kitchen, curry leaves are a treasure for hair. Loaded with beta-carotene, proteins, and antioxidants, they protect follicles from oxidative damage and strengthen each strand from root to tip. Their high iron content also helps address anaemia-related hair loss.',
 'Karnataka, India', 6, TRUE,
 'Rich in beta-carotene;Protects from oxidative stress;Strengthens thin hair;Repairs damaged roots;High in iron and protein',
 'hair_fall,dull,slow_growth'),

('Neem', 'Nimba', '🌱', 'Purifier',
 'Nature''s most potent purifier. Neem''s powerful antibacterial, antifungal, and antiseptic properties make it the definitive solution for persistent dandruff, scalp infections, and itchiness. Azadirachtin and nimbin — neem''s active compounds — directly inhibit the Malassezia fungus responsible for dandruff.',
 'Maharashtra, India', 7, TRUE,
 'Eliminates dandruff at source;Powerful antifungal action;Reduces scalp itching;Balances scalp pH;Antibacterial and antiseptic',
 'dandruff,hair_fall'),

('Sesame', 'Tila Taila', '⚪', 'UV Shield',
 'One of the oldest cultivated crops, sesame oil forms a natural SPF barrier on the hair, shielding it from UV and heat damage. Its exceptional mineral profile — zinc, magnesium, copper, calcium — deeply nourishes dry and brittle hair. Sesame is the only natural oil with documented UV-blocking properties for hair.',
 'Gujarat, India', 8, TRUE,
 'Natural UV protection for hair;Rich in zinc, magnesium and copper;Seals moisture into cuticle;Restores elasticity to brittle hair;Adds mirror-like shine',
 'frizz,greying,dull');

-- ─── HOME BENEFITS SEED ────────────────────────────────────────────────────

TRUNCATE TABLE home_benefits;

INSERT INTO home_benefits (icon, title, description, display_order, is_active) VALUES
('💪', 'Strengthens Roots',    'Deep conditioning fortifies each strand from the follicle, reducing breakage and shedding significantly within weeks.', 1, TRUE),
('🌱', 'Promotes Growth',      'Bhringraj and Hibiscus stimulate dormant follicles, encouraging thick, healthy new hair growth from dormant roots.', 2, TRUE),
('✨', 'Adds Natural Shine',   'Coconut and Sesame oils coat the shaft with natural lustre, restoring brilliance without silicones or chemicals.', 3, TRUE),
('🧘', 'Calms Scalp',         'Neem''s anti-bacterial and anti-fungal properties soothe irritation, dandruff, and inflammation at the scalp level.', 4, TRUE),
('🔒', 'Locks Moisture',      'A rich emollient blend seals the cuticle, preventing dryness and combating frizz naturally even in humid conditions.', 5, TRUE),
('⏳', 'Delays Greying',      'Amla''s high Vitamin C content helps preserve natural melanin production for longer, slowing premature greying.', 6, TRUE);

-- ─── RECOMMENDATION RULES SEED ─────────────────────────────────────────────
-- Templates support: {name}, {hairType}, {scalpType}, {lifestyle}
-- Key ingredients: semicolon-separated

TRUNCATE TABLE recommendation_rules;

INSERT INTO recommendation_rules (concern_key, product_name, tagline, recommendation_template, key_ingredients, usage_tip, is_active) VALUES

('hair_fall',
 'Vedrithm Bhringraj Growth Formula',
 'Rooted strength. Lasting growth.',
 'Based on your profile, {name}, we''ve identified hair fall as your primary concern. Your {scalpType} scalp type calls for a deeply nourishing formula that works at the follicle level. Our Bhringraj-enriched blend is specifically designed to fortify follicles, reduce shedding, and stimulate dormant roots back to activity. Combined with Hibiscus and Amla, this formula significantly reduces fall within 4–6 weeks of consistent use.',
 'Bhringraj — Ayurveda''s #1 hair regrowth herb;Hibiscus — Follicle stimulator and strengthener;Amla — Root fortifier rich in Vitamin C;Curry Leaves — Antioxidant and keratin booster',
 'Warm the oil slightly, massage into roots for 5 minutes using circular motion. Leave overnight, 3× per week. Results visible within 6–8 weeks.',
 TRUE),

('slow_growth',
 'Vedrithm Bhringraj & Hibiscus Growth Elixir',
 'Wake your roots. Unlock your length.',
 'Dear {name}, slow growth is often linked to poor scalp circulation and under-nourished follicles. With {hairType} hair, your follicles need a targeted stimulating formula. The synergistic combination of Bhringraj — Ayurveda''s most studied hair growth herb — and Hibiscus, which dilates blood vessels around follicles, creates an optimal environment for accelerated, healthy growth.',
 'Bhringraj — Proven follicle activator;Hibiscus — Blood circulation stimulator;Amla — Protein synthesis support;Fenugreek — Keratin and lecithin builder',
 'Use a scalp massager tool while applying for 5–7 minutes. The increased circulation dramatically amplifies absorption. Apply 3× weekly consistently for best results.',
 TRUE),

('dandruff',
 'Vedrithm Neem Purifying Formula',
 'Cleanse. Balance. Restore.',
 '{name}, a {scalpType} scalp with dandruff needs a powerful purifier, not just a moisturiser. Our Neem-dominant formula leverages nature''s most potent antifungal herb to eliminate dandruff at its source — directly targeting the Malassezia fungus responsible for most scalp flaking — while Fenugreek restores your scalp''s natural pH balance for lasting relief.',
 'Neem — Powerful antifungal, eliminates dandruff source;Fenugreek — Scalp pH balancer;Coconut Oil — Moisturising anti-microbial base;Curry Leaves — Soothes scalp inflammation',
 'Part hair into sections and apply directly to the scalp. Massage for 10 minutes. Leave for 1 hour minimum, then wash. Use 3× per week until dandruff clears, then reduce to maintenance twice weekly.',
 TRUE),

('frizz',
 'Vedrithm Sesame Smoothing Formula',
 'Seal the moisture. Silence the frizz.',
 'For {name}''s frizz and dryness concerns, the root cause is an open hair cuticle that cannot retain moisture. With {hairType} hair, your strands are especially vulnerable to humidity-driven frizz. Our Sesame and Coconut blend creates a protective lipid barrier that seals the cuticle, locking moisture in and environmental humidity out — giving you smooth, manageable hair with natural shine.',
 'Sesame Oil — Cuticle sealer with natural UV protection;Coconut Oil — Deep moisture lock;Hibiscus — Natural conditioner and softener;Fenugreek — Protein rebuilder for damaged shafts',
 'Apply to mid-lengths and ends as a leave-in serum or as a pre-wash mask for 30 minutes before shampooing. A small amount goes a long way on dry hair ends.',
 TRUE),

('greying',
 'Vedrithm Amla Melanin Formula',
 'Preserve your colour. Preserve your youth.',
 'Dear {name}, premature greying is triggered by oxidative stress and melanin depletion at the follicle level. Our Amla-dominant formula — with the highest natural concentration of Vitamin C — works to preserve melanin production and slow the greying process naturally. For {hairType} hair, this blend absorbs deeply into the cortex where melanocytes reside, providing targeted antioxidant protection.',
 'Amla — Highest natural Vitamin C, preserves melanin;Sesame — Rich in copper and zinc for pigmentation;Bhringraj — Supports melanocyte function;Coconut Oil — Deep cortex penetration carrier',
 'Apply from root to tip, focusing on greying sections. Leave for minimum 2 hours or overnight, 3× weekly. Consistent use over 8–12 weeks shows visible improvement in new growth colour.',
 TRUE),

('dull',
 'Vedrithm Signature Radiance Blend',
 'Let your hair speak for itself.',
 '{name}, dull hair typically signals product buildup, cuticle damage, or lack of essential fatty acids. Our full-spectrum Signature Blend combines all 8 herbs in perfect Ayurvedic balance to comprehensively restore luminosity, repair the cuticle, and coat each {hairType} strand in a light-reflective sheen that looks naturally vibrant — never greasy.',
 'Sesame Oil — Natural shine and UV protection;Coconut Oil — Deep repair and moisture;Hibiscus — Colour depth and gloss;Amla — Antioxidant glow restoration',
 'Apply generously from roots to ends. Cover with a warm towel for 20 minutes for a deep conditioning treatment. Wash off thoroughly. Use 2–3× per week for a visible glow within 2 weeks.',
 TRUE),

('default',
 'Vedrithm Signature Herbal Blend',
 'Complete Ayurvedic care from root to tip.',
 '{name}, our full-spectrum Signature Blend combines all 8 Ayurvedic herbs in perfect balance for comprehensive hair nourishment. Crafted for those who want holistic care, this formula addresses strength, growth, shine, and scalp health simultaneously — the way ancient Ayurveda intended.',
 'Coconut Oil — Moisture and protein protection;Bhringraj — Growth stimulation;Amla — Vitamin C fortification;Neem — Scalp purification;Hibiscus — Shine and strength;Sesame — UV protection and lustre',
 'Apply generously from roots to ends. Cover with a warm towel for 20 minutes before washing for a salon-grade deep treatment. Use 2–3× per week.',
 TRUE);

-- ─── Useful admin views ────────────────────────────────────────────────────

CREATE OR REPLACE VIEW v_leads_full AS
SELECT
    qs.id,
    qs.name,
    qs.mobile_number,
    qs.hair_type,
    qs.scalp_type,
    qs.lifestyle,
    GROUP_CONCAT(qc.concern SEPARATOR ', ') AS concerns,
    qs.recommended_product,
    qs.submitted_at
FROM quiz_submissions qs
LEFT JOIN quiz_concerns qc ON qs.id = qc.submission_id
GROUP BY qs.id
ORDER BY qs.submitted_at DESC;

SELECT 'Vedrithm DB fully seeded ✓' AS status;
