package com.floweytf.customitemapi.helpers.tag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class TagBackedTagApplicator implements TagApplicator{
    private final CompoundTag tag;

    public TagBackedTagApplicator(CompoundTag tag) {
        this.tag = tag;
    }

    @Override
    public void put(String name, Tag tag) {
        this.tag.put(name, tag);
    }
}
