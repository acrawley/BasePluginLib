package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import org.bukkit.entity.EntityType;

import java.util.Arrays;

public class EntityUtil {
    public static boolean isMob(EntityType entityType) {
        return EntityUtil.isHostileMob(entityType) || EntityUtil.isFriendlyMob(entityType);
    }

    public static boolean isHostileMob(EntityType entityType) {
        switch (entityType) {
            case ENDERMAN:
            case SPIDER:
            case CAVE_SPIDER:
            case PIG_ZOMBIE:
            case BLAZE:
            case CREEPER:
            case ENDERMITE:
            case GHAST:
            case GUARDIAN:
            case MAGMA_CUBE:
            case SHULKER:
            case SILVERFISH:
            case SKELETON:
            case SLIME:
            case WITCH:
            case ZOMBIE:
            case ENDER_DRAGON:
            case WITHER:
            case GIANT:
                return true;

            default:
                return false;
        }
    }

    public static boolean isFriendlyMob(EntityType entityType) {
        switch (entityType) {
            case BAT:
            case CHICKEN:
            case COW:
            case MUSHROOM_COW:
            case PIG:
            case RABBIT:
            case SHEEP:
            case SQUID:
            case VILLAGER:
            case POLAR_BEAR:
            case WOLF:
            case OCELOT:
            case HORSE:
            case SNOWMAN:
            case IRON_GOLEM:
                return true;

            default:
                return false;
        }
    }

    public static EntityType tryGetEntityTypeByName(String name) {
        return Arrays.stream(EntityType.values())
            .filter(e -> name.equalsIgnoreCase(e.name()))
            .findAny()
            .orElse(null);
    }
}
