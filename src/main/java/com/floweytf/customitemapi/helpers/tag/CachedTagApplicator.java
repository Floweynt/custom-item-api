package com.floweytf.customitemapi.helpers.tag;

import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * A tag applicator that caches NBT applications actions.
 * This is typically used by stateless items to initialize the item stack tags.
 */
public class CachedTagApplicator implements TagApplicator {
    private final List<String> keys = new ArrayList<>();
    private final List<Tag> tags = new ArrayList<>();

    public CachedTagApplicator() {
    }

    @Override
    public void put(String key, Tag tag) {
        keys.add(key);
        tags.add(tag.copy());
    }

    public void apply(TagApplicator target) {
        for (int i = 0; i < keys.size(); i++) {
            target.put(keys.get(i), tags.get(i).copy());
        }
    }
}
