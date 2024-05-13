package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.datadriven.Pair;
import com.floweytf.customitemapi.datadriven.json.tags.TaggedItemComponent;
import com.floweytf.customitemapi.datadriven.registry.MonumentaLocations;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRarities;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRegions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class JsonCustomItem {
    public static final String NO_GLINT = "noglint";
    public static final String GLINT = "glint";
    public static final Style DEFAULT_LORE_STYLE = Style.style(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC,
        false);
    public static final Style NO_ITALIC = Style.empty().decoration(TextDecoration.ITALIC, false);

    private static Component getComponent(JsonObject object, String key) {
        // TODO: do better here
        return GsonComponentSerializer.gson().deserializeFromTree(object.get(key));
    }

    private static Optional<String> getStringOrOptionalEmpty(JsonObject object, String key) {
        // TODO: do better here
        return object.has(key) ? Optional.of(object.get(key).getAsString()) : Optional.empty();
    }

    private static String getTagId(JsonElement element) {
        if (element.isJsonPrimitive())
            return element.getAsString();

        return element.getAsJsonObject().get("tag").getAsString();
    }

    private static TriState setTriStateOrRun(TriState state, boolean value, Runnable onAlreadySet) {
        if (state == TriState.NOT_SET) {
            state = TriState.byBoolean(value);
        } else {
            onAlreadySet.run();
        }

        return state;
    }

    /**
     * ItemVariant API:
     * An ItemVariant is generically interpreted with the "variant_type" field:
     * "variant": "masterwork[level=1]"
     */


    public static Pair<Material, Supplier<CustomItem>> readFromJson(Logger logger, JsonObject resource, NamespacedKey id) {
        final var name = getComponent(resource, "name");
        final var material = Objects.requireNonNull(Material.matchMaterial(resource.get("item").getAsString()));
        final var rarity = getStringOrOptionalEmpty(resource, "rarity")
            .map(String::toUpperCase).map(MonumentaRarities::valueOf);
        final var region = getStringOrOptionalEmpty(resource, "region")
            .map(String::toUpperCase).map(MonumentaRegions::valueOf);
        final var location = getStringOrOptionalEmpty(resource, "location")
            .map(String::toUpperCase).map(MonumentaLocations::valueOf);

        final var lore = resource.getAsJsonArray("lore").asList().stream()
            .map(GsonComponentSerializer.gson()::deserializeFromTree)
            .toList();
        final Optional<Class<?>> pluginImpl = getStringOrOptionalEmpty(resource, "plugin_implementation")
            .map(className -> {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

        // obtain some flags
        TriState hasGlint = TriState.NOT_SET;
        boolean isStateless = pluginImpl.isEmpty();
        final var tagInfos = new ArrayList<Pair<TaggedItemComponent.TaggedItemComponentInfo,
            Supplier<TaggedItemComponent>>>();
        final Runnable onDuplicateGlintTag = () -> logger.warn("While parsing " + id + " - duplicate glint specifier tag, " +
            "ignoring. Please fix this in " + "the json file!");

        if (resource.has("tags")) {
            for (final var tag : resource.getAsJsonArray("tags")) {
                final var tagId = getTagId(tag);

                JsonObject objectTag;
                if (tag.isJsonPrimitive()) {
                    objectTag = new JsonObject();
                    objectTag.addProperty("tag", tagId);
                } else {
                    objectTag = tag.getAsJsonObject();
                }

                switch (tagId) {
                    case NO_GLINT -> hasGlint = setTriStateOrRun(hasGlint, false, onDuplicateGlintTag);
                    case GLINT -> hasGlint = setTriStateOrRun(hasGlint, true, onDuplicateGlintTag);
                    default -> {
                        final var tagInfo = TaggedItemComponent.TAG_PRODUCERS.get(tagId);
                        if (tagInfo == null) {
                            logger.warn("While parsing " + id + " - unknown tag with id: " + tagId);
                            return null;
                        }

                        isStateless |= tagInfo.isStateless();
                        tagInfos.add(new Pair<>(tagInfo, tagInfo.supplier().apply(objectTag)));
                    }
                }
            }
        }

        if (isStateless) {
            final var itemInstance = new StatelessJsonCustomItem(
                name, lore,
                rarity, region, location,
                pluginImpl,
                hasGlint, tagInfos.stream().map(u -> u.second().get()).toList()
            );
            return new Pair<>(material, () -> itemInstance);
        }

        return null;
    }
}