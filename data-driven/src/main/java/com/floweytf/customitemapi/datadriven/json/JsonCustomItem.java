package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.datadriven.Pair;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.datadriven.json.tags.TaggedItemComponent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class JsonCustomItem {
    public static final String NO_GLINT = "noglint";
    public static final String GLINT = "glint";
    public static final Style DEFAULT_LORE_STYLE = Style.style(NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC,
        false);
    public static final Style NO_ITALIC = Style.empty().decoration(TextDecoration.ITALIC, false);

    private static Component getComponent(JsonObject object, String key) {
        // TODO: do better here
        return GsonComponentSerializer.gson().deserializeFromTree(object.get(key));
    }

    private static Optional<String> getStringOrOptionalEmpty(JsonObject object, String key) {
        // TODO: do better here
        return object.has(key) ? Optional.of(object.get(key).getAsString()) : Optional.empty();
    }

    private static String getTagId(JsonElement element) {
        if (element.isJsonPrimitive())
            return element.getAsString();

        return element.getAsJsonObject().get("tag").getAsString();
    }

    private static TriState setTriStateOrRun(TriState state, boolean value, Runnable onAlreadySet) {
        if (state != TriState.NOT_SET) {
            state = TriState.byBoolean(value);
        } else {
            onAlreadySet.run();
        }

        return state;
    }

    public static Pair<Material, Supplier<CustomItem>> readFromJson(Logger logger, JsonObject resource, NamespacedKey id) {
        final var name = getComponent(resource, "name");
        final var material = Objects.requireNonNull(Material.matchMaterial(resource.get("item").getAsString()));
        final var rarity = getStringOrOptionalEmpty(resource, "rarity")
            .map(String::toUpperCase).map(Rarity::valueOf);
        final var region = getStringOrOptionalEmpty(resource, "region")
            .map(String::toUpperCase).map(Region::valueOf);
        final var location = getStringOrOptionalEmpty(resource, "location")
            .map(String::toUpperCase).map(Location::valueOf);

        final var lore = resource.getAsJsonArray("lore").asList().stream()
            .map(GsonComponentSerializer.gson()::deserializeFromTree)
            .toList();
        final Optional<Class<?>> pluginImpl = getStringOrOptionalEmpty(resource, "plugin_implementation")
            .map(className -> {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

        // obtain some flags
        TriState hasGlint = TriState.NOT_SET;
        boolean isStateless = pluginImpl.isEmpty();
        final var tagInfos = new ArrayList<Pair<TaggedItemComponent.TaggedItemComponentInfo,
            Supplier<TaggedItemComponent>>>();
        final Runnable onDuplicateGlintTag = () -> logger.warn("While parsing " + id + " - duplicate glint specifier tag, " +
            "ignoring. Please fix this in " + "the json file!");

        for (final var tag : resource.getAsJsonArray("tags")) {
            final var tagId = getTagId(tag);

            JsonObject objectTag;
            if (tag.isJsonPrimitive()) {
                objectTag = new JsonObject();
                objectTag.addProperty("tag", tagId);
            } else {
                objectTag = tag.getAsJsonObject();
            }

            switch (tagId) {
                case NO_GLINT -> hasGlint = setTriStateOrRun(hasGlint, false, onDuplicateGlintTag);
                case GLINT -> hasGlint = setTriStateOrRun(hasGlint, true, onDuplicateGlintTag);
                default -> {
                    final var tagInfo = TaggedItemComponent.TAG_PRODUCERS.get(tagId);
                    if (tagInfo == null) {
                        logger.warn("While parsing " + id + " - unknown tag with id: " + tagId);
                        return null;
                    }

                    isStateless |= tagInfo.isStateless();
                    tagInfos.add(new Pair<>(tagInfo, tagInfo.supplier().apply(objectTag)));
                }
            }
        }

        if (isStateless) {
            final var itemInstance = new StatelessJsonCustomItem(
                name, lore,
                rarity, region, location,
                pluginImpl,
                hasGlint, tagInfos.stream().map(u -> u.second().get()).toList()
            );
            return new Pair<>(material, () -> itemInstance);
        }

        return null;
    }

    private static TextColor colorFromString(String color) {
        return color.startsWith("#") ? TextColor.fromHexString(color) : NamedTextColor.NAMES.value(color);
    }

    private static Component createRarityNameFormat(String text, String color, TextDecoration... decorations) {
        return Component.text(text).style(Style.style(colorFromString(color), decorations));
    }

    public static Style defaultTitleFormat(TextDecoration... decoration) {
        return Style.empty().decorate(decoration);
    }

    public enum Rarity {
        T0(createRarityNameFormat("Tier 0", "dark_gray"), defaultTitleFormat()),
        T1(createRarityNameFormat("Tier I", "dark_gray"), defaultTitleFormat()),
        T2(createRarityNameFormat("Tier II", "dark_gray"), defaultTitleFormat()),
        T3(createRarityNameFormat("Tier III", "dark_gray"), defaultTitleFormat()),
        T4(createRarityNameFormat("Tier IV", "dark_gray"), defaultTitleFormat()),
        T5(createRarityNameFormat("Tier V", "dark_gray"), defaultTitleFormat()),
        EPIC(createRarityNameFormat("Epic", "#B314E3", TextDecoration.BOLD), defaultTitleFormat(TextDecoration.UNDERLINED,
            TextDecoration.BOLD)),
        ARTIFACT(createRarityNameFormat("Artifact", "#D02E28"), defaultTitleFormat(TextDecoration.BOLD)),
        RARE(createRarityNameFormat("Rare", "#4AC2E5"), defaultTitleFormat(TextDecoration.BOLD)),
        UNCOMMON(createRarityNameFormat("Uncommon", "#C0C0C0"), defaultTitleFormat(TextDecoration.BOLD)),
        COMMON(createRarityNameFormat("Common", "#C0C0C0"), defaultTitleFormat(TextDecoration.BOLD)),
        CHARM(createRarityNameFormat("Charm", "#FFFA75"), defaultTitleFormat()),
        RARECHARM(createRarityNameFormat("Rare Charm", "#4AC2E5"), defaultTitleFormat()),
        EPICCHARM(createRarityNameFormat("Epic Charm", "#B314E3"), defaultTitleFormat()),
        FISH(createRarityNameFormat("Fish", "dark_gray"), defaultTitleFormat()),
        UNIQUE(createRarityNameFormat("Unique", "#C8A2C8"), defaultTitleFormat(TextDecoration.BOLD)),
        TROPHY(createRarityNameFormat("Trophy", "#CAFFFD"), defaultTitleFormat(TextDecoration.BOLD)),
        EVENT(createRarityNameFormat("Event", "#7FFFD4"), defaultTitleFormat(TextDecoration.BOLD)),
        PATRON(createRarityNameFormat("Patron Made", "#82DB17"), defaultTitleFormat(TextDecoration.BOLD)),
        KEY(createRarityNameFormat("Key", "#47B6B5", TextDecoration.BOLD), defaultTitleFormat(TextDecoration.BOLD)),
        LEGACY(createRarityNameFormat("Legacy", "#EEE6D6"), defaultTitleFormat(TextDecoration.BOLD)),
        CURRENCY(createRarityNameFormat("Currency", "#DCAE32"), defaultTitleFormat(TextDecoration.BOLD)),
        OBFUSCATED(createRarityNameFormat("Stick_:)", "#5D2D87", TextDecoration.OBFUSCATED),
            defaultTitleFormat(TextDecoration.BOLD)),
        LEGENDARY(createRarityNameFormat("Legendary", "#FFD700", TextDecoration.BOLD),
            defaultTitleFormat(TextDecoration.BOLD)),
        EVENT_CURRENCY(createRarityNameFormat("Event Currency", "#DCAE32"), defaultTitleFormat(TextDecoration.BOLD)),
        ;

        private final Component text;
        private final Style nameFormat;

        Rarity(Component text, Style nameFormat) {
            this.text = text;
            this.nameFormat = nameFormat;
        }

        public Component getText() {
            return text;
        }

        public Style getNameFormat() {
            return nameFormat;
        }
    }

    public enum Region {
        VALLEY("King's Valley"),
        ISLES("Celsian Isles"),
        RING("Architect's Ring"),
        ;

        private final String name;

        Region(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Location {
        LABS("Alchemy Labs", "#B4ACC3"),
        WHITE("Halls of Wind and Blood", "white"),
        ORANGE("Fallen Menagerie", "gold"),
        MAGENTA("Plagueroot Temple", "light_purple"),
        LIGHTBLUE("Arcane Rivalry", "#4AC2E5"),
        YELLOW("Vernal Nightmare", "yellow"),
        LIME("Salazar's Folly", "green"),
        PINK("Harmonic Arboretum", "#FF69B4"),
        GRAY("Valley of Forgotten Pharaohs", "dark_gray"),
        LIGHTGRAY("Palace of Mirrors", "gray"),
        CYAN("The Scourge of Lunacy", "dark_aqua"),
        PURPLE("The Grasp of Avarice", "dark_purple"),
        TEAL("Echoes of Oblivion", "#47B6B5"),
        BLUE("Coven's Gambit", "#0C2CA2"),
        BROWN("Cradle of the Broken God", "#703608"),
        INDIGO("Indigo Dungeon", "white"),
        GREEN("Green Dungeon", "white"),
        RED("Red Dungeon", "white"),
        BLACK("Black Dungeon", "white"),
        VERDANT("Verdant Remnants", "#158315"),
        SANCTUM("Forsworn Sanctum", "#52AA00"),
        MIST("The Black Mist", "#674C5B"),
        REMORSE("Sealed Remorse", "#EEE6D6"),
        VIGIL("The Eternal Vigil", "#72999C"),
        BLUESTRIKE("Masquerader's Ruin", "#326DA8"),
        SCIENCE("P.O.R.T.A.L.", "#DCE8E3"),
        REVERIE("Malevolent Reverie", "#790E47"),
        FORUM("The Fallen Forum", "#808000"),
        SILVER("Silver Knight's Tomb", "#C0C0C0"),
        HOARD("The Hoard", "#DAAD3E"),
        EPHEMERAL("Ephemeral Corridors", "#8B0000"),
        EPHEMERALENHANCEMENTS("Ephemeral Enhancements", "#8B0000"),
        WILLOWS("The Black Willows", "#006400"),
        RUSH("Rush of Dissonance", "#C21E56"),
        SHIFTING("City of Shifting Waters", "#7FFFD4"),
        DEPTHS("Darkest Depths", "#5D2D87"),
        GALLERYBASE("Gallery of Fear", "#39B14E"),
        GALLERY1("Sanguine Halls", "#AB0000"),
        GALLERY2("Marina Noir", "#324150"),
        ZENITH("The Celestial Zenith", "#FF9CF0"),
        OVERWORLD1("King's Valley Overworld", "#DCAE32"),
        OVERWORLD2("Celsian Isles Overworld", "#32D7DC"),
        FOREST("The Wolfswood", "#4C8F4D"),
        KEEP("Pelias' Keep", "#C4BBA5"),
        STARPOINT("Star Point", "#342768"),
        FISHING("Architect's Ring Fishing", "#A9D1D0"),
        OVERWORLD3("Architect's Ring Overworld", "#C2C4C4"),
        QUEST("Quest Reward", "#C8A2C8"),
        KAUL("Kaul's Judgment", "dark_green"),
        AZACOR("Azacor's Malice", "#FF6F55"),
        SIRIUS("The Final Blight", "#34CFBC"),
        GODSPORE("The Godspore's Domain", "#426B29"),
        FROSTGIANT("The Waking Giant", "#87CEFA"),
        LICH("Hekawt's Fury", "#FFB43E"),
        CASINO1("Rock's Little Casino", "#EDC863"),
        CASINO2("Monarch's Cozy Casino", "#1773B1"),
        CASINO3("Sticks and Stones Tavern", "#C6C2B6"),
        APRILFOOLS("April Fools Event", "#D22AD2"),
        EASTER("Easter Event", "green"),
        UGANDA("Uganda 2018", "#D02E28"),
        HALLOWEEN("Halloween Event", "gold"),
        VALENTINE("Valentine Event", "#FF7F7F"),
        WINTER("Winter Event", "#AFC2E3"),
        TREASURE("Treasures of Viridia", "#C8A2C8"),
        TRICKSTER("Trickster Challenge", "gold"),
        HORSEMAN("The Headless Horseman", "#8E3418"),
        SEASONPASS("Seasonal Pass", "#FFF63C"),
        LIGHT("Arena of Terth", "#FFFFAA"),
        HOLIDAYSKIN("Holiday Skin", "#B00C2F"),
        SKETCHED("Sketched Skin", "#FFF63C"),
        CHALLENGER("Challenger Skin", "#FEDC10"),
        DIVINE("Divine Skin", "#C6EFF1"),
        VERDANTSKIN("Threadwarped Skin", "#704C8A"),
        HALLOWEENSKIN("Halloween Skin", "gold"),
        WILLOWSKIN("Storied Skin", "#006400"),
        TITANICSKIN("Titanic Skin", "#87CEFA"),
        ETERNITYSKIN("Eternity Skin", "#FFB43E"),
        REMORSEFULSKIN("Remorseful Skin", "#EEE6D6"),
        GREEDSKIN("Greed Skin", "#DAAD3E"),
        MYTHIC("Mythic Reliquary", "#C4971A"),
        LOWTIDE("Lowtide Smuggler", "#196383"),
        ROYAL("Royal Armory", "#CAFFFD"),
        TRANSMOGRIFIER("Transmogrifier", "#6F2DA8"),
        SOUL("Soulwoven", "#7FFFD4"),
        TRUENORTH("True North", "#FFD700"),
        DOCKS("Expedition Docks", "#196383"),
        CARNIVAL("Floating Carnival", "#D02E28"),
        INTELLECT("Intellect Crystallizer", "#82DB17"),
        DELVES("Dungeon Delves", "#B47028"),
        BLITZ("Plunderer's Blitz", "#DAAD3E"),
        ;
        private final Component text;

        Location(String text, String color) {
            this.text = Component.text(text).color(colorFromString(color));
        }

        public Component getText() {
            return text;
        }
    }
}