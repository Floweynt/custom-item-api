package com.floweytf.customitemapi.mixin;

import com.floweytf.customitemapi.impl.CustomItemRegistryImpl;
import com.floweytf.customitemapi.impl.resource.PluginDataListener;
import com.mojang.datafixers.DataFixer;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin extends MinecraftServer {
    public DedicatedServerMixin(OptionSet options, WorldLoader.DataLoadContext worldLoader, Thread thread,
                                LevelStorageSource.LevelStorageAccess convertable_conversionsession,
                                PackRepository resourcepackrepository, WorldStem worldstem, Proxy proxy,
                                DataFixer datafixer, Services services,
                                ChunkProgressListenerFactory worldloadlistenerfactory) {
        super(options, worldLoader, thread, convertable_conversionsession, resourcepackrepository, worldstem, proxy,
            datafixer, services, worldloadlistenerfactory);
    }

    @Inject(
        method = "initServer",
        at = @At(
            value = "INVOKE",
            target = "Lorg/bukkit/craftbukkit/v1_19_R3/CraftServer;loadPlugins()V",
            shift = At.Shift.AFTER
        )
    )
    private void custom_item_api$reloadDatapackHandlers(CallbackInfoReturnable<Boolean> cir) {
        PluginDataListener.INSTANCE.reload(true);
        CustomItemRegistryImpl.getInstance().freeze();
    }
}