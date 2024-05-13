package com.floweytf.customitemapi;

import java.util.Optional;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> impl;
    private Optional<T> cache = Optional.empty();

    public Lazy(Supplier<T> supplier) {
        this.impl = supplier;
    }

    @Override
    public T get() {
        if (cache.isEmpty()) {
            cache = Optional.of(impl.get());
        }

        return cache.get();
    }
}
