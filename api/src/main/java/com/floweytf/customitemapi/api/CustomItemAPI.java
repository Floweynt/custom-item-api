package com.floweytf.customitemapi.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * "Entrypoint" for this library. This is mostly implementation details.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface CustomItemAPI {
    /**
     * Obtains the implementation version.
     *
     * @return The implementation version.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static Version getImplVersion() {
        return ImplLoader.IMPL_VERSION;
    }

    /**
     * Obtains the api version.
     *
     * @return The api version.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static Version getApiVersion() {
        return ImplLoader.API_VERSION;
    }

    /**
     * Obtains the implementation's instance of CustomItemAPI
     *
     * @return The implementation.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static CustomItemAPI getInstance() {
        return ImplLoader.INSTANCE;
    }

    /**
     * Obtains the implementation for {@link CustomItemRegistry}
     *
     * @return The implementation.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    CustomItemRegistry getRegistryInstance();

    /**
     * Obtains the implementation for {@link CustomItems}
     *
     * @return The implementation.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    CustomItems getCustomItemsInstance();

    /**
     * Obtains the implementation for {@link DataLoaderRegistry}
     *
     * @return The implementation.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    DataLoaderRegistry getDatapacksInstance();
}