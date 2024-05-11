package com.floweytf.customitemapi.api.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Defines custom behaviour and data for a {@link ItemStack}.
 */
public interface CustomItem {
    // Item Appearance
    default Optional<List<Component>> getLore() {
        return Optional.empty();
    }

    default Optional<Component> getTitle() {
        return Optional.empty();
    }

    default Multimap<Attribute, AttributeModifier> getBaseAttributes() {
        return ImmutableMultimap.of();
    }

    default List<ItemFlag> hideFlags() {
        return List.of();
    }

    // player interaction events
    default void onRightClick(Player actor, ItemStack rawItem, Block block) {
    }

    default void onLeftClick(Player actor, ItemStack rawItem, Block block) {
    }

    default void onRightClickBlock(Player actor, ItemStack rawItem, Block block, BlockFace face) {
    }

    default void onLeftClickBlock(Player actor, ItemStack rawItem, Block block, BlockFace face) {
    }

    default void onPlace(Player actor, ItemStack rawItem) {
    }

    default void onBreak(Player actor, ItemStack rawItem) {

    }

    default void onConsume(Player actor, ItemStack rawItem) {
    }

    // world events
    default void onDispense(ItemStack rawItem, Block dispenser) {

    }
}