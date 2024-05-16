package com.floweytf.customitemapi.impl.item;

import com.floweytf.customitemapi.Lazy;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.api.item.ItemVariantSet;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class CustomItemTypeImpl implements CustomItemType {
    private final Supplier<CustomItemInstance> factory;
    private final NamespacedKey key;
    private final String variant;
    private final Material baseItem;
    private final boolean isStateless;
    private final ItemVariantSetImpl variantSet;

    public CustomItemTypeImpl(
        Supplier<CustomItem> factory,
        NamespacedKey key,
        String variant,
        Material baseItem,
        boolean isStateless,
        ItemVariantSetImpl variantSet
    ) {
        this.key = key;
        this.variant = variant;
        this.variantSet = variantSet;
        this.baseItem = baseItem;
        this.isStateless = isStateless;
        this.factory = convertSupplier(factory, Optional.empty());
    }

    private Supplier<CustomItemInstance> convertSupplier(Supplier<CustomItem> suppler, Optional<String> variant) {
        final Supplier<CustomItemInstance> supplier = () -> new CustomItemInstance(suppler.get(), key, variant, baseItem,
            isStateless);
        if (isStateless)
            return new Lazy<>(supplier);
        return supplier;
    }

    // API implementation
    @Override
    public @NotNull String variantId() {
        return variant;
    }

    @Override
    public @NotNull Material baseItem() {
        return baseItem;
    }

    @Override
    public boolean isStateless() {
        return isStateless;
    }

    @Override
    public @NotNull ItemVariantSet variantSet() {
        return variantSet;
    }

    // Internal getters
    public Supplier<CustomItemInstance> factory() {
        return factory;
    }
}