package com.floweytf.customitemapi.impl;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class ExtraItemDataImpl implements ExtraItemData {
    private boolean unbreakable = false;
    private List<Component> text = null;
    private BookMeta.Generation generation = null;
    private String author = null;
    private Component title = null;

    @Override
    public void setUnbreakable(boolean flag) {
        unbreakable = flag;
    }

    @Override
    public void setBookPages(List<Component> text) {
        if (text == null) {
            this.text = null;
            return;
        }

        this.text = new ArrayList<>(text);
    }

    @Override
    public void setBookGeneration(BookMeta.Generation generation) {
        this.generation = generation;
    }

    @Override
    public void setBookAuthor(String author) {
        this.author = author;
    }

    @Override
    public void setBookTitle(Component title) {
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

    public String getAuthor() {
        return author;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

}
