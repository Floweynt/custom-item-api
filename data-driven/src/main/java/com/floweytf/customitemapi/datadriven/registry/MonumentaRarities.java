package com.floweytf.customitemapi.datadriven.registry;

import com.floweytf.customitemapi.datadriven.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

public enum MonumentaRarities {
    T0(nameFormat("Tier 0", "dark_gray"), titleFormat()),
    T1(nameFormat("Tier I", "dark_gray"), titleFormat()),
    T2(nameFormat("Tier II", "dark_gray"), titleFormat()),
    T3(nameFormat("Tier III", "dark_gray"), titleFormat()),
    T4(nameFormat("Tier IV", "dark_gray"), titleFormat()),
    T5(nameFormat("Tier V", "dark_gray"), titleFormat()),
    EPIC(nameFormat("Epic", "#B314E3", TextDecoration.BOLD),
        titleFormat(TextDecoration.UNDERLINED,
            TextDecoration.BOLD)),
    ARTIFACT(nameFormat("Artifact", "#D02E28"),
        titleFormat(TextDecoration.BOLD)),
    RARE(nameFormat("Rare", "#4AC2E5"), titleFormat(TextDecoration.BOLD)),
    UNCOMMON(nameFormat("Uncommon", "#C0C0C0"),
        titleFormat(TextDecoration.BOLD)),
    COMMON(nameFormat("Common", "#C0C0C0"),
        titleFormat(TextDecoration.BOLD)),
    CHARM(nameFormat("Charm", "#FFFA75"), titleFormat()),
    RARECHARM(nameFormat("Rare Charm", "#4AC2E5"), titleFormat()),
    EPICCHARM(nameFormat("Epic Charm", "#B314E3"), titleFormat()),
    FISH(nameFormat("Fish", "dark_gray"), titleFormat()),
    UNIQUE(nameFormat("Unique", "#C8A2C8"),
        titleFormat(TextDecoration.BOLD)),
    TROPHY(nameFormat("Trophy", "#CAFFFD"),
        titleFormat(TextDecoration.BOLD)),
    EVENT(nameFormat("Event", "#7FFFD4"), titleFormat(TextDecoration.BOLD)),
    PATRON(nameFormat("Patron Made", "#82DB17"),
        titleFormat(TextDecoration.BOLD)),
    KEY(nameFormat("Key", "#47B6B5", TextDecoration.BOLD),
        titleFormat(TextDecoration.BOLD)),
    LEGACY(nameFormat("Legacy", "#EEE6D6"),
        titleFormat(TextDecoration.BOLD)),
    CURRENCY(nameFormat("Currency", "#DCAE32"),
        titleFormat(TextDecoration.BOLD)),
    OBFUSCATED(nameFormat("Stick_:)", "#5D2D87", TextDecoration.OBFUSCATED),
        titleFormat(TextDecoration.BOLD)),
    LEGENDARY(nameFormat("Legendary", "#FFD700", TextDecoration.BOLD),
        titleFormat(TextDecoration.BOLD)),
    EVENT_CURRENCY(nameFormat("Event Currency", "#DCAE32"),
        titleFormat(TextDecoration.BOLD)),
    ;

    private final Component text;
    private final Style nameFormat;

    MonumentaRarities(Component text, Style nameFormat) {
        this.text = text;
        this.nameFormat = nameFormat;
    }

    private static Component nameFormat(String text, String color, TextDecoration... decorations) {
        return Component.text(text).style(Style.style(Utils.colorFromString(color), decorations));
    }

    public static Style titleFormat(TextDecoration... decoration) {
        return Style.empty().decorate(decoration);
    }

    public Component getText() {
        return text;
    }

    public Style getNameFormat() {
        return nameFormat;
    }
}