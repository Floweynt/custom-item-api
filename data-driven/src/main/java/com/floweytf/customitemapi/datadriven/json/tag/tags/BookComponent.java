package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.PluginMain;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

import java.util.List;

public class BookComponent extends SimpleTaggedComponent<BookComponent.Config> {
    public static final TaggedComponentType<Config, BookComponent> TYPE = new TaggedComponentType<>(
        Config::fromJson,
        BookComponent::new
    );

    private BookComponent(Config config) {
        super(config);
    }

    @Override
    public void configure(ExtraItemData data) {
        data.setBookAuthor(config.author);
        data.setBookPages(config.pages);
        data.setBookTitle(config.title);
    }

    public record Config(String author, Component title, List<Component> pages) implements TaggedComponentConfig<Config> {
        private static Config fromJson(JsonObject e) {
            return new Config(
                e.get("author").getAsString(),
                GsonComponentSerializer.gson().deserializeFromTree(e.get("title")),
                e.get("pages").getAsJsonArray().asList().stream().map(entry -> GsonComponentSerializer.gson().deserializeFromTree(entry)).toList()
            );
        }

        @Override
        public Config tryMerge(Config other) {
            PluginMain.LOGGER.warn("duplicate book components found, using the first one");
            return this;
        }
    }
}