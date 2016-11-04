package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import lombok.Data;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
public class Version {
    private final int major;
    private final int minor;
    private final int build;
    private final int revision;

    public Version(int major, int minor) {
        this(major, minor, -1, -1);
    }

    public Version(int major, int minor, int build) {
        this(major, minor, build, -1);
    }

    public Version(int major, int minor, int build, int revision) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
    }

    public boolean isOlderThan(Version other) {
        if (this.major < other.major ||
            this.minor < other.minor ||
            (this.build != -1 && other.build != -1 && this.build < other.build) ||
            (this.revision != -1 && other.revision != -1 && this.revision < other.revision)) {
            return true;
        }

        return false;
    }

    public static Version tryParse(String text) {
        String[] parts = text.split("\\.");
        if (parts.length < 2 || parts.length > 4) {
            return null;
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int build = -1;
            int revision = -1;

            if (parts.length > 2) {
                build = Integer.parseInt(parts[2]);
            }

            if (parts.length > 3) {
                revision = Integer.parseInt(parts[3]);
            }

            return new Version(major, minor, build, revision);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        if (major == -1) {
            return super.toString();
        }

        return Arrays.stream(new int[] { this.major, this.minor, this.build, this.revision })
            .filter(i -> i != -1)
            .mapToObj(i -> Integer.toString(i))
            .collect(Collectors.joining("."));
    }
}
