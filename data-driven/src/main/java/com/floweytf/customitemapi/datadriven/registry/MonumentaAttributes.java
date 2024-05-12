package com.floweytf.customitemapi.datadriven.registry;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import javax.annotation.Nullable;

public enum MonumentaAttributes {
    THROW_RATE("Throw Rate", true),
    THORNS_DAMAGE("Thorns Damage", false),
    PROJECTILE_DAMAGE("Projectile Damage", true),
    PROJECTILE_SPEED("Projectile Speed", true),
    AGILITY("Agility", false, "#33CCFF", "#D02E28"),
    ARMOR("Armor", false, "#33CCFF", "#D02E28"),
    MOVEMENT_SPEED("Speed", false),
    MAX_HEALTH("Max Health", false),
    KNOCKBACK_RESISTANCE("Knockback Resistance", false),
    ATTACK_SPEED("Attack Speed", true),
    ATTACK_DAMAGE("Attack Damage", true),
    MAGIC_DAMAGE("Magic Damage", false),
    SPELL_POWER("Spell Power", false),
    POTION_DAMAGE("Potion Damage", true),
    POTION_RADIUS("Potion Radius", true);

    private final String name;
    private final boolean isBase;
    private final TextColor positiveColor;
    private final TextColor negativeColor;

    MonumentaAttributes(String name, boolean isBase, String positiveColor, String negativeColor) {
        this.name = name;
        this.isBase = isBase;
        this.positiveColor = TextColor.fromHexString(positiveColor);
        this.negativeColor = TextColor.fromHexString(negativeColor);
    }

    MonumentaAttributes(String name, boolean isBase) {
        this(name, isBase, "#5555FF", "##FF5555");
    }

    public static MonumentaAttributes fromJson(JsonElement e) {
        return valueOf(e.getAsString().toUpperCase());
    }

    public String displayName() {
        return name;
    }

    public boolean isBase() {
        return isBase;
    }

    public TextColor positiveColor() {
        return positiveColor;
    }

    public TextColor negativeColor() {
        return negativeColor;
    }

    public enum Operation {
        ADD, BASE, MULTIPLY;

        public static Operation fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }
    }

    public enum Usages {
        MAINHAND("in", "Main Hand"),
        OFFHAND("in", "Off Hand"),
        HEAD("on", "Head"),
        CHEST("on", "Chest"),
        LEGS("on", "Legs"),
        FEET("on", "Feet"),
        PROJECTILE(null, "Shot");

        private final Component displayText;

        Usages(@Nullable String preposition, String text) {
            Component c = Component.text(text).color(NamedTextColor.GOLD);

            if (preposition != null) {
                c = Component.text(preposition).append(Component.space()).append(c);
            }

            this.displayText = c;
        }

        public static Usages fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }

        public Component displayText() {
            return displayText;
        }
    }
}
