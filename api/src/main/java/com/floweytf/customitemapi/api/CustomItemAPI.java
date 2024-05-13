package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.resource.DatapackResourceLoader;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The main entrypoint into this library.
 *
 * @author Floweynt
 * @since 1.0.0
 */
public interface CustomItemAPI {
    /**
     * Obtains an instance of the API.
     *
     * @return The API.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static CustomItemAPI getInstance() {
        return ApiMeta.API;
    }

    /**
     * Obtains the current implementation version.
     *
     * @return The implementation version.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static Version implVersion() {
        return ApiMeta.IMPL_VERSION;
    }

    /**
     * Registers a datapack resource loader under a specific prefix.
     *
     * @param prefix The prefix for resources. The actual prefix (in datapack file) would be [ns]/plugin/[prefix].
     * @param loader The datapack loader instance, to which load event will be passed.
     * @author Floweynt
     * @since 1.0.0
     */
    void addDatapackLoader(@NotNull String prefix, @NotNull DatapackResourceLoader loader);

    /**
     * Obtains the {@link CustomItem} associated with an {@link ItemStack}.
     *
     * @param stack The stack.
     * @return The associated {@link ItemStack} or null if absent.
     * @author Floweynt
     * @since 1.0.0
     */
    @Nullable
    CustomItem getCustomItem(@NotNull ItemStack stack);

    /**
     * Forces re-computation of {@link ItemStack} properties.
     *
     * @param stack The stack to force update.
     * @author Floweynt
     * @since 1.0.0
     */
    void forceUpdate(@NotNull ItemStack stack);
}