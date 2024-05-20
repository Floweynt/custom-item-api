package com.floweytf.customitemapi.api.item;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Extra misc data associated with an {@link org.bukkit.inventory.ItemStack}.
 *
 * @author Floweynt
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public interface ExtraItemData {
    /**
     * Sets the item to be unbreakable.
     *
     * @param unbreakable Whether the item should be unbreakable. False does not do anything.
     * @author Floweynt
     * @since 1.0.0
     */
    void setUnbreakable(boolean unbreakable);

    /**
     * Sets the book pages components.
     *
     * @param pages The list of pages, with one element per page.
     * @author Floweynt
     * @since 1.0.0
     */
    void setBookPages(@NotNull List<Component> pages);

    /**
     * Sets the generation of the book.
     *
     * @param generation The generation of the book.
     * @author Floweynt
     * @since 1.0.0
     */
    void setBookGeneration(@NotNull BookMeta.Generation generation);

    /**
     * Sets the author of the book.
     *
     * @param author The author's name. This cannot be a component.
     * @author Floweynt
     * @since 1.0.0
     */
    void setBookAuthor(@NotNull String author);

    /**
     * Sets the title of the book.
     *
     * @param title The title of the book.
     * @author Floweynt
     * @since 1.0.0
     */
    void setBookTitle(@NotNull Component title);

    /**
     * Adds additional tags with NBT API. This is merged with all the other tags, with this taking priority.
     *
     * @author Floweynt
     * @since 1.0.0
     */
    ReadWriteNBT getNBTTag();
}