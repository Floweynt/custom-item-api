package com.floweytf.customitemapi.datadriven.json.tag;

import com.floweytf.customitemapi.datadriven.json.tag.tags.*;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class TaggedComponentRegistry {
    public static final Map<String, TaggedComponentType<?, ?>> TAGS = new ImmutableMap.Builder<String, TaggedComponentType<?, ?>>()
        .put("attributes", AttributeComponent.TYPE)
        .put("book", BookComponent.TYPE)
        .put("charm", CharmComponent.TYPE)
        .put("enchants", EnchantComponent.TYPE)
        .put("masterwork", MasterworkComponent.TYPE)
        .put("potion", PotionComponent.TYPE)
        .put("quest", QuestComponent.INFO)
        .put("wand", EmptyNonmergableConfig.pure("wand", StaticTextBeginComponent.MAGIC_WAND))
        .put("alch_potion", EmptyNonmergableConfig.pure("alch_potion", StaticTextBeginComponent.ALCH_POTION))
        .put("material", EmptyNonmergableConfig.pure("material", StaticTextBeginComponent.MATERIAL))
        .build();
}