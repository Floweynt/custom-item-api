package com.floweytf.customitemapi.datadriven.json.tag;

import com.floweytf.customitemapi.datadriven.PluginMain;
import org.jetbrains.annotations.Nullable;

public class EmptyNonmergableConfig implements TaggedComponentConfig<EmptyNonmergableConfig> {
    private final String name;

    public EmptyNonmergableConfig(String name) {
        this.name = name;
    }

    @Nullable
    @Override
    public EmptyNonmergableConfig tryMerge(EmptyNonmergableConfig other) {
        PluginMain.LOGGER.warn("duplicate tag {}", name);
        return this;
    }

    public static <T extends TaggedComponent> TaggedComponentType<EmptyNonmergableConfig, T> pure(String name, T instance) {
        final var config = new EmptyNonmergableConfig(name);
        return new TaggedComponentType<>(
            ignored -> config,
            ignored -> instance
        );
    }
}