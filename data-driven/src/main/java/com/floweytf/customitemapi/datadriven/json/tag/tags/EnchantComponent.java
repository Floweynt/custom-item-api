package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import com.floweytf.customitemapi.datadriven.registry.MonumentaEnchantments;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTCompoundList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnchantComponent extends SimpleTaggedComponent<EnchantComponent.Config> {
    public static final TaggedComponentType<Config, EnchantComponent> TYPE = new TaggedComponentType<>(
        Config::fromJson,
        EnchantComponent::new
    );

    private EnchantComponent(Config config) {
        super(config);
    }

    @Override
    public void putComponentsStart(ComponentWriter output) {
        config.enchants.stream().map(instance -> {
            final var main = Component.text(instance.enchant().displayText(), NamedTextColor.GRAY);

            if (!instance.enchant.hideLevel()) {
                return main
                    .append(Component.space())
                    .append(Component.text(Utils.toRoman(instance.level), NamedTextColor.GRAY));
            }

            return main;
        }).forEach(output::writeOne);
    }

    @Override
    public void configure(ExtraItemData data) {
        final var monumentaTag = data.getNBTTag()
            .getOrCreateCompound("Monumenta")
            .getOrCreateCompound("Stock")
            .getOrCreateCompound("Enchantments");

        final var vanillaTag = data.getNBTTag()
            .getCompoundList("Enchantments");

        for (final var inst : config.enchants) {
            inst.putTag(monumentaTag);
        }

        for (final var inst : config.enchants) {
            inst.putVanillaTag(vanillaTag);
        }
    }

    private record EnchantmentInstance(MonumentaEnchantments enchant, int level) {
        public static EnchantmentInstance fromJson(JsonElement e) {
            return new EnchantmentInstance(
                MonumentaEnchantments.fromJson(e.getAsJsonArray().get(0)),
                e.getAsJsonArray().get(1).getAsInt()
            );
        }

        public void putTag(ReadWriteNBT list) {
            list.setInteger(enchant.displayText(), level);
        }

        public void putVanillaTag(ReadWriteNBTCompoundList list) {
            if (!enchant.id().getNamespace().equals("minecraft"))
                return;

            final var tag = list.addCompound();
            tag.setInteger("lvl", level);
            tag.setString("id", enchant.id().toString());
        }
    }

    public record Config(List<EnchantmentInstance> enchants) implements TaggedComponentConfig<Config> {
        private static Config fromJson(JsonObject e) {
            return new Config(
                e.get("enchants").getAsJsonArray().asList().stream().map(EnchantmentInstance::fromJson).toList()
            );
        }

        @Override
        public @NotNull Config tryMerge(Config other) {
            return new Config(Utils.joinToImmutable(enchants, other.enchants));
        }
    }
}