package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.Pair;
import com.floweytf.customitemapi.access.ItemStackAccess;
import com.floweytf.customitemapi.api.CustomItemAPI;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.api.resource.DatapackResourceLoader;
import com.floweytf.customitemapi.helpers.CustomItemInstance;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.floweytf.customitemapi.Utils.m;

public class CustomItemAPIImpl implements CustomItemAPI {
    private static final CustomItemAPIImpl INSTANCE = new CustomItemAPIImpl();
    public final List<Pair<String, DatapackResourceLoader>> loaders = new ArrayList<>();

    private CustomItemAPIImpl() {

    }

    public static CustomItemAPIImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void addDatapackLoader(@NotNull String prefix, @NotNull DatapackResourceLoader loader) {
        Preconditions.checkNotNull(prefix);
        Preconditions.checkNotNull(loader);

        if (prefix.matches("a-z0-9/\\._-")) {
            throw new IllegalArgumentException("bad path prefix fragment when registering datapack loader");
        }

        if (prefix.endsWith("/")) {
            throw new IllegalArgumentException("path prefix must not end with /");
        }

        loaders.add(new Pair<>(prefix, loader));
    }


    @Override
    public CustomItem getCustomItem(@NotNull ItemStack stack) {
        Preconditions.checkNotNull(stack);

        if (stack instanceof CraftItemStack craftStack) {
            final var mc = craftStack.handle;
            return m(((ItemStackAccess) (Object) mc).custom_item_api$getStateManager().getCustomState(), CustomItemInstance::item);
        } else {
            throw new IllegalArgumentException("stack must be from paper");
        }
    }

    /*
    @Override
    public NamespacedKey getCustomItemId(ItemStack stack) {
        Preconditions.checkNotNull(stack);

        if (stack instanceof CraftItemStack craftStack) {
            final var mc = craftStack.handle;
            return m(((ItemStackAccess) (Object) mc).getStateManager().getCustomState(), CustomItemInstance::key);
        } else {
            throw new IllegalArgumentException("stack must be from paper");
        }
    }*/

    @SuppressWarnings("UnreachableCode")
    @Override
    public void forceUpdate(@NotNull ItemStack stack) {
        Preconditions.checkNotNull(stack);

        if (stack instanceof CraftItemStack craftStack) {
            final var mc = craftStack.handle;
            final var state = ((ItemStackAccess) (Object) mc).custom_item_api$getStateManager();
            if (state.getCustomState() != null) {
                state.recomputeDisplay(craftStack.handle);
            }
        } else {
            throw new IllegalArgumentException("stack must be from paper");
        }
    }
}