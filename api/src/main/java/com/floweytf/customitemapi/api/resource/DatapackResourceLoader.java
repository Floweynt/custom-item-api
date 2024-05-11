package com.floweytf.customitemapi.api.resource;

public interface DatapackResourceLoader {
    void load(DatapackResourceManager manager);

    default boolean canReload() {
        return false;
    }
}