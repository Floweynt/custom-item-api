package com.floweytf.customitemapi.datadriven.json;

import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.item.CustomItem;
import com.floweytf.customitemapi.datadriven.Pair;
import com.floweytf.customitemapi.datadriven.Utils;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.NamespacedKey;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JsonCustomItem {
    public static final Style DEFAULT_LORE_STYLE = Style.style(NamedTextColor.DARK_GRAY)
        .decoration(TextDecoration.ITALIC, false);
    public static final Style NO_ITALIC = Style.empty().decoration(TextDecoration.ITALIC, false);
    private static final Logger LOGGER = LogManager.getLogger("CustomItemAPI/DataDriven/JSON");

    private static Supplier<CustomItem> fromFragment(JsonItemFragment fragment) {
        if (fragment.isStateless()) {
            final var instance = new StatelessJsonCustomItem(fragment);
            return () -> instance;
        }

        throw new NotImplementedException("not implemented");
    }

    public static void readFromJson(CustomItemRegistry registry, JsonObject resource, NamespacedKey id) {
        // we are defining a variant...
        if (resource.has("variants")) {
            // attempt to read base...
            final var base = Utils.tryGetJsonElement(resource, "base")
                .map(x -> JsonItemFragment.fromTree(id, x.getAsJsonObject()));

            // read variants
            final var variantDef = resource.getAsJsonObject("variants")
                .asMap()
                .entrySet()
                .stream()
                .map(kv -> new Pair<>(kv.getKey(), JsonItemFragment.fromTree(id, kv.getValue().getAsJsonObject())))
                .map(kv -> new Pair<>(kv.first(), base.map(b -> kv.second().merge(b)).orElseGet(kv::second)))
                .collect(Collectors.toMap(Pair::first, Pair::second));

            final var defaultVariant = resource.get("default").getAsString();

            if (!variantDef.containsKey(defaultVariant)) {
                LOGGER.warn("when loading variant set {}: default variant specification must refer to a valid variant", id);
                return;
            }

            final var variantSet = registry.defineVariant(id);
            variantDef.forEach((key, value) -> {
                final var type = variantSet.register(key, fromFragment(value), value.baseItem().orElseThrow(),
                    value.isStateless());
                if (key.equals(defaultVariant)) {
                    variantSet.setDefaultVariant(type);
                }
            });
        } else {
            final var data = JsonItemFragment.fromTree(id, resource);

            final var factory = fromFragment(data);

            registry.registerSimple(id, factory, data.baseItem().orElseThrow(), true);
        }
    }
}