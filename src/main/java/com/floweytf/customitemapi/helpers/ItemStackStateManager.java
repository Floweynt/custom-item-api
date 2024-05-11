package com.floweytf.customitemapi.helpers;

import com.floweytf.customitemapi.CustomItemAPI;
import com.floweytf.customitemapi.access.ItemStackAccess;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftMagicNumbers;
import org.jetbrains.annotations.Nullable;

public class ItemStackStateManager {
    public static final String KEY_BUILTIN_ID = "id";
    public static final String KEY_SAVE_DATA = "data";
    public static final String ROOT_TAG_KEY = "customitemapi";

    // TODO: instead of storing this, we should store a smarter object with better caching tech
    @Nullable
    private CustomItemInstance customState = null;

    @Nullable
    public CustomItemInstance getCustomState() {
        return customState;
    }

    public void recomputeDisplay(ItemStack stack) {
        try {
            if (customState == null)
                return;

            customState.item().getTitle()
                .map(x -> x.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .ifPresent(component -> stack.getOrCreateTagElement("display").putString(
                    "Name", JSONComponentSerializer.json().serialize(component)
                ));

            customState.item().getLore()
                .ifPresent(component -> {
                    var tag = stack.getOrCreateTagElement("display");
                    var lore = new ListTag();
                    component.forEach(x -> lore.add(StringTag.valueOf(JSONComponentSerializer.json().serialize(x))));
                    tag.put("Lore", lore);
                });

            final var hideFlags = customState.item().hideFlags().stream()
                .map(flag -> 1 << flag.ordinal())
                .reduce(0, (a, b) -> a | b);

            stack.getOrCreateTag().putInt("HideFlags", hideFlags);

            ((ItemStackAccess) (Object) stack).setItemRaw(CraftMagicNumbers.getItem(customState.baseItem()));
        } catch (Throwable e) {
            CustomItemAPI.LOGGER.error("Failed to initialize item: ", e);
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
            customState =
                CustomItemRegistryImpl.getInstance().create(NamespacedKey.fromString(rootTag.getString(KEY_BUILTIN_ID)));
        }

        if (customState == null)
            return;

        // TODO: give custom state a change to load
        storeCustomState(stack);
        recomputeDisplay(stack);
    }
}