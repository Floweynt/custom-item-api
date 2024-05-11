package com.floweytf.customitemapi;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Utils {
    public static <T, U> @Nullable U m(@Nullable T value, Function<T, @Nullable U> mapper) {
        if (value == null)
            return null;
        return mapper.apply(value);
    }
}
