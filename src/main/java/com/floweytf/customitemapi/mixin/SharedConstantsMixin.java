package com.floweytf.customitemapi.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
    @Shadow
    public static boolean IS_RUNNING_IN_IDE;

    static {
        final var isDevEnv = System.getenv("IS_DEVENV");
        IS_RUNNING_IN_IDE = isDevEnv != null && isDevEnv.equals("1");
    }
}
