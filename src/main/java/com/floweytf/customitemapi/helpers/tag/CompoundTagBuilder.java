package com.floweytf.customitemapi.helpers.tag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class CompoundTagBuilder implements ITagBuilder {
    private final CompoundTag tag = new CompoundTag();

    private CompoundTagBuilder() {
    }

    public static CompoundTagBuilder of() {
        return new CompoundTagBuilder();
    }

    public static CompoundTagBuilder of(String key, Tag value) {
        return new CompoundTagBuilder().put(key, value);
    }

    public static CompoundTagBuilder of(String key, ITagBuilder value) {
        return new CompoundTagBuilder().put(key, value.get());
    }

    public static CompoundTag build(String key, Tag value) {
        return new CompoundTagBuilder().put(key, value).get();
    }

    public static CompoundTag build(String key, ITagBuilder value) {
        return new CompoundTagBuilder().put(key, value.get()).get();
    }

    public CompoundTagBuilder put(String key, ITagBuilder value) {
        tag.put(key, value.get());
        return this;
    }

    public CompoundTagBuilder put(String key, Tag value) {
        tag.put(key, value);
        return this;
    }

    public CompoundTagBuilder put(String key, String value) {
        tag.putString(key, value);
        return this;
    }

    public CompoundTagBuilder put(String key, int value) {
        tag.putInt(key, value);
        return this;
    }

    public CompoundTagBuilder put(String key, byte value) {
        tag.putByte(key, value);
        return this;
    }

    public CompoundTag get() {
        return tag;
    }
}