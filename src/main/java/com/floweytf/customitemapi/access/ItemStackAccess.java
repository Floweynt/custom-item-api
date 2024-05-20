package com.floweytf.customitemapi.access;

import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import net.minecraft.world.item.Item;

/**
 * Interface implemented by ItemStack in order to access the implementation.
 */
public interface ItemStackAccess {
    /**
     * Obtains the contained state manager.
     * @return The state manager bound to an ItemStack.
     */
    ItemStackStateManager custom_item_api$getStateManager();

    /**
     * Sets the item raw. This is needed because setItem throws.
     * @param item The item to set.
     */
    void custom_item_api$setItemRaw(Item item);
}
