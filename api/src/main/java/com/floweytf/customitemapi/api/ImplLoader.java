package com.floweytf.customitemapi.api;

import java.lang.reflect.InvocationTargetException;

class ImplLoader {
    static final Version API_VERSION = new Version(1, 0, 0);
    static final Version IMPL_VERSION;
    static CustomItemAPI INSTANCE;

    static {
        try {
            IMPL_VERSION = (Version) Class.forName("com.floweytf.customitemapi.CustomItemAPIMain")
                .getField("API_VERSION")
                .get(null);

            if (!API_VERSION.isCompatibleImplementation(IMPL_VERSION)) {
                throw new RuntimeException("Api version " + API_VERSION + " not compatible with " + IMPL_VERSION);
            }

            INSTANCE = (CustomItemAPI) Class.forName("com.floweytf.customitemapi.CustomItemAPIMain")
                .getMethod("getAPIInstance")
                .invoke(null);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
