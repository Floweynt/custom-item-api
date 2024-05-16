package com.floweytf.customitemapi.api.item;

import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A mostly opaque handle representing the registration info of a {@link CustomItem}.
 * This object describes the "shape" of a CustomItem, with type-properties such as {@link CustomItemType#isStateless()}.
 * Instances is obtained by registering custom items with
 * {@link ItemVariantSet#register(String, Supplier, Material, boolean)}.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface CustomItemType {
    /**
     * The item type that this {@link CustomItem} is based on.
     *
     * @return The {@link Material}.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Material baseItem();

    /**
     * Returns whether this custom item is stateless.
     * For a stateless CustomItem, the supplier's result may be cached.
     * The result of various rendering calls such as {@link CustomItem#getLore()} may also be cached.
     *
     * @return Whether this custom item is stateless.
     * @author Floweynt
     * @since 1.0.0
     */
    boolean isStateless();

    /**
     * The variant set that this custom item type belongs to. See {@link ItemVariantSet}
     *
     * @return The variant set that this custom item type belongs to.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    ItemVariantSet variantSet();

    /**
     * The variant id that this custom item type was registered with
     * ({@link ItemVariantSet#register(String, Supplier, Material, boolean)}
     *
     * @return The variant id. This may be an empty string.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    String variantId();
}