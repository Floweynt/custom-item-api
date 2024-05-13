package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.ModMain;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemTypeHandle;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CustomItemRegistryImpl implements CustomItemRegistry {
    private final static CustomItemRegistryImpl INSTANCE = new CustomItemRegistryImpl();
    private final Map<NamespacedKey, CustomItemTypeHandle> registry = new HashMap<>();
    private final Map<Material, CustomItemTypeHandle> defaultRegistry = new EnumMap<>(Material.class);
    private boolean isFrozen = false;
    private Set<ResourceLocation> minecraftKeys = null;

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
    public CustomItemTypeHandle register(NamespacedKey key, Supplier<CustomItem> factory, Material material) {
        Preconditions.checkNotNull(key);
        Preconditions.checkNotNull(factory);
        Preconditions.checkNotNull(material);

        onMutate();

        if (registry.containsKey(key)) {
            throw new IllegalArgumentException("duplicate key " + key.asString());
        }

        final var type = new CustomItemTypeHandleImpl(factory, key, material);
        registry.put(key, type);
        return type;
    }

    @Override
    public void registerVariant(CustomItemTypeHandle custom, String key, Supplier<CustomItem> factory) {
        final var map = ((CustomItemTypeHandleImpl) custom).getVariantsMutable();
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("duplicate key");
        }

        map.put(key, factory);
    }

    public void registerDefault(CustomItemTypeHandle custom) {
        Preconditions.checkNotNull(custom);

        onMutate();

        if (defaultRegistry.containsKey(custom.baseItem())) {
            throw new IllegalArgumentException("duplicate key " + custom.baseItem());
        }

        defaultRegistry.put(custom.baseItem(), custom);
    }

    @Override
    public Set<NamespacedKey> keys() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    @Override
    public Collection<CustomItemTypeHandle> values() {
        return Collections.unmodifiableCollection(registry.values());
    }

    @Override
    public Set<Map.Entry<NamespacedKey, CustomItemTypeHandle>> entries() {
        return Collections.unmodifiableSet(registry.entrySet());
    }

    @Override
    public Collection<CustomItemTypeHandle> defaultRegistrations() {
        return Collections.unmodifiableCollection(defaultRegistry.values());
    }

    @Nullable
    public CustomItemTypeHandle get(NamespacedKey key) {
        return registry.get(key);
    }

    public @Nullable CustomItemInstance create(NamespacedKey key, Optional<String> variant) {
        final var value = registry.get(key);
        if (value == null)
            return null;

        return new CustomItemInstance(registry.get(key).factory().get(), value.key(), variant, value.baseItem());
    }

    public @Nullable CustomItemInstance create(Item key) {
        final var value = defaultRegistry.get(ModMain.materialFromItem(key));
        if (value == null)
            return null;
        return new CustomItemInstance(value.factory().get(), value.key(), Optional.empty(), value.baseItem());
    }

    public void freeze() {
        isFrozen = true;
        ModMain.LOGGER.info("Loaded {} items and {} defaults", registry.size(), defaultRegistry.size());
    }

    public Set<ResourceLocation> minecraftKeys() {
        if (!isFrozen) {
            throw new IllegalStateException("cannot call minecraftKeys() before registry is frozen");
        }
        if (minecraftKeys == null) {
            minecraftKeys =
                CustomItemRegistryImpl.getInstance().keys().stream().map(CraftNamespacedKey::toMinecraft).collect(Collectors.toUnmodifiableSet());
        }
        return minecraftKeys;
    }
}