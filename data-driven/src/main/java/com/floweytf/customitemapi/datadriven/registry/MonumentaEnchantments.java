package com.floweytf.customitemapi.datadriven.registry;

import com.google.gson.JsonElement;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public enum MonumentaEnchantments {
    LOOTING("minecraft:looting", "Looting", false),
    AQUA_AFFINITY("minecraft:aqua_affinity", "Aqua Affinity", true),
    DEPTH_STRIDER("minecraft:depth_strider", "Depth Strider", false),
    EFFICIENCY("minecraft:efficiency", "Efficiency", false),
    FORTUNE("minecraft:fortune", "Fortune", false),
    INFINITY("minecraft:infinity", "Infinity", true),
    MULTISHOT("minecraft:multishot", "Multishot", true),
    KNOCKBACK("minecraft:knockback", "Knockback", false),
    SOUL_SPEED("minecraft:soul_speed", "Soul Speed", false),
    LURE("minecraft:lure", "Lure", false),
    MENDING("minecraft:mending", "Mending", true),
    POWER("minecraft:power", "Power", false),
    PUNCH("minecraft:punch", "Punch", false),
    QUICK_CHARGE("minecraft:quick_charge", "Quick Charge", false),
    RESPIRATION("minecraft:respiration", "Respiration", false),
    RIPTIDE("minecraft:riptide", "Riptide", false),
    SILK_TOUCH("minecraft:silk_touch", "Silk Touch", true),
    SWEEPING_EDGE("minecraft:sweeping", "Sweeping Edge", false),
    UNBREAKING("minecraft:unbreaking", "Unbreaking", false),
    BLAST_PROTECTION("minecraft:blast_protection", "Blast Protection", false),
    FIRE_ASPECT("minecraft:fire_aspect", "Fire Aspect", false),
    FEATHER_FALLING("minecraft:feather_falling", "Feather Falling", false),
    ABYSSAL("monumenta:abyssal", "Abyssal", false),
    ADAPTABILITY("monumenta:adaptability", "Adaptability", true),
    ADRENALINE("monumenta:adrenaline", "Adrenaline", false),
    ALCHEMICAL_UTENSIL("monumenta:alchemical_utensil", "Alchemical Utensil", false),
    APTITUDE("monumenta:aptitude", "Aptitude", false),
    BLAST_FRAGILITY("monumenta:blast_fragility", "Blast Fragility", false),
    BLEEDING("monumenta:bleeding", "Bleeding", false),
    BROOMSTICK("monumenta:broomstick", "Broomstick", true),
    CLOAKED("monumenta:cloaked", "Cloaked", false),
    CLUCKING("monumenta:clucking", "Clucking", true),
    CURSE_OF_ANEMIA("monumenta:curse_of_anemia", "Curse of Anemia", false),
    CURSE_OF_CORRUPTION("monumenta:curse_of_corruption", "Curse of Corruption", true),
    CURSE_OF_CRIPPLING("monumenta:curse_of_crippling", "Curse of Crippling", false),
    CURSE_OF_EPHEMERALITY("monumenta:curse_of_ephemerality", "Curse of Ephemerality", true),
    CURSE_OF_IRREPARABILITY("monumenta:curse_of_irreparability", "Curse of Irreparability", true),
    CURSE_OF_VANISHING("minecraft:vanishing_curse", "Curse of Vanishing", true),
    DARKSIGHT("monumenta:darksight", "Darksight", true),
    DECAY("monumenta:decay", "Decay", false),
    DIVINE_AURA("monumenta:divine_aura", "Divine Aura", true),
    EARTH_ASPECT("monumenta:earth_aspect", "Earth Aspect", false),
    ETHEREAL("monumenta:ethereal", "Ethereal", false),
    EVASION("monumenta:evasion", "Evasion", false),
    FIRE_FRAGILITY("monumenta:fire_fragility", "Fire Fragility", false),
    FIRE_PROTECTION("monumenta:fire_protection", "Fire Protection", false),
    FIRST_STRIKE("monumenta:first_strike", "First Strike", false),
    GILLS("monumenta:gills", "Gills", true),
    GUARD("monumenta:guard", "Guard", false),
    HIDEATTRIBUTES("monumenta:hideattributes", "HideAttributes", false),
    HIDEENCHANTS("monumenta:hideenchants", "HideEnchants", false),
    HIDEINFO("monumenta:hideinfo", "HideInfo", false),
    ICE_ASPECT("monumenta:ice_aspect", "Ice Aspect", false),
    INEPTITUDE("monumenta:ineptitude", "Ineptitude", false),
    INFERNO("monumenta:inferno", "Inferno", false),
    INSTANT_DRINK("monumenta:instant_drink", "Instant Drink", true),
    INTOXICATING_WARMTH("monumenta:intoxicating_warmth", "Intoxicating Warmth", true),
    INTUITION("monumenta:intuition", "Intuition", true),
    INURE("monumenta:inure", "Inure", false),
    JUNGLES_NOURISHMENT("monumenta:jungles_nourishment", "Jungle's Nourishment", true),
    LIFE_DRAIN("monumenta:life_drain", "Life Drain", false),
    LIQUID_COURAGE("monumenta:liquid_courage", "Liquid Courage", true),
    MAGIC_FRAGILITY("monumenta:magic_fragility", "Magic Fragility", false),
    MAGIC_PROTECTION("monumenta:magic_protection", "Magic Protection", false),
    MAGIC_WAND("monumenta:magic_wand", "Magic Wand", false),
    MATERIAL("monumenta:material", "Material", false),
    MELEE_FRAGILITY("monumenta:melee_fragility", "Melee Fragility", false),
    MELEE_PROTECTION("monumenta:melee_protection", "Melee Protection", false),
    NOGLINT("monumenta:noglint", "NoGlint", false),
    OINKING("monumenta:oinking", "Oinking", true),
    PIERCING("monumenta:piercing", "Piercing", false),
    POINT_BLANK("monumenta:point_blank", "Point Blank", false),
    POISE("monumenta:poise", "Poise", false),
    PROJECTILE_PROTECTION("monumenta:projectile_protection", "Projectile Protection", false),
    RAGE_OF_THE_KETER("monumenta:rage_of_the_keter", "Rage of the Keter", true),
    RECOIL("monumenta:recoil", "Recoil", false),
    REFLEXES("monumenta:reflexes", "Reflexes", false),
    REGENERATION("monumenta:regeneration", "Regeneration", false),
    REGICIDE("monumenta:regicide", "Regicide", false),
    RETRIEVAL("monumenta:retrieval", "Retrieval", false),
    REVERB("monumenta:reverb", "Reverb", false),
    SECOND_WIND("monumenta:second_wind", "Second Wind", false),
    SHIELDING("monumenta:shielding", "Shielding", false),
    SLAYER("monumenta:slayer", "Slayer", false),
    SMITE("monumenta:smite", "Smite", false),
    SNIPER("monumenta:sniper", "Sniper", false),
    STAMINA("monumenta:stamina", "Stamina", false),
    STARVATION("monumenta:starvation", "Starvation", false),
    STEADFAST("monumenta:steadfast", "Steadfast", false),
    SUSTENANCE("monumenta:sustenance", "Sustenance", false),
    TECHNIQUE("monumenta:technique", "Technique", false),
    TEMPO("monumenta:tempo", "Tempo", false),
    TEMPORAL_BENDER("monumenta:temporal_bender", "Temporal Bender", true),
    THROWING_KNIFE("monumenta:throwing_knife", "Throwing Knife", false),
    THUNDER_ASPECT("monumenta:thunder_aspect", "Thunder Aspect", false),
    TRIAGE("monumenta:triage", "Triage", false),
    TRIVIUM("monumenta:trivium", "Trivium", false),
    TWO_HANDED("monumenta:two_handed", "Two Handed", true),
    UNBREAKABLE("monumenta:unbreakable", "Unbreakable", false),
    WEIGHTLESS("monumenta:weightless", "Weightless", true),
    RADIANT("monumenta:radiant", "Radiant", true),
    WIND_ASPECT("monumenta:wind_aspect", "Wind Aspect", false),
    WORLDLY_PROTECTION("monumenta:worldly_protection", "Worldly Protection", false),
    ERUPTION("monumenta:eruption", "Eruption", false),
    RESURRECTION("monumenta:resurrection", "Resurrection", true),
    MULTITOOL("monumenta:multitool", "Multitool", false),
    PROJECTILE_FRAGILITY("monumenta:projectile_fragility", "Projectile Fragility", false),
    HEX_EATER("monumenta:hex_eater", "Hex Eater", false),
    QUAKE("monumenta:quake", "Quake", false),
    CUMBERSOME("monumenta:cumbersome", "Cumbersome", true),
    DUELIST("monumenta:duelist", "Duelist", false),
    ARCANE_THRUST("monumenta:arcane_thrust", "Arcane Thrust", false),
    SAPPER("monumenta:sapper", "Sapper", false),
    CHAOTIC("monumenta:chaotic", "Chaotic", false),
    ASHES_OF_ETERNITY("monumenta:ashes_of_eternity", "Ashes of Eternity", true),
    DELETEONSHATTER("monumenta:delete_on_shatter", "DeleteOnShatter", false),
    BAAING("monumenta:baaing", "Baaing", true),
    CURSE_OF_SHRAPNEL("monumenta:curse_of_shrapnel", "Curse of Shrapnel", false),
    DRILLING("monumenta:drilling", "Drilling", false),
    CURSE_OF_INSTABILITY("monumenta:curse_of_instability", "Curse of Instability", true),
    EXCAVATOR("monumenta:excavator", "Excavator", true);

    private static final Map<String, MonumentaEnchantments> TABLE = Arrays.stream(values()).collect(Collectors.toMap(
        e -> e.id,
        e -> e
    ));
    private final String id;
    private final String displayText;
    private final boolean shouldDisplayLevel;

    MonumentaEnchantments(String id, String displayText, boolean displayLevel) {
        this.id = id;
        this.displayText = displayText;
        this.shouldDisplayLevel = displayLevel;
    }

    public static MonumentaEnchantments fromJson(JsonElement e) {
        return Objects.requireNonNull(TABLE.get(e.getAsString()));
    }

    public String id() {
        return id;
    }

    public boolean shouldDisplayLevel() {
        return shouldDisplayLevel;
    }

    public String displayText() {
        return displayText;
    }
}
