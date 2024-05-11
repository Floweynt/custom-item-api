package com.floweytf.customitemapi.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.function.Supplier;

public record CustomItemType(Supplier<CustomItem> factory, NamespacedKey key, Material baseItem) {
}