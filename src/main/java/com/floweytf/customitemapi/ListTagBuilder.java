package com.floweytf.customitemapi;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class ListTagBuilder implements ITagBuilder {
    private final ListTag tag = new ListTag();

    private ListTagBuilder() {
    }

    public static ListTagBuilder of() {
        return new ListTagBuilder();
    }

    public static ListTagBuilder of(Tag value) {
        return new ListTagBuilder().put(value);
    }

    public static ListTagBuilder of(ITagBuilder value) {
        return new ListTagBuilder().put(value.get());
    }

    public static ListTag build(Tag value) {
        return new ListTagBuilder().put(value).get();
    }

    public static ListTag build(ITagBuilder value) {
        return new ListTagBuilder().put(value.get()).get();
    }


    public ListTagBuilder put(Tag value) {
        tag.add(value);
        return this;
    }

    public ListTag get() {
        return tag;
    }
}