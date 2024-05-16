package com.floweytf.customitemapi;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Utils {
    public static <T, U> @Nullable U mapNull(@Nullable T value, Function<T, @Nullable U> mapper) {
        if (value == null)
            return null;
        return mapper.apply(value);
    }

    public static long profile(Runnable func) {
        long start = System.currentTimeMillis();
        func.run();
        long end = System.currentTimeMillis();
        return end - start;
    }
}
