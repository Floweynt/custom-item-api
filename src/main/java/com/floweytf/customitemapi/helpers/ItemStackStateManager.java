package com.floweytf.customitemapi.helpers;

import com.floweytf.customitemapi.CustomItemAPIMain;
import com.floweytf.customitemapi.access.ItemStackAccess;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import com.floweytf.customitemapi.impl.item.CustomItemTypeImpl;
import net.minecraft.world.item.ItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.Nullable;

public class ItemStackStateManager {
    public static final String KEY_ID = "id";
    public static final String KEY_VARIANT = "variant";
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
            CustomItemAPIMain.LOGGER.error("Failed to initialize item: ", e);
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
        rootTag.putString(KEY_ID, customState.key().asString());
    }

    public void loadCustomState(ItemStack stack) {
        final var rootTag = stack.getOrCreateTagElement(ROOT_TAG_KEY);

        // Vanilla item, check to see if there is a default
        if (!rootTag.contains(KEY_ID)) {
            customState = CustomItemRegistryImpl.getInstance().create(stack.getItem());
        } else {
            final var id = NamespacedKey.fromString(rootTag.getString(KEY_ID));

            if (id == null) {
                throw new IllegalStateException("Failed to parse id");
            }

            final var variants = CustomItemRegistryImpl.getInstance().get(id);

            if (variants == null) {
                throw new IllegalStateException("Unknown variantSet with id " + id);
            }

            final var type = rootTag.contains(KEY_VARIANT) ?
                variants.variants().get(rootTag.getString(KEY_VARIANT)) :
                variants.defaultVariant();

            if (type == null) {
                throw new IllegalStateException("Unknown variant");
            }

            customState = ((CustomItemTypeImpl) type).factory().get();
        }

        if (customState == null)
            return;

        // TODO: give custom state a change to load
        storeCustomState(stack);
        recomputeDisplay(stack);
    }
}