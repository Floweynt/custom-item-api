package com.floweytf.customitemapi.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public interface ExtraItemData {
    void setUnbreakable(boolean flag);
    void setBookPages(List<Component> text);
    void setBookGeneration(BookMeta.Generation generation);
    void setBookAuthor(String author);
    void setBookTitle(Component title);
}
