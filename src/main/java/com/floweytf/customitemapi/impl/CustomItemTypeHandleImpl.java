package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.Lazy;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemTypeHandle;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CustomItemTypeHandleImpl implements CustomItemTypeHandle {
    private Supplier<CustomItemInstance> convertSupplier(Supplier<CustomItem> suppler, Optional<String> variant) {
        final Supplier<CustomItemInstance> supplier = () -> new CustomItemInstance(suppler.get(), key, variant, baseItem, isStateless);
        if(isStateless)
            return new Lazy<>(supplier);
        return supplier;
    }

    private final Supplier<CustomItemInstance> factory;
    private final NamespacedKey key;
    private final Material baseItem;
    private final boolean isStateless;
    private final Map<String, Supplier<CustomItemInstance>> variants = new HashMap<>();

    public CustomItemTypeHandleImpl(Supplier<CustomItem> factory, NamespacedKey key, Material baseItem, boolean isStateless) {
        this.key = key;
        this.baseItem = baseItem;
        this.isStateless = isStateless;
        this.factory = convertSupplier(factory, Optional.empty());
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
    public Set<String> variants() {
        return variants.keySet();
    }

    public Supplier<CustomItemInstance> factory() {
        return factory;
    }
}