package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.CustomItemAPIMain;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.api.item.ItemVariantSet;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import com.floweytf.customitemapi.impl.item.CustomItemTypeImpl;
import com.floweytf.customitemapi.impl.item.ItemVariantSetImpl;
import com.google.common.base.Preconditions;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class CustomItemRegistryImpl implements CustomItemRegistry {
    private final static CustomItemRegistryImpl INSTANCE = new CustomItemRegistryImpl();
    private final Map<NamespacedKey, ItemVariantSet> registry = new HashMap<>();
    private final Map<Material, CustomItemType> defaultRegistry = new EnumMap<>(Material.class);
    private final List<String> giveCompletion = new ArrayList<>();
    private boolean isFrozen = false;

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
    public void registerAsDefault(CustomItemType type) {
        defaultRegistry.put(type.baseItem(), type);
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

        // compute stuff
        registry.forEach((id, value) -> {
            giveCompletion.add("\"" + id.toString() + "\"");
            for (final var variantId : value.variants().keySet()) {
                giveCompletion.add("\"" + id + "[" + variantId + "]\"");
            }
        });

        CustomItemAPIMain.LOGGER.info("Loaded {} items and {} defaults", registry.size(), defaultRegistry.size());
    }

    // getter methods
    public @Nullable ItemVariantSetImpl get(NamespacedKey id) {
        return (ItemVariantSetImpl) registry.get(id);
    }

    public List<String> getGiveCompletion() {
        return giveCompletion;
    }

    public CustomItemInstance create(Item item) {
        final var type = defaultRegistry.get(CustomItemAPIMain.materialFromItem(item));
        if (type == null) {
            return null;
        }

        return ((CustomItemTypeImpl) type).factory().get();
    }
}