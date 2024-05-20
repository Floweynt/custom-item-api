package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.datadriven.Pair;
import com.floweytf.customitemapi.datadriven.PluginMain;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentRegistry;
import com.floweytf.customitemapi.datadriven.registry.MonumentaLocations;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRarities;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRegions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    ImmutableMap<String, ImmutableList<TaggedComponentConfig<?>>> componentConfigurations
) {
    private static String getTagId(JsonElement element) {
        if (element.isJsonPrimitive())
            return element.getAsString();

        return element.getAsJsonObject().get("tag").getAsString();
    }

    private static ImmutableMap<String, ImmutableList<TaggedComponentConfig<?>>> toImmutable(Map<String, List<TaggedComponentConfig<?>>> mutable) {
        return mutable.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), ImmutableList.copyOf(entry.getValue())))
            .collect(ImmutableMap.toImmutableMap(Pair::first, Pair::second));
    }

    private static Map<String, List<TaggedComponentConfig<?>>> toMutable(ImmutableMap<String, ImmutableList<TaggedComponentConfig<?>>> immutable) {
        return immutable.entrySet().stream()
            .map(entry -> new Pair<>(entry.getKey(), new ArrayList<>(entry.getValue())))
            .collect(Collectors.toMap(Pair::first, Pair::second));
    }

    public static @NotNull JsonItemFragment fromTree(NamespacedKey id, JsonObject tree) {
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
        final var componentConfigurations = new HashMap<String, List<TaggedComponentConfig<?>>>();

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

                final var handler = TaggedComponentRegistry.TAGS.get(tagId);

                if (handler == null) {
                    PluginMain.LOGGER.warn("While parsing {} - unknown tag with id {}, skipping it!", id, tagId);
                    continue;
                }

                // parse stuff
                componentConfigurations.computeIfAbsent(tagId, ignored -> new ArrayList<>())
                    .add(handler.parser().apply(objectTag));
            }
        }

        return new JsonItemFragment(
            name, baseItem, rarity, region, location, lore, pluginImpl,
            toImmutable(componentConfigurations)
        ).tagMerge();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JsonItemFragment tagMerge() {
        final var newConfigurations = ImmutableMap.<String, ImmutableList<TaggedComponentConfig<?>>>builder();

        // configuration merging
        componentConfigurations.forEach((key, value) -> {
            final var configs = new ArrayList<>(value);

            for (int i = 0; i < configs.size(); i++) {
                var config = configs.get(i);

                for (int j = i + 1; j < configs.size(); j++) {
                    final var mergeResult = ((TaggedComponentConfig) config).tryMerge(configs.get(j));
                    if (mergeResult != null) {
                        config = mergeResult;
                        configs.remove(j);
                        j--;
                    }
                }

                configs.set(i, config);
            }

            newConfigurations.put(key, ImmutableList.copyOf(configs));
        });

        return new JsonItemFragment(name, baseItem, rarity, region, location, lore, pluginImpl, newConfigurations.build());
    }

    public JsonItemFragment merge(JsonItemFragment base) {
        final var resultComponentMap = toMutable(base.componentConfigurations);

        componentConfigurations.forEach((key, value) -> resultComponentMap.computeIfAbsent(key,
            ignored -> new ArrayList<>()).addAll(value));

        return new JsonItemFragment(
            name.or(base::name),
            baseItem.or(base::baseItem),
            rarity.or(base::rarity),
            region.or(base::region),
            location.or(base::location),
            lore.or(base::lore),
            pluginImpl.or(base::pluginImpl),
            toImmutable(resultComponentMap)
        ).tagMerge();
    }

    public boolean isStateless() {
        return pluginImpl.isEmpty() && componentConfigurations.entrySet()
            .stream()
            .flatMap(entry -> entry.getValue().stream())
            .map(TaggedComponentConfig::isStateless)
            .reduce(true, Boolean::logicalAnd);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Supplier<TaggedComponent>> getComponentSuppliers() {
        return componentConfigurations.entrySet()
            .stream()
            .flatMap(entry -> {
                final var factory = TaggedComponentRegistry.TAGS.get(entry.getKey()).factory();
                return entry.getValue()
                    .stream()
                    .<Supplier<TaggedComponent>>map(config -> () -> (TaggedComponent) ((Function) factory).apply(config));
            })
            .toList();
    }
}