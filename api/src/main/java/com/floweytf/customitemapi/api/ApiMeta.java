package com.floweytf.customitemapi.api;

import java.lang.reflect.InvocationTargetException;

class ApiMeta {
    public static final Version API_VERSION = new Version(1, 0, 0);
    public static final Version IMPL_VERSION;
    final static CustomItemRegistry REGISTRY;
    final static CustomItemAPI API;

    static {
        try {
            IMPL_VERSION =
                (Version) Class.forName("com.floweytf.customitemapi.CustomItemAPI").getField("API_VERSION").get(null);

            if (!API_VERSION.isCompatibleImplementation(IMPL_VERSION)) {
                throw new RuntimeException("Api version " + API_VERSION + " not compatible with " + IMPL_VERSION);
            }

            REGISTRY = (CustomItemRegistry) Class.forName("com.floweytf.customitemapi.impl.CustomItemRegistryImpl")
                .getMethod("getInstance").invoke(null);
            API = (CustomItemAPI) Class.forName("com.floweytf.customitemapi.impl.CustomItemAPIImpl")
                .getMethod("getInstance").invoke(null);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}