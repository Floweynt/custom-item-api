package com.floweytf.customitemapi.impl.resource;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExtraItemDataImpl implements ExtraItemData {
    private boolean unbreakable = false;
    private List<Component> text = null;
    private BookMeta.Generation generation = null;
    private String author = null;
    private Component title = null;

    @Override
    public void setBookGeneration(BookMeta.@NotNull Generation generation) {
        Preconditions.checkNotNull(generation);
        this.generation = generation;
    }

    @Override
    public void setBookAuthor(@NotNull String author) {
        Preconditions.checkNotNull(author);
        this.author = author;
    }

    @Override
    public void setBookTitle(@NotNull Component title) {
        Preconditions.checkNotNull(title);
        this.title = title;
    }

    public Component getTitle() {
        return title;
    }

    public BookMeta.Generation getGeneration() {
        return generation;
    }

    public List<Component> getBookPages() {
        return text;
    }

    @Override
    public void setBookPages(@NotNull List<Component> pages) {
        Preconditions.checkNotNull(pages);

        this.text = new ArrayList<>(pages);
    }

    public String getAuthor() {
        return author;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }
}