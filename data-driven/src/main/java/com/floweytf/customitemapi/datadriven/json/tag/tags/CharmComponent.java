package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.datadriven.PluginMain;
import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import com.floweytf.customitemapi.datadriven.registry.MonumentaCharmAttributes;
import com.floweytf.customitemapi.datadriven.registry.MonumentaClasses;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CharmComponent extends SimpleTaggedComponent<CharmComponent.Config> {
    public static final TaggedComponentType<Config, CharmComponent> TYPE = new TaggedComponentType<>(
        Config::fromJson,
        CharmComponent::new
    );

    private CharmComponent(Config config) {
        super(config);
    }

    @Override
    public void putComponentsEnd(ComponentWriter output) {
        output.writeOne(
            Component.text("Charm Power : "),
            Component.text("★".repeat(config.power()), TextColor.fromHexString("#FFFA75")),
            Component.text(" - ", NamedTextColor.DARK_GRAY),
            config.playerClass.text()
        );

        output.writeOne("When in Charm Slot:");

        for (final var instance : config.attributes()) {
            final var color = TextColor.fromHexString(
                instance.attribute.isNegative() != instance.value() > 0 ? "#40C2E5" : "#D02E28"
            );

            switch (instance.operation) {
                case ADD -> output.writeOne(Component.text(
                    Utils.fmtFloat(instance.value()) + " " + instance.attribute().displayName(),
                    color
                ));
                case MULTIPLY -> output.writeOne(Component.text(
                    Utils.fmtFloat(instance.value() * 100) + "% " + instance.attribute().displayName(),
                    color
                ));
            }
        }
    }

    private record CharmAttributeInstance(MonumentaCharmAttributes attribute, MonumentaCharmAttributes.Operation operation,
                                          double value) {
        public static CharmAttributeInstance fromJson(JsonElement e) {
            return new CharmAttributeInstance(
                MonumentaCharmAttributes.fromJson(e.getAsJsonArray().get(0)),
                MonumentaCharmAttributes.Operation.fromJson(e.getAsJsonArray().get(1)),
                e.getAsJsonArray().get(2).getAsDouble()
            );
        }
    }

    public record Config(MonumentaClasses playerClass, int power,
                         List<CharmAttributeInstance> attributes) implements TaggedComponentConfig<Config> {
        private static Config fromJson(JsonObject e) {
            return new Config(
                MonumentaClasses.fromJson(e.get("class")),
                e.get("power").getAsInt(),
                e.get("attributes").getAsJsonArray().asList().stream().map(CharmAttributeInstance::fromJson).toList()
            );
        }

        @Override
        public @NotNull Config tryMerge(Config other) {
            PluginMain.LOGGER.warn("duplicate charm tag");
            return this;
        }
    }
}