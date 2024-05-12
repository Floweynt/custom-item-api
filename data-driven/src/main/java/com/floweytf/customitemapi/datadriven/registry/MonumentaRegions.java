package com.floweytf.customitemapi.datadriven.registry;

public enum MonumentaRegions {
    VALLEY("King's Valley"),
    ISLES("Celsian Isles"),
    RING("Architect's Ring"),
    ;

    private final String name;

    MonumentaRegions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
