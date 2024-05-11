package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.datadriven.Lazy;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.datadriven.json.tags.TaggedItemComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StatelessJsonCustomItem implements CustomItem {
    private final Component name;
    private final List<Component> lore;
    private final Optional<JsonCustomItem.Rarity> rarity;
    private final Optional<JsonCustomItem.Region> region;
    private final Optional<JsonCustomItem.Location> location;
    private final Optional<Class<?>> pluginImpl; // TODO: implement this
    private final TriState hasGlint;
    private final List<TaggedItemComponent> components;

    private final Supplier<List<Component>> loreSupplier;
    private final Supplier<Component> titleSupplier;

    StatelessJsonCustomItem(
        Component name, List<Component> lore,
        Optional<JsonCustomItem.Rarity> rarity, Optional<JsonCustomItem.Region> region,
        Optional<JsonCustomItem.Location> location,
        Optional<Class<?>> pluginImpl,
        TriState hasGlint, List<TaggedItemComponent> components
    ) {
        this.name = name;
        this.lore = lore;
        this.rarity = rarity;
        this.region = region;
        this.location = location;
        this.pluginImpl = pluginImpl;
        this.hasGlint = hasGlint;
        this.components = components;

        loreSupplier = new Lazy<>(this::renderLore);
        titleSupplier = new Lazy<>(this::renderTitle);
    }

    private Component renderTitle() {
        return rarity.map(value -> name.applyFallbackStyle(value.getNameFormat())).orElse(name);
    }

    private List<Component> renderLore() {
        final var computedLore = new ArrayList<Component>();
        final Consumer<Component> loreAppender =
            text -> computedLore.add(text.applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE));

        components.forEach(component -> component.putComponentsStart(loreAppender));

        if (rarity.isPresent() && region.isPresent()) {
            computedLore.add(
                Component.text(region.get().getName() + " : ")
                    .applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE)
                    .append(rarity.get().getText())
            );
        }

        location.ifPresent(value -> computedLore.add(value.getText().applyFallbackStyle(JsonCustomItem.NO_ITALIC)));

        this.lore.stream().map(u -> u.applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE)).forEach(computedLore::add);

        components.forEach(component -> component.putComponentsEnd(loreAppender));

        return computedLore;
    }

    @Override
    public Optional<Component> getTitle() {
        return Optional.of(titleSupplier.get());
    }

    @Override
    public Optional<List<Component>> getLore() {
        return Optional.of(loreSupplier.get());
    }

    @Override
    public List<ItemFlag> hideFlags() {
        return List.of(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
    }
}