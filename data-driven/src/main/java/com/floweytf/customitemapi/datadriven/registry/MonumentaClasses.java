package com.floweytf.customitemapi.datadriven.registry;

import com.google.gson.JsonElement;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public enum MonumentaClasses {
    ALCHEMIST("Alchemist", "#81D434"),
    CLERIC("Cleric", "##FFC644"),
    MAGE("Mage", "#A129D3"),
    ROGUE("Rogue", "#36393D"),
    GENERALIST("Generalist", "#9F8F91"),
    SCOUT("Scout", "#59B4EB"),
    SHAMAN("Shaman", "#009900"),
    WARLOCK("Warlock", "#F0489E"),
    WARRIOR("Warrior", "#D32818");

    private final Component text;

    MonumentaClasses(String text, String color) {
        this.text = Component.text(text).color(TextColor.fromHexString(color));
    }

    public static MonumentaClasses fromJson(JsonElement e) {
        return valueOf(e.getAsString().toUpperCase());
    }

    public Component text() {
        return text;
    }
}
