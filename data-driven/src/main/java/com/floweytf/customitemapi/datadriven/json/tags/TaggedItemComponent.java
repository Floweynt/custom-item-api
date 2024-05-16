package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface TaggedItemComponent {
    Map<String, TaggedItemComponentInfo> TAG_PRODUCERS = ImmutableMap.<String, TaggedItemComponentInfo>builder()
        .put("wand", pure(StaticTextBeginComponent.MAGIC_WAND))
        .put("alch_potion", pure(StaticTextBeginComponent.ALCH_POTION))
        .put("material", pure(StaticTextBeginComponent.MATERIAL))
        .put("attributes", AttributeComponent.INFO)
        .put("enchants", EnchantComponent.INFO)
        .put("quest", QuestComponent.INFO)
        .put("charm", CharmComponent.INFO)
        .put("potion", PotionComponent.INFO)
        .put("book", BookComponent.INFO)
        .put("masterwork", MasterworkComponent.INFO)
        .build();

    private static TaggedItemComponentInfo pure(TaggedItemComponent value) {
        return new TaggedItemComponentInfo(true, o -> () -> value);
    }

    default void putComponentsStart(ComponentWriter output) {

    }

    default void putComponentsEnd(ComponentWriter output) {

    }

    default void configure(ExtraItemData data) {

    }

    record TaggedItemComponentInfo(boolean isStateless, Function<JsonObject, Supplier<TaggedItemComponent>> supplier) {

    }
}