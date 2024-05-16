package com.floweytf.customitemapi.api.resource;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents access to datapack resources.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface DatapackResourceManager {
    /**
     * Obtains a list of all resources belonging to the {@link DatapackResourceLoader}.
     * The contents of this depends on the specific {@link DatapackResourceLoader}, as it is registered with a prefix.
     *
     * @return The list of resources starting with plugins/[registered-prefix]/
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    Map<NamespacedKey, JsonObject> resources();
}