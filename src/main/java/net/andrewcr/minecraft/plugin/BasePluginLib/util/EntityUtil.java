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
            case ELDER_GUARDIAN:
            case ENDERMITE:
            case EVOKER:
            case GHAST:
            case GUARDIAN:
            case HUSK:
            case MAGMA_CUBE:
            case SHULKER:
            case SILVERFISH:
            case SKELETON:
            case SLIME:
            case STRAY:
            case VEX:
            case VINDICATOR:
            case WITCH:
            case WITHER_SKELETON:
            case ZOMBIE:
            case ZOMBIE_VILLAGER:
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
            case SKELETON_HORSE:
            case SQUID:
            case VILLAGER:
            case POLAR_BEAR:
            case DONKEY:
            case LLAMA:
            case MULE:
            case OCELOT:
            case WOLF:
            case HORSE:
            case IRON_GOLEM:
            case SNOWMAN:
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
