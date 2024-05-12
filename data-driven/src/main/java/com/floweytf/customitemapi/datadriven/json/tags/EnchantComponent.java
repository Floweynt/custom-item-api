package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.registry.MonumentaEnchantments;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class EnchantComponent implements TaggedItemComponent {
    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var config = Config.fromJson(object);
        return () -> new EnchantComponent(config);
    });

    private final Config config;

    private EnchantComponent(Config config) {
        this.config = config;
    }

    @Override
    public void putComponentsStart(ComponentWriter output) {
        config.enchants.stream().map(instance -> {
            final var main = Component.text(instance.enchant().displayText(), NamedTextColor.GRAY);

            if (instance.enchant.shouldDisplayLevel()) {
                return main
                    .append(Component.space())
                    .append(Component.text(Utils.toRoman(instance.level), NamedTextColor.GRAY));
            }

            return main;
        }).forEach(output::writeOne);
    }

    private record EnchantmentInstance(MonumentaEnchantments enchant, int level) {
        public static EnchantmentInstance fromJson(JsonElement e) {
            return new EnchantmentInstance(
                MonumentaEnchantments.fromJson(e.getAsJsonArray().get(0)),
                e.getAsJsonArray().get(1).getAsInt()
            );
        }
    }

    private record Config(List<EnchantmentInstance> enchants) {
        private static Config fromJson(JsonObject e) {
            return new Config(
                e.get("enchants").getAsJsonArray().asList().stream().map(EnchantmentInstance::fromJson).toList()
            );
        }
    }
}