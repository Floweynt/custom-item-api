package com.floweytf.customitemapi.datadriven.json.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.List;

public class BookComponent implements TaggedItemComponent {
    public static final TaggedItemComponentInfo INFO = new TaggedItemComponentInfo(true, object -> {
        final var config = Config.fromJson(object);
        return () -> new BookComponent(config);
    });

    private final Config config;

    private BookComponent(Config config) {
        this.config = config;
    }

    @Override
    public void configure(ExtraItemData data) {
        data.setBookAuthor(config.author);
        data.setBookPages(config.pages);
    }

    private record Config(String author, List<Component> pages) {
        private static Config fromJson(JsonObject e) {
            return new Config(
                e.get("author").getAsString(),
                e.get("pages").getAsJsonArray().asList().stream().map(entry -> GsonComponentSerializer.gson().deserializeFromTree(entry)).toList()
            );
        }
    }
}