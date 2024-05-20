package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import com.floweytf.customitemapi.datadriven.registry.MonumentaAttributes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTCompoundList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class AttributeComponent extends SimpleTaggedComponent<AttributeComponent.Config> {
    public static final TaggedComponentType<Config, AttributeComponent> TYPE = new TaggedComponentType<>(
        Config::fromJson,
        AttributeComponent::new
    );

    private AttributeComponent(Config config) {
        super(config);
    }

    @Override
    public void putComponentsEnd(ComponentWriter output) {
        output.writeOne(Component.text("When "), config.usage.displayText());

        for (final var instance : config.attributes) {
            final var color = instance.value() > 0 ? instance.attr.positiveColor() : instance.attr.negativeColor();
            final var value = instance.attr == MonumentaAttributes.KNOCKBACK_RESISTANCE ? instance.value * 10 :
                instance.value;

            switch (instance.operation) {
                case ADD -> output.writeOne(
                    Component.text(Utils.fmtFloat(value), color),
                    Component.space(),
                    Component.text(instance.attr.displayName()).decorate(TextDecoration.BOLD)
                );
                case MULTIPLY -> output.writeOne(
                    Component.text(Utils.fmtFloat(value * 100) + "%", color),
                    Component.space(),
                    Component.text(instance.attr.displayName()).decorate(TextDecoration.BOLD)
                );
                case BASE -> output.writeOne(
                    Component.text(Utils.fmtFloat(value, false), NamedTextColor.DARK_GREEN),
                    Component.space(),
                    Component.text(instance.attr.displayName()).decorate(TextDecoration.BOLD)
                );
            }
        }
    }

    @Override
    public void configure(ExtraItemData data) {
        final var monumentaTag = data.getNBTTag()
            .getOrCreateCompound("Monumenta")
            .getOrCreateCompound("Stock")
            .getCompoundList("Attributes");


        final var vanillaTag = data.getNBTTag()
            .getCompoundList("AttributeModifiers");

        for (final var inst : config.attributes) {
            inst.putTag(config.usage, monumentaTag);
        }

        for (final var inst : config.attributes) {
            inst.putVanillaTag(config.usage, vanillaTag);
        }
    }

    private record AttributeInstance(MonumentaAttributes attr, MonumentaAttributes.Operation operation, double value) {
        public static AttributeInstance fromJson(JsonElement e) {
            return new AttributeInstance(
                MonumentaAttributes.fromJson(e.getAsJsonArray().get(0)),
                MonumentaAttributes.Operation.fromJson(e.getAsJsonArray().get(1)),
                e.getAsJsonArray().get(2).getAsDouble()
            );
        }

        private String computeAttributeName() {
            return switch (attr) {
                case ARMOR, AGILITY, MAX_HEALTH, ATTACK_SPEED, PROJECTILE_SPEED, THROW_RATE, SPELL_POWER, MOVEMENT_SPEED,
                     POTION_DAMAGE, THORNS_DAMAGE, POTION_RADIUS, KNOCKBACK_RESISTANCE -> attr.displayName();
                // wtf? why? operation already exists????
                // Is monumenta pranking me or something?
                case ATTACK_DAMAGE, PROJECTILE_DAMAGE, MAGIC_DAMAGE -> attr.displayName() + " " + switch (operation) {
                    case ADD, BASE -> " Add";
                    case MULTIPLY -> " Multiply";
                };
            };
        }

        public void putTag(MonumentaAttributes.Usages usage, ReadWriteNBTCompoundList listTag) {
            final var entryTag = listTag.addCompound();
            entryTag.setString("Slot", usage.name().toLowerCase());
            entryTag.setString("Operation", operation.name().toLowerCase());
            entryTag.setDouble("Amount", value);
            entryTag.setString("AttributeName", computeAttributeName());
        }

        public void putVanillaTag(MonumentaAttributes.Usages usage, ReadWriteNBTCompoundList listTag) {
            if (attr.vanilla() == null) {
                return;
            }

            final var entryTag = listTag.addCompound();
            entryTag.setString("Name", "Modifier");
            entryTag.setString("Slot", usage.name().toLowerCase());
            entryTag.setInteger("Operation", operation.minecraftId());
            entryTag.setDouble("Amount", value);
            entryTag.setString("AttributeName", attr.vanilla().getKey().toString());
            entryTag.setUUID("UUID", UUID.randomUUID());
        }
    }

    public record Config(MonumentaAttributes.Usages usage,
                         List<AttributeInstance> attributes) implements TaggedComponentConfig<Config> {
        private static Config fromJson(JsonObject e) {
            return new Config(
                MonumentaAttributes.Usages.fromJson(e.get("usage")),
                e.get("attributes").getAsJsonArray().asList().stream().map(AttributeInstance::fromJson).toList()
            );
        }

        @Nullable
        @Override
        public Config tryMerge(Config other) {
            if (other.usage == this.usage) {
                return new Config(this.usage, Utils.joinToImmutable(attributes, other.attributes));
            }

            return null;
        }
    }
}