package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.function.Supplier;

public interface CustomItemRegistry {
    static CustomItemRegistry getInstance() {
        return ApiMeta.REGISTRY;
    }

    CustomItemType register(NamespacedKey key, Supplier<CustomItem> custom, Material material);

    CustomItemType registerDefault(CustomItemType custom);
}
