package com.floweytf.customitemapi.datadriven.json.tag;

import com.floweytf.customitemapi.api.item.ExtraItemData;
import com.floweytf.customitemapi.datadriven.json.ComponentWriter;

public abstract class SimpleTaggedComponent<C extends TaggedComponentConfig<C>> implements TaggedComponent {
    protected final C config;

    protected SimpleTaggedComponent(C config) {
        this.config = config;
    }
}