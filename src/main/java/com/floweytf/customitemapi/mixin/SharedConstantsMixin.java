package com.floweytf.customitemapi.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
    @Shadow
    public static boolean IS_RUNNING_IN_IDE;

    static {
        IS_RUNNING_IN_IDE = System.getenv("IS_DEVENV").equals("1");
    }
}
