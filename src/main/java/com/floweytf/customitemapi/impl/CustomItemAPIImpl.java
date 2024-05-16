package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.api.CustomItemAPI;
import com.floweytf.customitemapi.api.CustomItemRegistry;
import com.floweytf.customitemapi.api.CustomItems;
import com.floweytf.customitemapi.api.DataLoaderRegistry;
import org.jetbrains.annotations.NotNull;

public class CustomItemAPIImpl implements CustomItemAPI {
    private static final CustomItemAPIImpl INSTANCE = new CustomItemAPIImpl();

    private CustomItemAPIImpl() {

    }

    public static CustomItemAPIImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull CustomItemRegistry getRegistryInstance() {
        return CustomItemRegistryImpl.getInstance();
    }

    @Override
    public @NotNull CustomItems getCustomItemsInstance() {
        return CustomItemsImpl.getInstance();
    }

    @Override
    public @NotNull DataLoaderRegistry getDatapacksInstance() {
        return DataLoaderRegistryImpl.getInstance();
    }
}
