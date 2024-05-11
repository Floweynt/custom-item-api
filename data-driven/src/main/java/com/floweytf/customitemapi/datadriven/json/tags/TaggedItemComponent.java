package com.floweytf.customitemapi.datadriven.json.tags;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TaggedItemComponent {
    Map<String, TaggedItemComponentInfo> TAG_PRODUCERS = ImmutableMap.<String, TaggedItemComponentInfo>builder()
        .put("wand", pure(StaticTextBeginComponent.MAGIC_WAND))
        .put("alch_potion", pure(StaticTextBeginComponent.ALCH_POTION))
        .put("attributes", AttributeComponent.INFO)
        .put("enchants", EnchantComponent.INFO)
        .build();

    private static TaggedItemComponentInfo pure(TaggedItemComponent value) {
        return new TaggedItemComponentInfo(true, o -> () -> value);
    }

    default void putComponentsStart(Consumer<Component> output) {

    }

    default void putComponentsEnd(Consumer<Component> output) {

    }

    record TaggedItemComponentInfo(boolean isStateless, Function<JsonObject, Supplier<TaggedItemComponent>> supplier) {

    }
}