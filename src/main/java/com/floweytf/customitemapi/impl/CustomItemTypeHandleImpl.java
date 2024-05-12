package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemTypeHandle;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.function.Supplier;

public record CustomItemTypeHandleImpl(Supplier<CustomItem> factory, NamespacedKey key, Material baseItem) implements
    CustomItemTypeHandle {
}