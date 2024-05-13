package com.floweytf.customitemapi.helpers.tag;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Collections;
import java.util.stream.Stream;

public class ListTagBuilder implements ITagBuilder {
    private final ListTag tag = new ListTag();

    private ListTagBuilder() {
    }

    public static ListTagBuilder of() {
        return new ListTagBuilder();
    }

    public static ListTagBuilder of(Tag... value) {
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

    public static ListTag of(Stream<Tag> tags) {
        final var instance = new ListTagBuilder();
        tags.forEach(instance::put);
        return instance.get();
    }

    public ListTagBuilder put(Tag... values) {
        Collections.addAll(tag, values);
        return this;
    }

    public ListTag get() {
        return tag;
    }
}