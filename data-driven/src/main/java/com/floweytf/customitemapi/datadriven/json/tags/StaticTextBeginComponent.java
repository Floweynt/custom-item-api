package com.floweytf.customitemapi.datadriven.json.tags;

import net.kyori.adventure.text.Component;

import java.util.function.Consumer;

public enum StaticTextBeginComponent implements TaggedItemComponent {
    MAGIC_WAND("* Magic Wand *"),
    ALCH_POTION("* Alchemical Utensil *");

    private final Component text;

    StaticTextBeginComponent(Component text) {
        this.text = text;
    }

    StaticTextBeginComponent(String text) {
        this(Component.text(text));
    }

    @Override
    public void putComponentsStart(Consumer<Component> output) {
        output.accept(text);
    }
}
