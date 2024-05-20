package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.Lazy;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponent;
import com.floweytf.customitemapi.datadriven.registry.MonumentaLocations;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRarities;
import com.floweytf.customitemapi.datadriven.registry.MonumentaRegions;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class StatelessJsonCustomItem implements CustomItem {
    private final Component name;
    private final List<Component> lore;
    private final Optional<MonumentaRarities> rarity;
    private final Optional<MonumentaRegions> region;
    private final Optional<MonumentaLocations> location;
    private final Optional<Class<?>> pluginImpl; // TODO: implement this
    private final List<TaggedComponent> components;

    private final Supplier<List<Component>> loreSupplier;
    private final Supplier<Component> titleSupplier;

    StatelessJsonCustomItem(JsonItemFragment fragment) {
        this.name = fragment.name().orElseThrow();
        this.lore = fragment.lore().orElseThrow();
        this.rarity = fragment.rarity();
        this.region = fragment.region();
        this.location = fragment.location();
        this.pluginImpl = fragment.pluginImpl();
        this.components = fragment.getComponentSuppliers().stream().map(Supplier::get).toList();

        loreSupplier = new Lazy<>(this::renderLore);
        titleSupplier = new Lazy<>(this::renderTitle);
    }

    private Component renderTitle() {
        return rarity.map(value -> name.applyFallbackStyle(value.getNameFormat())).orElse(name);
    }

    private List<Component> renderLore() {
        final var computedLore = new ArrayList<Component>();
        final ComponentWriter loreAppender =
            text -> computedLore.add(text.applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE));

        for (final var taggedComponent : components) {
            taggedComponent.putComponentsStart(loreAppender);
        }

        if (rarity.isPresent() && region.isPresent()) {
            computedLore.add(
                Component.text(region.get().getName() + " : ")
                    .applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE)
                    .append(rarity.get().getText())
            );
        }

        location.ifPresent(value -> computedLore.add(value.getText().applyFallbackStyle(JsonCustomItem.NO_ITALIC)));

        this.lore.stream().map(u -> u.applyFallbackStyle(JsonCustomItem.DEFAULT_LORE_STYLE)).forEach(computedLore::add);

        for (final var component : components) {
            component.putComponentsEnd(loreAppender);
        }

        return computedLore;
    }

    @Override
    public @NotNull Optional<Component> getTitle() {
        return Optional.of(titleSupplier.get());
    }

    @Override
    public @NotNull Optional<List<Component>> getLore() {
        return Optional.of(loreSupplier.get());
    }

    @Override
    public @NotNull List<ItemFlag> hideFlags() {
        return List.of(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
    }

    @Override
    public void configureExtra(@NotNull ExtraItemData extraData) {
        for (final var component : components) {
            component.configure(extraData);
        }
    }
}