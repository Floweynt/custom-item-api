package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MasterworkComponent implements TaggedItemComponent {
    private static final Int2ObjectMap<MasterworkComponent> BY_LEVEL = new Int2ObjectArrayMap<>();

    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var inst = BY_LEVEL.computeIfAbsent(object.get("level").getAsInt(), MasterworkComponent::new);
        return () -> inst;
    });

    private final Component text;

    private MasterworkComponent(int level) {
        this.text = Component.text("Masterwork : ")
            .append(Component.text("★".repeat(level), NamedTextColor.GOLD))
            .append(Component.text("☆".repeat(Math.max(4 - level, 0)), NamedTextColor.DARK_GRAY));
    }

    @Override
    public void putComponentsStart(ComponentWriter output) {
        output.writeOne(text);
    }
}
