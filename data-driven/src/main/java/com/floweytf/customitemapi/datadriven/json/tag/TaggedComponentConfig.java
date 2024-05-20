package com.floweytf.customitemapi.datadriven.json.tag;

import javax.annotation.Nullable;

public interface TaggedComponentConfig<T extends TaggedComponentConfig<T>> {
    default boolean isStateless() {
        return true;
    }

    @Nullable
    T tryMerge(T other);
}