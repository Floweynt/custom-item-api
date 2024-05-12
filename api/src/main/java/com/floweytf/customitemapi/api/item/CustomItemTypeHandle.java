package com.floweytf.customitemapi.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.function.Supplier;

public interface CustomItemTypeHandle {
    Supplier<CustomItem> factory();
    NamespacedKey key();
    Material baseItem();
}