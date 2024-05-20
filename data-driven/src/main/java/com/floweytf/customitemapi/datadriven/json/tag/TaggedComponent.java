package com.floweytf.customitemapi.datadriven.json.tag;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;

public interface TaggedComponent {
    default void putComponentsStart(ComponentWriter output) {

    }

    default void putComponentsEnd(ComponentWriter output) {

    }

    default void configure(ExtraItemData data) {

    }
}