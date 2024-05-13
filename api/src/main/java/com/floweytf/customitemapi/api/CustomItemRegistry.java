package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemTypeHandle;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface CustomItemRegistry {
    static CustomItemRegistry getInstance() {
        return ApiMeta.REGISTRY;
    }

    // registration methods

    CustomItemTypeHandle register(NamespacedKey key, Supplier<CustomItem> factory, Material material, boolean isStateless);

    void registerVariant(CustomItemTypeHandle custom, String key, Supplier<CustomItem> factory);

    void registerDefault(CustomItemTypeHandle custom);

    // access methods

    Set<NamespacedKey> keys();

    Collection<CustomItemTypeHandle> values();

    Set<Map.Entry<NamespacedKey, CustomItemTypeHandle>> entries();

    Collection<CustomItemTypeHandle> defaultRegistrations();
}
