package com.floweytf.customitemapi;

import com.floweytf.customitemapi.api.CustomItemAPI;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.Version;
import com.floweytf.customitemapi.api.item.CustomItemType;
import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import com.floweytf.customitemapi.helpers.tag.CompoundTagBuilder;
import com.floweytf.customitemapi.impl.CustomItemAPIImpl;
import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.jar.Manifest;

public class CustomItemAPIMain {
    public static final String MOD_ID = "customitemapi";
    public static final Version API_VERSION = Version.from("1.0.0");
    public static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/" + API_VERSION);
    public static final String GIT_BRANCH;
    public static final String GIT_HASH;

    public static Manifest manifest(final @NotNull Class<?> clazz) {
        final String classLocation = "/" + clazz.getName().replace(".", "/") + ".class";
        final URL resource = clazz.getResource(classLocation);

        if (resource == null) {
            throw new RuntimeException("not found");
        }

        final String classFilePath = resource.toString().replace("\\", "/");
        final String archivePath = classFilePath.substring(0, classFilePath.length() - classLocation.length());

        try (final InputStream stream = new URL(archivePath + "/META-INF/MANIFEST.MF").openStream()) {
            return new Manifest(stream);
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static {
        if(SharedConstants.IS_RUNNING_IN_IDE) {
            GIT_BRANCH = "";
            GIT_HASH = "";
            LOGGER.info("Running CustomItemAPI {}+dev", API_VERSION);
        } else {
            final var manifest = manifest(CustomItemAPIMain.class);
            GIT_BRANCH = manifest.getMainAttributes().getValue("Git-Branch");
            GIT_HASH = manifest.getMainAttributes().getValue("Git-Hash").substring(0, 10);
            LOGGER.info("Running CustomItemAPI {}+{}-{}", API_VERSION, GIT_BRANCH, GIT_HASH);
        }
    }

    private static Map<Item, Material> ITEM_MATERIAL = null;

    // Stupid 1.19.4 hack
    private static void initMap() {
        if (ITEM_MATERIAL != null) {
            return;
        }

        ITEM_MATERIAL = new HashMap<>();
        for (final var e : BuiltInRegistries.ITEM.entrySet()) {
            final var resource = e.getKey().location();
            final var item = e.getValue();
            ITEM_MATERIAL.put(item, Material.getMaterial(resource.getPath().toUpperCase(Locale.ROOT)));
        }
    }

    public static Material materialFromItem(Item item) {
        initMap();
        return ITEM_MATERIAL.get(item);
    }
    // end hack

    public static CustomItemAPIImpl getAPIInstance() {
        return CustomItemAPIImpl.getInstance();
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

    public static ItemStack makeItem(ResourceLocation key, String variantId, int count, Optional<CompoundTag> dataTag) {
        final var variantSet = CustomItemRegistryImpl.getInstance().get(CraftNamespacedKey.fromMinecraft(key));
        if (variantSet == null) {
            throw new IllegalArgumentException("unknown item id");
        }

        final var variant = variantSet.variants().get(variantId);

        if (variant == null) {
            throw new IllegalArgumentException("unknown variant '" + variantId + "'");
        }

        return makeItem(variant, count, dataTag);
    }

    public static ItemStack makeItem(ResourceLocation key, int count, Optional<CompoundTag> dataTag) {
        final var variantSet = CustomItemRegistryImpl.getInstance().get(CraftNamespacedKey.fromMinecraft(key));
        if (variantSet == null) {
            throw new IllegalArgumentException("unknown item id");
        }

        return makeItem(variantSet.defaultVariant(), count, dataTag);
    }
}