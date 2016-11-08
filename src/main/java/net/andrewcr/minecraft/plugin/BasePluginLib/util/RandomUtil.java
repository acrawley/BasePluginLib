package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import java.util.List;

public class RandomUtil {
    public static int getRandomInt(int lessThan) {
        return (int) (Math.random() * lessThan);
    }

    public static <T> T getRandomElement(List<T> list) {
        return list.get(RandomUtil.getRandomInt(list.size()));
    }
}
