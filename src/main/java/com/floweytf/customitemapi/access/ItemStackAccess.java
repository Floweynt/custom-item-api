package com.floweytf.customitemapi.access;

import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import net.minecraft.world.item.Item;

public interface ItemStackAccess {
    ItemStackStateManager getStateManager();

    void setItemRaw(Item item);
}
