package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.datadriven.PluginMain;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

public class QuestComponent extends SimpleTaggedComponent<QuestComponent.Config> {
    public static final TaggedComponentType<Config, QuestComponent> INFO = new TaggedComponentType<>(
        Config::fromJson,
        QuestComponent::new
    );

    private QuestComponent(Config config) {
        super(config);
    }

    @Override
    public void putComponentsEnd(ComponentWriter output) {
        output.writeOne(Component.text("* Quest Item *", TextColor.fromHexString("#ff55ff")));
        output.writeOne("#Q" + config.id);
    }

    public record Config(String id) implements TaggedComponentConfig<Config> {
        public static Config fromJson(JsonObject e) {
            return new Config(
                e.get("id").getAsString()
            );
        }

        @Override
        public Config tryMerge(Config other) {
            PluginMain.LOGGER.warn("duplicate quest tag");
            return this;
        }
    }
}