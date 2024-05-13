package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.Utils;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.registry.MonumentaEffects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PotionComponent implements TaggedItemComponent {
    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var config = Config.fromJson(object);
        return () -> new PotionComponent(config);
    });

    private final Config config;

    private PotionComponent(Config config) {
        this.config = config;
    }

    @Override
    public void putComponentsEnd(ComponentWriter output) {
        output.writeOne(Component.text("When Consumed", NamedTextColor.GRAY));

        for (final var instance : config.effects) {
            final var duration = instance.duration / 20;
            final var color = TextColor.fromHexString(instance.effects.isNegative() ? "#D02E28" : "#40C2E5");

            var text = Component.empty();

            switch (instance.effects().displayLevelType()) {
                case PERCENT -> text = text.append(Component.text(
                    Utils.fmtFloat(instance.level * 100, false) + "% "
                ));
                case PERCENT_BONUS -> text = text.append(Component.text(
                    Utils.fmtFloat(instance.level * 100) + "% "
                ));
                case PERCENT_NEGATE -> text = text.append(Component.text(
                    Utils.fmtFloat(-instance.level * 100) + "% "
                ));
            }

            text = text.append(Component.text(instance.effects().displayName() + " ", color));

            if (instance.effects().displayLevelType() == MonumentaEffects.LevelDisplayType.DISPLAY_LEVEL) {
                text = text.append(Component.text(
                    Utils.toRoman((int) instance.level()) + " ", color
                ));
            }

            if (instance.effects().displayDuration()) {
                text = text.append(Component.text(
                    "(" +
                        Math.floor(duration / 60) + ":" +
                        StringUtils.rightPad(Integer.toString((int) (duration % 60)), 2, "0") + " ",
                    NamedTextColor.GRAY
                ));
            }

            output.writeOne(text);
        }
    }

    private record PotionEffectInstance(double duration, double level, MonumentaEffects effects) {
        public static PotionEffectInstance fromJson(JsonElement e) {
            return new PotionEffectInstance(
                e.getAsJsonArray().get(0).getAsDouble(),
                e.getAsJsonArray().get(1).getAsDouble(),
                MonumentaEffects.fromJson(e.getAsJsonArray().get(2))
            );
        }
    }

    private record Config(List<PotionEffectInstance> effects) {
        private static Config fromJson(JsonObject e) {
            return new Config(
                e.get("effects").getAsJsonArray().asList().stream().map(PotionEffectInstance::fromJson).toList()
            );
        }
    }
}