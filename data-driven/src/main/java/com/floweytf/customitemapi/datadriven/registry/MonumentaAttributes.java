package com.floweytf.customitemapi.datadriven.registry;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nullable;

public enum MonumentaAttributes {
    THROW_RATE("Throw Rate", true),
    THORNS_DAMAGE("Thorns Damage", false),
    PROJECTILE_DAMAGE("Projectile Damage", true),
    PROJECTILE_SPEED("Projectile Speed", true),
    AGILITY("Agility", false, "#33CCFF", "#D02E28"),
    ARMOR("Armor", false, "#33CCFF", "#D02E28"),
    MOVEMENT_SPEED("Speed", false, Attribute.GENERIC_MOVEMENT_SPEED),
    MAX_HEALTH("Max Health", false, Attribute.GENERIC_MAX_HEALTH),
    KNOCKBACK_RESISTANCE("Knockback Resistance", false, Attribute.GENERIC_KNOCKBACK_RESISTANCE),
    ATTACK_SPEED("Attack Speed", true, Attribute.GENERIC_ATTACK_SPEED),
    ATTACK_DAMAGE("Attack Damage", true, Attribute.GENERIC_ATTACK_DAMAGE),
    MAGIC_DAMAGE("Magic Damage", false),
    SPELL_POWER("Spell Power", false),
    POTION_DAMAGE("Potion Damage", true),
    POTION_RADIUS("Potion Radius", true);

    private final String name;
    private final boolean isBase;
    private final TextColor positiveColor;
    private final TextColor negativeColor;
    @Nullable
    private final Attribute vanilla;

    MonumentaAttributes(String name, boolean isBase, String positiveColor, String negativeColor, @Nullable Attribute vanilla) {
        this.name = name;
        this.isBase = isBase;
        this.vanilla = vanilla;
        this.positiveColor = TextColor.fromHexString(positiveColor);
        this.negativeColor = TextColor.fromHexString(negativeColor);
    }


    MonumentaAttributes(String name, boolean isBase, String positiveColor, String negativeColor) {
        this(name, isBase, positiveColor, negativeColor, null);
    }

    MonumentaAttributes(String name, boolean isBase, Attribute vanilla) {
        this(name, isBase, "#5555FF", "##FF5555", vanilla);
    }

    MonumentaAttributes(String name, boolean isBase) {
        this(name, isBase, "#5555FF", "##FF5555", null);
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

    @Nullable
    public Attribute vanilla() {
        return vanilla;
    }

    public enum Operation {
        ADD(0), BASE(0), MULTIPLY(0);

        private final int minecraftId;

        Operation(int minecraftId) {
            this.minecraftId = minecraftId;
        }

        public static Operation fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }

        public int minecraftId() {
            return minecraftId;
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
        private final @Nullable EquipmentSlot vanilla;

        Usages(@Nullable String preposition, String text, EquipmentSlot vanilla) {
            this.vanilla = vanilla;
            Component c = Component.text(text).color(NamedTextColor.GOLD);

            if (preposition != null) {
                c = Component.text(preposition).append(Component.space()).append(c);
            }

            this.displayText = c;
        }

        Usages(@Nullable String preposition, String text) {
            this(preposition, text, null);
        }

        public static Usages fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }

        public Component displayText() {
            return displayText;
        }

        @Nullable
        public EquipmentSlot vanilla() {
            return vanilla;
        }
    }
}
