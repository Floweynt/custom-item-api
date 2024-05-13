package com.floweytf.customitemapi.helpers;

import com.floweytf.customitemapi.api.item.CustomItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Optional;

public record CustomItemInstance(CustomItem item, NamespacedKey key, Optional<String> variant, Material baseItem) {
}