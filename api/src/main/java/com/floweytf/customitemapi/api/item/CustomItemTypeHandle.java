package com.floweytf.customitemapi.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A mostly opaque handle representing the registration info of a {@link CustomItem}.
 *
 * @author Floweynt
 * @since 1.0.0
 */
public interface CustomItemTypeHandle {
    /**
     * The key that this item was registered with.
     *
     * @return The {@link NamespacedKey}.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    NamespacedKey key();

    /**
     * The item type that this {@link CustomItem} is based on.
     *
     * @return The {@link Material}.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Material baseItem();

    Set<String> variants();
}