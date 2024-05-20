package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.EmptyNonmergableConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum StaticTextBeginComponent implements TaggedComponent {
    MAGIC_WAND("* Magic Wand *"),
    MATERIAL(Component.text("Material").color(NamedTextColor.GRAY)),
    ALCH_POTION("* Alchemical Utensil *");

    private final Component text;

    StaticTextBeginComponent(Component text) {
        this.text = text;
    }

    StaticTextBeginComponent(String text) {
        this(Component.text(text));
    }

    @Override
    public void putComponentsStart(ComponentWriter output) {
        output.writeOne(text);
    }
}
