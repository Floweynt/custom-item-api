package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.datadriven.Pair;
import com.floweytf.customitemapi.datadriven.json.tags.TaggedItemComponent;
import com.floweytf.customitemapi.datadriven.registry.MonumentaLocations;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRarities;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRegions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.util.TriState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.floweytf.customitemapi.datadriven.Utils.tryGetJsonElement;
import static com.floweytf.customitemapi.datadriven.Utils.tryGetString;

public record JsonItemFragment(
    Optional<Component> name,
    Optional<Material> baseItem,
    Optional<MonumentaRarities> rarity,
    Optional<MonumentaRegions> region,
    Optional<MonumentaLocations> location,
    Optional<List<Component>> lore,
    Optional<Class<?>> pluginImpl,
    List<Pair<TaggedItemComponent.TaggedItemComponentInfo, Supplier<TaggedItemComponent>>> tags,
    TriState hasGlint
) {
    public static final String NO_GLINT = "noglint";
    public static final String GLINT = "glint";
    private static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/DataDriven/JSON");

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

    public static @Nullable JsonItemFragment fromTree(NamespacedKey id, JsonObject tree) {
        final var name = tryGetJsonElement(tree, "name")
            .map(GsonComponentSerializer.gson()::deserializeFromTree);
        final var baseItem = tryGetString(tree, "item")
            .map(itemId -> Objects.requireNonNull(Material.matchMaterial(itemId)));
        final var rarity = tryGetString(tree, "rarity")
            .map(String::toUpperCase)
            .map(MonumentaRarities::valueOf);
        final var region = tryGetString(tree, "region")
            .map(String::toUpperCase)
            .map(MonumentaRegions::valueOf);
        final var location = tryGetString(tree, "location")
            .map(String::toUpperCase)
            .map(MonumentaLocations::valueOf);

        final var lore = tryGetJsonElement(tree, "lore")
            .map(item -> item.getAsJsonArray().asList()
                .stream()
                .map(GsonComponentSerializer.gson()::deserializeFromTree)
                .toList()
            );

        final Optional<Class<?>> pluginImpl = tryGetString(tree, "plugin_implementation")
            .map(className -> {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

        // obtain some flags
        var hasGlint = TriState.NOT_SET;
        final var tagInfos = new ArrayList<Pair<TaggedItemComponent.TaggedItemComponentInfo,
            Supplier<TaggedItemComponent>>>();

        final Runnable onDuplicateGlintTag = () ->
            LOGGER.warn("While parsing {} - duplicate glint specifier tag, ignoring. Please fix this in the json file!", id);

        if (tree.has("tags")) {
            for (final var tag : tree.getAsJsonArray("tags")) {
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
                            LOGGER.warn("While parsing {} - unknown tag with id: {}", id, tagId);
                            return null;
                        }

                        tagInfos.add(new Pair<>(tagInfo, tagInfo.supplier().apply(objectTag)));
                    }
                }
            }
        }

        return new JsonItemFragment(name, baseItem, rarity, region, location, lore, pluginImpl, tagInfos, hasGlint);
    }

    public JsonItemFragment merge(JsonItemFragment base) {
        return new JsonItemFragment(
            name.or(base::name),
            baseItem.or(base::baseItem),
            rarity.or(base::rarity),
            region.or(base::region),
            location.or(base::location),
            lore.or(base::lore),
            pluginImpl.or(base::pluginImpl),
            Stream.concat(tags.stream(), base.tags.stream()).toList(),
            base.hasGlint == TriState.NOT_SET ? hasGlint : base.hasGlint
        );
    }

    public boolean isStateless() {
        return pluginImpl.isEmpty() && tags.stream().map(x -> x.first().isStateless()).reduce(false, Boolean::logicalOr);
    }
}