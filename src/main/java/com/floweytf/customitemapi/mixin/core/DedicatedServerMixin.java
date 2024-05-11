package com.floweytf.customitemapi.mixin.core;

import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import com.floweytf.customitemapi.impl.resource.PluginDataListener;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lorg/bukkit/craftbukkit/v1_20_R3/CraftServer;loadPlugins()V",
            shift = At.Shift.AFTER
        )
    )
    private void reloadDatapackHandlers(CallbackInfoReturnable<Boolean> cir) {
        PluginDataListener.INSTANCE.reload(true);
        CustomItemRegistryImpl.getInstance().freeze();
    }
}