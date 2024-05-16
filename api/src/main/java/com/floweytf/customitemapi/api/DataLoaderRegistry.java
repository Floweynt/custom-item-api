package com.floweytf.customitemapi.api;

import com.floweytf.customitemapi.api.resource.DatapackResourceLoader;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The main entrypoint into this library.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface DataLoaderRegistry {
    /**
     * Obtains an instance of the API.
     *
     * @return The API.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    static DataLoaderRegistry getInstance() {
        return CustomItemAPI.getInstance().getDatapacksInstance();
    }

    /**
     * Registers a datapack resource loader under a specific prefix.
     *
     * @param prefix The prefix for resources. The actual prefix (in datapack file) would be [ns]/plugin/[prefix].
     * @param loader The datapack loader instance, to which load event will be passed.
     * @author Floweynt
     * @since 1.0.0
     */
    void addDatapackLoader(@NotNull String prefix, @NotNull DatapackResourceLoader loader);
}