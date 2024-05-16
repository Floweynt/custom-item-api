package com.floweytf.customitemapi.mixin;

import com.floweytf.customitemapi.access.ItemStackAccess;
import com.floweytf.customitemapi.helpers.ItemStackStateManager;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.bukkit.craftbukkit.v1_19_R3.CraftEquipmentSlot;
import org.bukkit.craftbukkit.v1_19_R3.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.v1_19_R3.attribute.CraftAttributeMap;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackAccess {
    @Unique
    private final ItemStackStateManager custom_item_api$stateManager = new ItemStackStateManager();

    @Shadow
    @javax.annotation.Nullable
    private Item item;

    // Item state loading logic
    // TODO: this might be expensive
    @Inject(
        method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V",
        at = @At("TAIL")
    )
    private void custom_item_api$computeItemOnInit(ItemLike item, int count, CallbackInfo ci) {
        custom_item_api$stateManager.loadCustomState(((ItemStack) (Object) this));
    }

    @Inject(
        method = "load",
        at = @At("RETURN")
    )
    private void custom_item_api$loadCustomItemData$load(CompoundTag tag, CallbackInfo ci) {
        custom_item_api$stateManager.loadCustomState((ItemStack) (Object) this);
    }

    @Inject(
        method = "setTag",
        at = @At("RETURN")
    )
    private void custom_item_api$loadCustomItemData$setTag(CompoundTag tag, CallbackInfo ci) {
        custom_item_api$stateManager.loadCustomState((ItemStack) (Object) this);
    }

    // Fail on set item
    // TODO: this may cause crashes in some cases
    @Inject(
        method = "setItem",
        at = @At("HEAD")
    )
    private void custom_item_api$computeItemOnSetItem(Item item, CallbackInfo ci) {
        throw new UnsupportedOperationException();
    }

    // Handling saving logic
    @Inject(
        method = "save",
        at = @At("HEAD")
    )
    private void custom_item_api$recomputeOnSave(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        custom_item_api$stateManager.storeCustomState((ItemStack) (Object) this);
    }

    @Override
    public @Nullable ItemStackStateManager custom_item_api$getStateManager() {
        return custom_item_api$stateManager;
    }

    @Override
    public void custom_item_api$setItemRaw(Item item) {
        this.item = item;
    }

    @ModifyReturnValue(
        at = @At("RETURN"),
        method = "getAttributeModifiers"
    )
    public final Multimap<Attribute, AttributeModifier> custom_item_api$addAttributes(Multimap<Attribute,
        AttributeModifier> returnValue,
                                                                                      EquipmentSlot slot) {
        final var state = custom_item_api$stateManager.getCustomState();
        if (state == null)
            return returnValue;

        for (var attr : state.item().getBaseAttributes().entries()) {
            if (CraftEquipmentSlot.getNMS(Objects.requireNonNull(attr.getValue().getSlot())) != slot)
                continue;
            returnValue.put(
                CraftAttributeMap.toMinecraft(attr.getKey()),
                CraftAttributeInstance.convert(attr.getValue())
            );
        }

        return returnValue;
    }
}