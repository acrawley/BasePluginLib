package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationUtil {
    private static final String THETA_SYMBOL = "θ";
    private static final String PHI_SYMBOL = "φ";
    private static final String DECIMAL_EXPR = "-?\\d+(?:\\.\\d+)?";
    private static final String LOCATION_EXPR =
        "(?:(?<world>.+?)\\s*@\\s*)?" +         // Match optional world
            "(?<x>" + DECIMAL_EXPR + "),\\s*" + // Match X coordinate
            "(?<y>" + DECIMAL_EXPR + "),\\s*" + // Match Y coordinate
            "(?<z>" + DECIMAL_EXPR + ")" +      // Match Z coordinate
            "(?:,\\s*" + THETA_SYMBOL + "\\s*=\\s*(?<yaw>" + DECIMAL_EXPR + "))?" + // Match optional yaw
            "(?:,\\s*" + PHI_SYMBOL + "\\s*=\\s*(?<pitch>" + DECIMAL_EXPR + "))?";  // Match optional pitch

    private static final Pattern LOCATION_REGEX = Pattern.compile(LOCATION_EXPR);

    public static float getSignAngle(Sign sign) {
        BlockFace face = ((org.bukkit.material.Sign) sign.getData()).getFacing();
        return LocationUtil.blockFaceToYaw(face);
    }

    public static float blockFaceToYaw(BlockFace face) {
        switch (face) {
            case NORTH:
                return -180f;
            case NORTH_NORTH_EAST:
                return -157.5f;
            case NORTH_EAST:
                return -135f;
            case EAST_NORTH_EAST:
                return -112.5f;
            case EAST:
                return -90f;
            case EAST_SOUTH_EAST:
                return -67.5f;
            case SOUTH_EAST:
                return -45f;
            case SOUTH_SOUTH_EAST:
                return -22.5f;
            case SOUTH:
                return 0f;
            case SOUTH_SOUTH_WEST:
                return 22.5f;
            case SOUTH_WEST:
                return 45f;
            case WEST_SOUTH_WEST:
                return 67.5f;
            case WEST:
                return 90f;
            case WEST_NORTH_WEST:
                return 112.5f;
            case NORTH_WEST:
                return 135f;
            case NORTH_NORTH_WEST:
                return 157.5f;
        }

        return 0f;
    }

    public static Location findSafeLocationInColumn(Location startLocation) {
        Location location = LocationUtil.findSafeLocationInColumn(
            startLocation.getWorld(),
            startLocation.getBlockX(),
            startLocation.getBlockY(),
            startLocation.getBlockZ());

        if (location != null) {
            location.setYaw(startLocation.getYaw());
            location.setPitch(startLocation.getPitch());
        }

        return location;
    }

    public static Location findSafeLocationInColumn(World world, int x, int minY, int z) {
        boolean foundSolid = false;
        int airBlocks = 0;

        // Check this column of the world for the first solid block above a minimum height with at least two blocks of air above it
        // NOTE: This seems to occasionally run into trouble with trees
        for (int y = minY; y < 256; y++) {
            Material material = world.getBlockAt(x, y, z).getType();

            if (!foundSolid) {
                // Keep looking for a solid block
                foundSolid = !MaterialUtil.canOccupy(material);
            } else {
                // Found a solid block, now look for 2 blocks of air
                if (MaterialUtil.canOccupy(material)) {
                    airBlocks++;
                } else {
                    // Found non-air, start over
                    airBlocks = 0;
                }

                if (airBlocks == 2) {
                    // Found our two blocks, we're done
                    return new Location(world, x + 0.5, y - 1, z + 0.5);
                }
            }
        }

        return null;
    }

    public static Location locationFromString(String text) {
        try {
            Matcher twoProblems = LOCATION_REGEX.matcher(text);
            if (!twoProblems.matches()) {
                // Invalid format
                return null;
            }

            return new Location(
                twoProblems.group("world") != null ? Bukkit.getWorld(twoProblems.group("world")) : null,
                Double.parseDouble(twoProblems.group("x")),
                Double.parseDouble(twoProblems.group("y")),
                Double.parseDouble(twoProblems.group("z")),
                twoProblems.group("pitch") != null ? Double.valueOf(twoProblems.group("pitch")).floatValue() : 0,
                twoProblems.group("yaw") != null ? Double.valueOf(twoProblems.group("yaw")).floatValue() : 0
            );
        } catch (NumberFormatException ex) {
            // Invalid format
            return null;
        }
    }

    public static String locationToIntString(Location location, boolean includeWorld, boolean includeYaw, boolean includePitch) {
        return (includeWorld ? (location.getWorld().getName() + " @ ") : "")
            + location.getBlockX() + ", "
            + location.getBlockY() + ", "
            + location.getBlockZ()
            + LocationUtil.getYawPitchSuffix(location, includeYaw, includePitch);
    }

    public static String locationToDecimalString(Location location, boolean includeWorld, boolean includeYaw, boolean includePitch) {
        return (includeWorld ? (location.getWorld().getName() + " @ ") : "")
            + location.getX() + ", "
            + location.getY() + ", "
            + location.getZ()
            + LocationUtil.getYawPitchSuffix(location, includeYaw, includePitch);
    }

    private static String getYawPitchSuffix(Location location, boolean includeYaw, boolean includePitch) {
        return (includeYaw && location.getYaw() != 0 ? (", " + THETA_SYMBOL + " = " + location.getYaw()) : "")
            + (includePitch && location.getPitch() != 0 ? (", " + PHI_SYMBOL + " = " + location.getPitch()) : "");
    }
}
