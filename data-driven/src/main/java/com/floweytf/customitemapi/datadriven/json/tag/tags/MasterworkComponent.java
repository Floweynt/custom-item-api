package com.floweytf.customitemapi.datadriven.json.tag.tags;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.PluginMain;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;
import com.floweytf.customitemapi.datadriven.json.tag.SimpleTaggedComponent;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentConfig;
import com.floweytf.customitemapi.datadriven.json.tag.TaggedComponentType;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class MasterworkComponent extends SimpleTaggedComponent<MasterworkComponent.Config> {
    private static final Int2ObjectMap<Config> CONFIG_BY_LEVEL = new Int2ObjectArrayMap<>();
    private static final Int2ObjectMap<MasterworkComponent> COMPONENT_BY_LEVEL = new Int2ObjectArrayMap<>();

    public static final TaggedComponentType<Config, MasterworkComponent> TYPE = new TaggedComponentType<>(
        (json) -> CONFIG_BY_LEVEL.computeIfAbsent(json.get("level").getAsInt(), Config::new),
        (config) -> COMPONENT_BY_LEVEL.computeIfAbsent(config.level, (k) -> new MasterworkComponent(config))
    );

    private final Component text;
    private final int level;

    private MasterworkComponent(Config config) {
        super(config);
        this.text = Component.text("Masterwork : ")
            .append(Component.text("★".repeat(config.level), NamedTextColor.GOLD))
            .append(Component.text("☆".repeat(Math.max(4 - config.level, 0)), NamedTextColor.DARK_GRAY));
        this.level = config.level;
    }

    @Override
    public void putComponentsStart(ComponentWriter output) {
        output.writeOne(text);
    }

    @Override
    public void configure(ExtraItemData data) {
        data.getNBTTag().getOrCreateCompound("Mounmenta").setString("Masterwork", String.valueOf(level));
    }

    public record Config(int level) implements TaggedComponentConfig<Config> {
        @Override
        public Config tryMerge(Config other) {
            PluginMain.LOGGER.warn("duplicate masterwork tag");
            return this;
        }
    }
}