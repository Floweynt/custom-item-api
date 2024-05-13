package com.floweytf.customitemapi.datadriven;

import com.floweytf.customitemapi.api.CustomItemAPI;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.datadriven.json.JsonCustomItem;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMain extends JavaPlugin {
    @Override
    public void onLoad() {
        final var registry = CustomItemRegistry.getInstance();
        CustomItemAPI.getInstance().addDatapackLoader(
            "items", manager -> manager.resources().forEach((id, resource) -> {
                try {
                    final var data = JsonCustomItem.readFromJson(registry, getSLF4JLogger(), resource, id);
                } catch (Exception e) {
                    getSLF4JLogger().warn("Failed to load item " + id, e);
                }
            })
        );
    }
}