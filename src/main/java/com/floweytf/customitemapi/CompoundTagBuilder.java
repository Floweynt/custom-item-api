package com.floweytf.customitemapi;

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

    public CompoundTagBuilder put(String string, ITagBuilder value) {
        tag.put(string, value.get());
        return this;
    }

    public CompoundTagBuilder put(String string, Tag value) {
        tag.put(string, value);
        return this;
    }

    public CompoundTagBuilder put(String string, String value) {
        tag.putString(string, value);
        return this;
    }

    public CompoundTagBuilder put(String string, int value) {
        tag.putInt(string, value);
        return this;
    }

    public CompoundTagBuilder put(String string, byte value) {
        tag.putByte(string, value);
        return this;
    }

    public CompoundTag get() {
        return tag;
    }
}
