package com.floweytf.customitemapi.helpers;

import com.floweytf.customitemapi.ModMain;
import com.floweytf.customitemapi.access.ItemStackAccess;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import net.minecraft.world.item.ItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemStackStateManager {
    public static final String KEY_BUILTIN_ID = "id";
    public static final String KEY_SAVE_DATA = "data";
    public static final String ROOT_TAG_KEY = "customitemapi";

    // TODO: instead of storing this, we should store a smarter object with better caching tech
    @Nullable
    protected CustomItemInstance customState = null;

    @Nullable
    public CustomItemInstance getCustomState() {
        return customState;
    }

    public void recomputeDisplay(ItemStack stack) {
        try {
            if (customState == null)
                return;

            customState.apply(stack);

            ((ItemStackAccess) (Object) stack).custom_item_api$setItemRaw(CraftMagicNumbers.getItem(customState.baseItem()));
        } catch (Throwable e) {
            ModMain.LOGGER.error("Failed to initialize item: ", e);
        }
    }

    public void storeCustomState(ItemStack stack) {
        if (customState == null)
            return;

        // nuke previous state
        stack.getOrCreateTag().remove(ROOT_TAG_KEY);

        // first, we need to sync ID
        // TODO: give the custom stack a chance to save it's own state
        final var rootTag = stack.getOrCreateTagElement(ROOT_TAG_KEY);
        rootTag.putString(KEY_BUILTIN_ID, customState.key().asString());
    }

    public void loadCustomState(ItemStack stack) {
        final var rootTag = stack.getOrCreateTagElement(ROOT_TAG_KEY);

        // Vanilla item, check to see if there is a default
        if (!rootTag.contains(KEY_BUILTIN_ID)) {
            customState = CustomItemRegistryImpl.getInstance().create(stack.getItem());
        } else {
            final var id = rootTag.getString(KEY_BUILTIN_ID);
            final var parts = id.split("@", 2);
            customState = CustomItemRegistryImpl.getInstance().create(NamespacedKey.fromString(parts[0]), parts.length == 1 ? Optional.empty() : Optional.of(parts[1]));
        }

        if (customState == null)
            return;

        // TODO: give custom state a change to load
        storeCustomState(stack);
        recomputeDisplay(stack);
    }

}