package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import com.google.common.base.Preconditions;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomItemRegistryImpl implements CustomItemRegistry {
    private final static CustomItemRegistryImpl INSTANCE = new CustomItemRegistryImpl();
    private final Map<NamespacedKey, CustomItemType> registry = new HashMap<>();
    private final Map<Material, CustomItemType> defaultRegistry = new EnumMap<>(Material.class);
    private boolean isFrozen = false;

    private CustomItemRegistryImpl() {
    }

    public static CustomItemRegistryImpl getInstance() {
        return INSTANCE;
    }

    private void onMutate() {
        if (isFrozen)
            throw new IllegalStateException("registry frozen");
    }

    @Override
    public CustomItemType register(NamespacedKey key, Supplier<CustomItem> custom, Material material) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(custom);
        Preconditions.checkNotNull(material);

        onMutate();

        if (registry.containsKey(key)) {
            throw new IllegalArgumentException("duplicate key " + key.asString());
        }

        final var type = new CustomItemType(custom, key, material);
        registry.put(key, type);
        return type;
    }

    @Override
    public CustomItemType registerDefault(CustomItemType custom) {
        Preconditions.checkNotNull(custom);

        onMutate();

        if (defaultRegistry.containsKey(custom.baseItem())) {
            throw new IllegalArgumentException("duplicate key " + custom.baseItem().toString());
        }

        defaultRegistry.put(custom.baseItem(), custom);
        return custom;
    }

    @Nullable
    public CustomItemType get(NamespacedKey key) {
        return registry.get(key);
    }

    public @Nullable CustomItemInstance create(NamespacedKey key) {
        final var value = registry.get(key);
        if (value == null)
            return null;

        return new CustomItemInstance(registry.get(key).factory().get(), value.key(), value.baseItem());
    }

    public @Nullable CustomItemInstance create(Item key) {
        final var value = defaultRegistry.get(CraftItemType.minecraftToBukkit(key));
        if (value == null)
            return null;
        return new CustomItemInstance(value.factory().get(), value.key(), value.baseItem());
    }

    public void freeze() {
        isFrozen = true;
    }
}