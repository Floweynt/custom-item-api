package com.floweytf.customitemapi;

import com.floweytf.customitemapi.api.Version;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ModMain {
    public static final String MOD_ID = "customitemapi";
    public static final Version API_VERSION = Version.from("1.0.0");
    public static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/" + API_VERSION);

    private static Map<Item, Material> ITEM_MATERIAL = null  ;

    // Stupid 1.19.4 hack
    private static void initMap() {
        if(ITEM_MATERIAL != null) {
            return;
        }

        ITEM_MATERIAL = new HashMap<>();
        BuiltInRegistries.ITEM.entrySet().forEach(e -> {
            final var resource = e.getKey().location();
            final var item = e.getValue();
            ITEM_MATERIAL.put(item, Material.getMaterial(resource.getPath().toUpperCase(Locale.ROOT)));
        });
    }

    public static Material materialFromItem(Item item) {
        initMap();
        return ITEM_MATERIAL.get(item);
    }
}