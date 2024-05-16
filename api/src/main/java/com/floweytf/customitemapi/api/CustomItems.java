package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.item.CustomItemType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The main entrypoint into this library.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface CustomItems {
    /**
     * Obtains an instance of the API.
     *
     * @return The API.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static CustomItems getInstance() {
        return CustomItemAPI.getInstance().getCustomItemsInstance();
    }

    @NotNull
    ItemStack create(@NotNull CustomItemType type, int count);

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