package com.floweytf.customitemapi.access;

import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import net.minecraft.world.item.Item;

public interface ItemStackAccess {
    ItemStackStateManager custom_item_api$getStateManager();

    void custom_item_api$setItemRaw(Item item);
}
