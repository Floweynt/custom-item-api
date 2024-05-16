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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Defines custom behaviour and data for a {@link ItemStack}.
 * Each {@link ItemStack} with custom behavior will have a bound {@link CustomItem}.
 * As such, this class behaves more like an {@link ItemStack} than {@link org.bukkit.Material}.
 * The library itself makes no requirement of uniqueness of {@link CustomItem}.
 * A {@link CustomItem} with no {@link ItemStack}-bound state should be shared in order to minimize memory usage.
 * Such an {@link CustomItem} should also be marked as stateless in order to opt into caching features.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
public interface CustomItem {
    /**
     * Extension point for setting the lore.
     *
     * @return A list of lore entries, 1 element for each line.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    default Optional<List<Component>> getLore() {
        return Optional.empty();
    }

    /**
     * Extension point for setting the display name.
     *
     * @return The display name for this item.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    default Optional<Component> getTitle() {
        return Optional.empty();
    }

    /**
     * Extension point for setting {@link Attribute}s.
     *
     * @return A map between attributes and the attribute modifier instance.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    default Multimap<Attribute, AttributeModifier> getBaseAttributes() {
        return ImmutableMultimap.of();
    }

    /**
     * Extension point for hiding various parts of the item's tooltip.
     *
     * @return The list of components to hide.
     * @author Floweynt
     * @since 1.0.0
     */
    @NotNull
    default List<ItemFlag> hideFlags() {
        return List.of();
    }

    /**
     * Extension point for configuring misc properties.
     *
     * @param extraData The item data to configure.
     * @author Floweynt
     * @see ExtraItemData
     * @since 1.0.0
     */
    default void configureExtra(@NotNull ExtraItemData extraData) {

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