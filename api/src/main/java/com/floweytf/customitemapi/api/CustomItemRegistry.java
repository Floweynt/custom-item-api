package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.api.item.ItemVariantSet;
import com.google.common.base.Preconditions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The main registry class for registering things related to custom items.
 * Registration methods must be executed relatively early into the plugin loading process, before the registry is frozen.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface CustomItemRegistry {
    /**
     * Obtains an instance of the API.
     *
     * @return The API.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static CustomItemRegistry getInstance() {
        return CustomItemAPI.getInstance().getRegistryInstance();
    }

    /**
     * Registers a variant set.
     *
     * @param variantKey The key to register. Must be unique.
     * @return The registered variant set, which may be used to register variants.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    ItemVariantSet defineVariant(@NotNull NamespacedKey variantKey);

    /**
     * Registers a variant set, and immediately registers a default variant with empty name.
     *
     * @param variantKey   The key to register the variant.
     * @param factory      A function to create instances of custom item.
     * @param baseMaterial The base item.
     * @param isStateless  Whether the item is stateless. See {@link CustomItemType#isStateless()}
     * @return The newly registered variant set. Call {@link ItemVariantSet#defaultVariant()} to get the variant registered.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    default ItemVariantSet registerSimple(@NotNull NamespacedKey variantKey,
                                          @NotNull Supplier<CustomItem> factory,
                                          @NotNull Material baseMaterial,
                                          boolean isStateless) {
        Preconditions.checkNotNull(variantKey);
        Preconditions.checkNotNull(factory);
        Preconditions.checkNotNull(baseMaterial);

        final var variant = defineVariant(variantKey);
        variant.setDefaultVariant(variant.register("", factory, baseMaterial, isStateless));
        return variant;
    }

    /**
     * Registers an item variant as a default type.
     *
     * @param type The item variant to register
     */
    void registerAsDefault(CustomItemType type);

    /**
     * Obtains the set of all registered keys.
     *
     * @return A readonly set of all keys.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Set<NamespacedKey> keys();

    /**
     * The set of all registered variants sets.
     *
     * @return A readonly map of all variant set.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Set<Map.Entry<NamespacedKey, ItemVariantSet>> entries();
}