package com.floweytf.customitemapi.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;

/**
 * A set of custom items, with a default state.
 * Each custom item is associated with a key ({@link CustomItemType#variantId()}), which may be an empty string.
 * Each variant must be unique to a variant set.
 * As an analogy:
 * {@link CustomItemType} is like a {@link org.bukkit.block.BlockState}
 * {@link ItemVariantSet} is like a {@link Material}
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ItemVariantSet {
    /**
     * Gets the default variant of the variant set.
     *
     * @return The default variant.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    CustomItemType defaultVariant();

    /**
     * Registers a new variant for this variant set.
     * This is a registration method, see {@link com.floweytf.customitemapi.api.CustomItemRegistry}
     * The name must be distinct from previously registered variants in this variant set.
     *
     * @param variantId   The name of this variant, may be an empty string.
     * @param factory     A function to create instances.
     * @param baseItem    The base item for this custom item.
     * @param isStateless See {@link CustomItemType#isStateless()}
     * @return The registered type.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    CustomItemType register(@NotNull String variantId, @NotNull Supplier<CustomItem> factory, @NotNull Material baseItem,
                            boolean isStateless);

    /**
     * Sets the default variant for this variant set.
     * This is a registration method, see {@link com.floweytf.customitemapi.api.CustomItemRegistry}
     *
     * @param type The new default variant. This variant must belong to this variant set.
     * @author Floweynt
     * @since 1.0.0
     */
    void setDefaultVariant(@NotNull CustomItemType type);

    /**
     * Returns the key provided to {@link com.floweytf.customitemapi.api.CustomItemRegistry#defineVariant(NamespacedKey)}
     *
     * @return The key that this variant set is registered with.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    NamespacedKey key();

    /**
     * The map from variant id to variant of the variants registered with this variant set.
     *
     * @return A readonly map.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Map<String, CustomItemType> variants();
}