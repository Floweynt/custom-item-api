package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.registry.MonumentaAttributes;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.List;

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

    private record AttributeInstance(MonumentaAttributes attr, MonumentaAttributes.Operation operation, double value) {
        public static AttributeInstance fromJson(JsonElement e) {
            return new AttributeInstance(
                MonumentaAttributes.fromJson(e.getAsJsonArray().get(0)),
                MonumentaAttributes.Operation.fromJson(e.getAsJsonArray().get(1)),
                e.getAsJsonArray().get(2).getAsDouble()
            );
        }
    }

    private record Config(MonumentaAttributes.Usages usage, List<AttributeInstance> attributes) {
        private static Config fromJson(JsonObject e) {
            return new Config(
                MonumentaAttributes.Usages.fromJson(e.get("usage")),
                e.get("attributes").getAsJsonArray().asList().stream().map(AttributeInstance::fromJson).toList()
            );
        }
    }
}