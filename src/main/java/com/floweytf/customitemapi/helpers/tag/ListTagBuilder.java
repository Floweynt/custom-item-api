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

    public static ListTagBuilder of(Tag... tags) {
        return new ListTagBuilder().put(tags);
    }

    public static ListTagBuilder of(ITagBuilder tag) {
        return new ListTagBuilder().put(tag.get());
    }

    public static ListTag build(Tag tag) {
        return new ListTagBuilder().put(tag).get();
    }

    public static ListTag build(ITagBuilder tag) {
        return new ListTagBuilder().put(tag.get()).get();
    }

    public static ListTag of(Stream<Tag> tags) {
        final var instance = new ListTagBuilder();
        tags.forEach(instance::put);
        return instance.get();
    }

    public ListTagBuilder put(Tag... tags) {
        Collections.addAll(tag, tags);
        return this;
    }

    public ListTag get() {
        return tag;
    }
}