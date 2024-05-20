package com.floweytf.customitemapi.datadriven.json.tag;

import com.google.gson.JsonObject;

import java.util.function.Function;

public record TaggedComponentType<C extends TaggedComponentConfig<C>, T extends TaggedComponent>(Function<JsonObject, C> parser,
                                                                                                    Function<C, T> factory) {
}