package com.floweytf.customitemapi.api;

public record Version(int major, int minor, int patch) {
    public static Version from(String str) {
        final var parts = str.split("\\.");
        if (parts.length == 2) {
            return new Version(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 0);
        } else if (parts.length == 3) {
            return new Version(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        } else {
            throw new RuntimeException("bad version " + str);
        }
    }

    public boolean isCompatibleImplementation(Version other) {
        if (other.major != major)
            return false;

        return other.minor >= minor;
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }
}
