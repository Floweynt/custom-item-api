package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemTypeHandle;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomItemTypeHandleImpl implements CustomItemTypeHandle {
    private final Supplier<CustomItem> factory;
    private final NamespacedKey key;
    private final Material baseItem;
    private final Map<String, Supplier<CustomItem>> variants = new HashMap<>();

    public CustomItemTypeHandleImpl(Supplier<CustomItem> factory, NamespacedKey key, Material baseItem) {
        this.factory = factory;
        this.key = key;
        this.baseItem = baseItem;
    }

    @Override
    public @NotNull Supplier<CustomItem> factory() {
        return factory;
    }

    @Override
    public @NotNull NamespacedKey key() {
        return key;
    }

    @Override
    public @NotNull Material baseItem() {
        return baseItem;
    }

    @Override
    public Map<String, Supplier<CustomItem>> variants() {
        return Collections.unmodifiableMap(variants);
    }

    public Map<String, Supplier<CustomItem>> getVariantsMutable() {
        return variants;
    }
}