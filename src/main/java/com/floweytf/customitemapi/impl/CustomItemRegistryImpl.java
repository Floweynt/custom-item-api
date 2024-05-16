package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.CustomItemAPIMain;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.api.item.ItemVariantSet;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import com.floweytf.customitemapi.impl.item.CustomItemTypeImpl;
import com.floweytf.customitemapi.impl.item.ItemVariantSetImpl;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class CustomItemRegistryImpl implements CustomItemRegistry {
    private final static CustomItemRegistryImpl INSTANCE = new CustomItemRegistryImpl();
    private final Map<NamespacedKey, ItemVariantSet> registry = new HashMap<>();
    private final Map<Material, CustomItemType> defaultRegistry = new EnumMap<>(Material.class);
    private boolean isFrozen = false;
    private Set<ResourceLocation> minecraftKeys = null;

    private CustomItemRegistryImpl() {
    }

    public static CustomItemRegistryImpl getInstance() {
        return INSTANCE;
    }

    public static void onMutate() {
        if (getInstance().isFrozen)
            throw new IllegalStateException("registry frozen");
    }

    @Override
    public @NotNull ItemVariantSet defineVariant(@NotNull NamespacedKey variantKey) {
        Preconditions.checkNotNull(variantKey);

        onMutate();

        if (registry.containsKey(variantKey)) {
            throw new IllegalArgumentException("duplicate key " + variantKey.asString());
        }

        final var inst = new ItemVariantSetImpl(variantKey);
        registry.put(variantKey, inst);
        return inst;
    }

    @Override
    public @NotNull Set<NamespacedKey> keys() {
        return Collections.unmodifiableSet(registry.keySet());
    }

    @Override
    public @NotNull Set<Map.Entry<NamespacedKey, ItemVariantSet>> entries() {
        return Collections.unmodifiableSet(registry.entrySet());
    }

    public void freeze() {
        isFrozen = true;

        // validate variants have defaults
        registry.entrySet().removeIf(entry -> {
            if (((ItemVariantSetImpl) entry.getValue()).isInvalid()) {
                CustomItemAPIMain.LOGGER.warn("Registered item variant set {} is invalid", entry.getKey());
                return true;
            }

            return false;
        });

        CustomItemAPIMain.LOGGER.info("Loaded {} items and {} defaults", registry.size(), defaultRegistry.size());
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

    // getter methods
    public @Nullable ItemVariantSetImpl get(NamespacedKey id) {
        return (ItemVariantSetImpl) registry.get(id);
    }

    public CustomItemInstance create(Item item) {
        final var type = defaultRegistry.get(CustomItemAPIMain.materialFromItem(item));
        if (type == null) {
            return null;
        }

        return ((CustomItemTypeImpl) type).factory().get();
    }
}