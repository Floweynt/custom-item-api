package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.Pair;
import com.floweytf.customitemapi.api.DataLoaderRegistry;
import com.floweytf.customitemapi.api.resource.DatapackResourceLoader;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DataLoaderRegistryImpl implements DataLoaderRegistry {
    private static final DataLoaderRegistryImpl INSTANCE = new DataLoaderRegistryImpl();
    public final List<Pair<String, DatapackResourceLoader>> loaders = new ArrayList<>();

    private DataLoaderRegistryImpl() {

    }

    public static DataLoaderRegistryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void addDatapackLoader(@NotNull String prefix, @NotNull DatapackResourceLoader loader) {
        Preconditions.checkNotNull(prefix);
        Preconditions.checkNotNull(loader);

        if (prefix.matches("a-z0-9/\\._-")) {
            throw new IllegalArgumentException("bad path prefix fragment when registering datapack loader");
        }

        if (prefix.endsWith("/")) {
            throw new IllegalArgumentException("path prefix must not end with /");
        }

        loaders.add(new Pair<>(prefix, loader));
    }
}