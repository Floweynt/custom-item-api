package com.floweytf.customitemapi.api.resource;

import com.google.gson.JsonObject;
import org.bukkit.NamespacedKey;

import java.util.Map;

public interface DatapackResourceManager {
    Map<NamespacedKey, JsonObject> resources();
}
