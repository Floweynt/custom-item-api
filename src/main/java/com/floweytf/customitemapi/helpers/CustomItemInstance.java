package com.floweytf.customitemapi.helpers;

import com.floweytf.customitemapi.api.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public record CustomItemInstance(CustomItem item, NamespacedKey key, Material baseItem) {
}