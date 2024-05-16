package com.floweytf.customitemapi.helpers.tag;

import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class CachedTagApplicator implements TagApplicator {
    private final List<String> keys = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();

    public CachedTagApplicator() {
    }

    @Override
    public void put(String name, Tag tag) {
        keys.add(name);
        tags.add(tag.copy());
    }

    public void apply(TagApplicator applicator) {
        for (int i = 0; i < keys.size(); i++) {
            applicator.put(keys.get(i), tags.get(i).copy());
        }
    }
}
