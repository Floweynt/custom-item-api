package com.floweytf.customitemapi.datadriven.json;

import net.kyori.adventure.text.Component;

@FunctionalInterface
public
interface ComponentWriter {
    void writeOne(Component text);

    default void writeMany(Component... text) {
        for (final var component : text) {
            writeOne(component);
        }
    }

    default void writeOne(String text) {
        writeOne(Component.text(text));
    }

    default void writeOne(Component first, Component... rest) {
        for (final var comp : rest) {
            first = first.append(comp);
        }

        writeOne(first);
    }
}
