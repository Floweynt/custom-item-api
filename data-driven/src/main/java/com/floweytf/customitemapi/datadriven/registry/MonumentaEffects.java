package com.floweytf.customitemapi.datadriven.registry;

import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.floweytf.customitemapi.datadriven.registry.MonumentaEffects.LevelDisplayType.*;

public enum MonumentaEffects {
    NIGHT_VISION("NightVision", "Night Vision", false, NONE, true),
    ABSORPTION_HEALTH("Absorption", "Absorption Health", false, PERCENT_BONUS, true),
    INSTANT_HEALTH("InstantHealthPercent", "Instant Health", false, PERCENT, false),
    SPEED("Speed", "Speed", false, PERCENT_BONUS, true),
    WATER_BREATHING("WaterBreath", "Water Breathing", false, NONE, true),
    NAUSEA("Nausea", "Nausea", true, NONE, true),
    RESISTANCE("Resistance", "Resistance", false, PERCENT_BONUS, true),
    MAGIC_RESISTANCE("MagicResistance", "Magic Resistance", false, PERCENT_BONUS, true),
    FIRE_IMMUNITY("VanillaFireRes", "Fire Immunity", false, NONE, true),
    JUMP_BOOST("JumpBoost", "Jump Boost", false, DISPLAY_LEVEL, true),
    BLINDNESS("Blindness", "Blindness", true, NONE, true),
    REGENERATION("Regeneration", "Regeneration", false, DISPLAY_LEVEL, true),
    HUNGER("Hunger", "Hunger", true, DISPLAY_LEVEL, true),
    POISON("Poison", "Poison", true, DISPLAY_LEVEL, true),
    INSTANT_DAMAGE("InstantDamagePercent", "Instant Damage", true, PERCENT, false),
    WITHER("Wither", "Wither", true, DISPLAY_LEVEL, true),
    HASTE("Haste", "Haste", false, DISPLAY_LEVEL, true),
    STRENGTH("damage", "Strength", false, PERCENT_BONUS, true),
    GLOWING("Glowing", "Glowing", false, NONE, true),
    HEALING_RATE("Heal", "Healing Rate", false, PERCENT_BONUS, true),
    MAX_HEALTH("MaxHealthIncrease", "Max Health", false, PERCENT_BONUS, true),
    ABILITY_COOLDOWNS("AbilityCooldownDecrease", "Ability Cooldowns", false, PERCENT_NEGATE, true),
    MAGIC_DAMAGE("MagicDamage", "Magic Damage", false, PERCENT_BONUS, true),
    KNOCKBACK_RESISTANCE("KnockbackResist", "Knockback Resistance", false, PERCENT_BONUS, true),
    KNOCKBACK_INCREASE("NegativeKnockbackResist", "Knockback Resistance", true, PERCENT_NEGATE, true),
    CONDUIT_POWER("ConduitPower", "Conduit Power", false, DISPLAY_LEVEL, true),
    SLOW("Slow", "Speed", false, PERCENT_NEGATE, true),
    WEAKNESS("Weakness", "Strength", false, PERCENT_NEGATE, true),
    BAD_LUCK("BadLuck", "Bad Luck", true, DISPLAY_LEVEL, true),
    MELEE_RESISTANCE("MeleeResistance", "Melee Resistance", false, PERCENT_BONUS, true),
    PROJECTILE_RESISTANCE("ProjectileResistance", "Projectile Resistance", false, PERCENT_BONUS, true),
    BLAST_RESISTANCE("BlastResistance", "Blast Resistance", false, PERCENT_BONUS, true),
    ATTACK_SPEED("AttackSpeed", "Attack Speed", false, PERCENT_BONUS, true),
    ATTACK_SPEED_SLOW("NegativeAttackSpeed", "Attack Speed", true, PERCENT_NEGATE, true),
    MAX_HEALTH_DECREASE("MaxHealthDecrease", "Max Health", true, PERCENT_NEGATE, true),
    VULNERABILITY("Vulnerability", "Resistance", true, PERCENT_NEGATE, true),
    BLAST_VULNERABILITY("BlastVulnerability", "Blast Resistance", true, PERCENT_NEGATE, true),
    FIRE_VULNERABILITY("FireVulnerability", "Fire Resistance", true, PERCENT_NEGATE, true),
    MAGIC_VULNERABILITY("MagicVulnerability", "Magic Resistance", true, PERCENT_NEGATE, true),
    MELEE_VULNERABILITY("MeleeVulnerability", "Melee Resistance", true, PERCENT_NEGATE, true),
    PROJECTILE_VULNERABILITY("ProjectileVulnerability", "Projectile Resistance", true, PERCENT_NEGATE, true),
    FALL_VULNERABILITY("FallVulnerability", "Fall Resistance", true, PERCENT_NEGATE, true),
    CLUCKING("Clucking", "Clucking", true, NONE, false),
    MELEE_DAMAGE("MeleeDamage", "Melee Damage", false, PERCENT_BONUS, true),
    PROJECTILE_DAMAGE("ProjectileDamage", "Projectile Damage", false, PERCENT_BONUS, true),
    HEALING_RATE_DECREASE("AntiHeal", "Healing Rate", true, PERCENT_NEGATE, true),
    MINING_FATIGUE("MiningFatigue", "Mining Fatigue", true, DISPLAY_LEVEL, true),
    EXPERIENCE("ExpBonus", "Experience", false, PERCENT_BONUS, true),
    EXPERIENCE_DECREASE("ExpLoss", "Experience", true, PERCENT_NEGATE, true),
    SOUL_THREAD_CHANCE("SoulThreadBonus", "Soul Thread Chance", false, PERCENT_BONUS, true),
    SOUL_THREAD_CHANCE_DECREASE("SoulThreadReduction", "Soul Thread Chance", true, PERCENT_NEGATE, true),
    FALL_RESISTANCE("FallResistance", "Fall Resistance", false, PERCENT_BONUS, true),
    ABILITY_COOLDOWNS_INCREASE("AbilityCooldownIncrease", "Ability Cooldowns", true, PERCENT_BONUS, true),
    BLEED("Bleed", "Bleed", true, PERCENT_NEGATE, true),
    DURABILITY_DECREASE("DurabilityLoss", "Durability", true, PERCENT_NEGATE, true),
    ARROW_SAVE_CHANCE("ArrowSaving", "Arrow Save Chance", false, PERCENT_BONUS, true),
    DURABILITY_INCREASE("DurabilitySave", "Durability", false, PERCENT_BONUS, true),
    MELEE_DAMAGE_WEAKNESS("MeleeWeakness", "Melee Damage", true, PERCENT_NEGATE, true);

    private static final Map<String, MonumentaEffects> TABLE = Arrays.stream(values())
        .collect(Collectors.toMap(
            entry -> entry.id,
            entry -> entry
        ));
    private final String id;
    private final String displayName;
    private final boolean isNegative;
    private final LevelDisplayType levelDisplayType;
    private final boolean displayDuration;

    MonumentaEffects(String id, String displayName, boolean isNegative, LevelDisplayType levelDisplayType,
                     boolean displayDuration) {
        this.id = id;
        this.displayName = displayName;
        this.isNegative = isNegative;
        this.levelDisplayType = levelDisplayType;
        this.displayDuration = displayDuration;
    }

    public static MonumentaEffects fromJson(JsonElement element) {
        return Objects.requireNonNull(TABLE.get(element.getAsString()));
    }

    public String displayName() {
        return displayName;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public LevelDisplayType displayLevelType() {
        return levelDisplayType;
    }

    public boolean displayDuration() {
        return displayDuration;
    }

    public String id() {
        return id;
    }

    public enum LevelDisplayType {
        PERCENT_BONUS,
        PERCENT_NEGATE,
        NONE,
        PERCENT,
        DISPLAY_LEVEL;
    }
}