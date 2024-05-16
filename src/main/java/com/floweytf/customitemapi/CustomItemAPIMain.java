package com.floweytf.customitemapi;

import com.floweytf.customitemapi.api.Version;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import com.floweytf.customitemapi.helpers.tag.CompoundTagBuilder;
import com.floweytf.customitemapi.impl.CustomItemAPIImpl;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class CustomItemAPIMain {
    public static final String MOD_ID = "customitemapi";
    public static final Version API_VERSION = Version.from("1.0.0");
    public static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/" + API_VERSION);

    private static Map<Item, Material> ITEM_MATERIAL = null;

    // Stupid 1.19.4 hack
    private static void initMap() {
        if (ITEM_MATERIAL != null) {
            return;
        }

        ITEM_MATERIAL = new HashMap<>();
        BuiltInRegistries.ITEM.entrySet().forEach(e -> {
            final var resource = e.getKey().location();
            final var item = e.getValue();
            ITEM_MATERIAL.put(item, Material.getMaterial(resource.getPath().toUpperCase(Locale.ROOT)));
        });
    }

    public static CustomItemAPIImpl getAPIInstance() {
        return CustomItemAPIImpl.getInstance();
    }

    public static Material materialFromItem(Item item) {
        initMap();
        return ITEM_MATERIAL.get(item);
    }


    public static ItemStack makeItem(CustomItemType type, int count, Optional<CompoundTag> dataTag) {
        CompoundTag itemTag = CompoundTagBuilder.of()
            .put("id", BuiltInRegistries.ITEM.getKey(CraftMagicNumbers.getItem(type.baseItem())).toString())
            .put("Count", (byte) count)
            .put("tag", CompoundTagBuilder.of(
                ItemStackStateManager.ROOT_TAG_KEY, CompoundTagBuilder.of()
                    .put(ItemStackStateManager.KEY_ID, type.variantSet().key().toString())
                    .put(ItemStackStateManager.KEY_VARIANT, type.variantId())
                    .put(ItemStackStateManager.KEY_SAVE_DATA, dataTag.orElseGet(CompoundTag::new))
                    .get()
            )).get();

        return ItemStack.of(itemTag);
    }

    public static ItemStack makeItem(ResourceLocation id, String variant, int count, Optional<CompoundTag> dataTag) {
        final var variantSet = CustomItemRegistryImpl.getInstance().get(CraftNamespacedKey.fromMinecraft(id));
        if (variantSet == null) {
            throw new IllegalArgumentException("unknown item id");
        }

        final var type = variantSet.variants().get(variant);

        if (type == null) {
            throw new IllegalArgumentException("unknown variant");
        }

        return makeItem(type, count, dataTag);
    }

    public static ItemStack makeItem(ResourceLocation id, int count, Optional<CompoundTag> dataTag) {
        final var variantSet = CustomItemRegistryImpl.getInstance().get(CraftNamespacedKey.fromMinecraft(id));
        if (variantSet == null) {
            throw new IllegalArgumentException("unknown item id");
        }

        return makeItem(variantSet.defaultVariant(), count, dataTag);
    }
}