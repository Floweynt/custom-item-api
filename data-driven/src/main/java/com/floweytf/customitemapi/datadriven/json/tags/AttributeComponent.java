package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class AttributeComponent implements TaggedItemComponent {
    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var config = Config.fromJson(object);
        return () -> new AttributeComponent(config);
    });
    private final Config config;

    private AttributeComponent(Config config) {
        this.config = config;
    }

    @Override
    public void putComponentsEnd(Consumer<Component> output) {
        output.accept(
            Component.text("When ").append(config.usage.displayText())
        );

        config.attributes.stream().map(attr -> {
            final var color = attr.value() > 0 ? attr.attr.positiveColor() : attr.attr.negativeColor();
            final var value = attr.attr == Attribute.KNOCKBACK_RESISTANCE ? attr.value * 10 : attr.value;

            return switch (attr.operation) {
                case ADD -> Component.text(Utils.fmtFloat(value))
                    .color(color)
                    .append(Component.space())
                    .append(Component.text(attr.attr.displayName())).decorate(TextDecoration.BOLD);
                case MULTIPLY -> Component.text(Utils.fmtFloat(value * 100) + "%")
                    .color(color)
                    .append(Component.space())
                    .append(Component.text(attr.attr.displayName()).decorate(TextDecoration.BOLD));
                case BASE -> Component.text(Utils.fmtFloat(value, false))
                    .color(NamedTextColor.DARK_GREEN)
                    .append(Component.space())
                    .append(Component.text(attr.attr.displayName()).decorate(TextDecoration.BOLD));
            };
        }).forEach(output);
    }

    public enum Usage {
        MAINHAND("in", "Main Hand"),
        OFFHAND("in", "Off Hand"),
        HEAD("on", "Head"),
        CHEST("on", "Chest"),
        LEGS("on", "Legs"),
        FEET("on", "Feet"),
        PROJECTILE(null, "Shot");

        private final Component displayText;

        Usage(@Nullable String preposition, String text) {
            Component c = Component.text(text).color(NamedTextColor.GOLD);

            if (preposition != null) {
                c = Component.text(preposition).append(Component.space()).append(c);
            }

            this.displayText = c;
        }

        public static Usage fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }

        public Component displayText() {
            return displayText;
        }
    }

    public enum Operation {
        ADD, BASE, MULTIPLY;

        public static Operation fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }
    }

    public enum Attribute {
        THROW_RATE("Throw Rate", true),
        THORNS_DAMAGE("Thorns Damage", false),
        PROJECTILE_DAMAGE("Projectile Damage", true),
        PROJECTILE_SPEED("Projectile Speed", true),
        AGILITY("Agility", false, "#33CCFF", "#D02E28"),
        ARMOR("Armor", false, "#33CCFF", "#D02E28"),
        MOVEMENT_SPEED("Speed", false),
        MAX_HEALTH("Max Health", false),
        KNOCKBACK_RESISTANCE("Knockback Resistance", false),
        ATTACK_SPEED("Attack Speed", true),
        ATTACK_DAMAGE("Attack Damage", true),
        MAGIC_DAMAGE("Magic Damage", false),
        SPELL_POWER("Spell Power", false),
        POTION_DAMAGE("Potion Damage", true),
        POTION_RADIUS("Potion Radius", true);

        private final String name;
        private final boolean isBase;
        private final TextColor positiveColor;
        private final TextColor negativeColor;

        Attribute(String name, boolean isBase, String positiveColor, String negativeColor) {
            this.name = name;
            this.isBase = isBase;
            this.positiveColor = TextColor.fromHexString(positiveColor);
            this.negativeColor = TextColor.fromHexString(negativeColor);
        }

        Attribute(String name, boolean isBase) {
            this(name, isBase, "#5555FF", "##FF5555");
        }

        public static Attribute fromJson(JsonElement e) {
            return valueOf(e.getAsString().toUpperCase());
        }

        public String displayName() {
            return name;
        }

        public boolean isBase() {
            return isBase;
        }

        public TextColor positiveColor() {
            return positiveColor;
        }

        public TextColor negativeColor() {
            return negativeColor;
        }
    }

    private record AttributeInstance(Attribute attr, Operation operation, double value) {
        public static AttributeInstance fromJson(JsonElement e) {
            return new AttributeInstance(
                Attribute.fromJson(e.getAsJsonArray().get(0)),
                Operation.fromJson(e.getAsJsonArray().get(1)),
                e.getAsJsonArray().get(2).getAsDouble()
            );
        }
    }

    private record Config(Usage usage, List<AttributeInstance> attributes) {
        private static Config fromJson(JsonObject e) {
            return new Config(
                Usage.fromJson(e.get("usage")),
                e.get("attributes").getAsJsonArray().asList().stream().map(AttributeInstance::fromJson).toList()
            );
        }
    }
}