package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.resource.DatapackResourceLoader;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface CustomItemAPI {
    static CustomItemAPI getInstance() {
        return ApiMeta.API;
    }

    void addDatapackLoader(String key, DatapackResourceLoader loader);

    @Nullable
    CustomItem getCustomItem(ItemStack stack);

    @Nullable
    NamespacedKey getCustomItemId(ItemStack stack);

    void forceUpdate(ItemStack stack);
}
