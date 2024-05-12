package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class QuestComponent implements TaggedItemComponent {
    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var config = Config.fromJson(object);
        return () -> new QuestComponent(config);
    });

    private final Config config;

    private QuestComponent(Config config) {
        this.config = config;
    }

    @Override
    public void putComponentsEnd(ComponentWriter output) {
        output.writeOne(Component.text("* Quest Item *", TextColor.fromHexString("#ff55ff")));
        output.writeOne("#Q" + config.id);
    }

    private record Config(String id) {
        public static Config fromJson(JsonObject e) {
            return new Config(
                e.get("id").getAsString()
            );
        }
    }
}