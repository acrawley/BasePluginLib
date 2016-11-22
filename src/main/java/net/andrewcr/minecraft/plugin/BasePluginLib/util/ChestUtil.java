package net.andrewcr.minecraft.plugin.BasePluginLib.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.util.Vector;

public class ChestUtil {
    public static Chest getChest(Location location) {
        Vector[] offsets = {
            new Vector(-1, 0, 0), // One block west
            new Vector(0, 0, 1)  // One block south
        };

        if (location == null) {
            return null;
        }

        Block startBlock = location.getBlock();
        if (startBlock == null || !(startBlock.getState() instanceof Chest)) {
            // Location wasn't a chest
            return null;
        }

        Chest startChest = (Chest)startBlock.getState();
        Material startMaterial = startChest.getData().getItemType();

        // Always refer to a double chest by the most south-west component to avoid ambiguity
        // NOTE: The game prevents chests from being placed such that two adjacent chests do
        //  not form a double chest, so we don't have to worry about that.  Trapped chests can
        //  be placed next to standard chests, so do we have to check for that.
        for (Vector offset : offsets) {
            Location candidate = location.clone();
            candidate.add(offset);
            Block candidateBlock = candidate.getBlock();
            if (candidateBlock != null && candidateBlock.getState() instanceof Chest)
            {
                Chest candidateChest = (Chest)candidateBlock.getState();

                if (candidateChest.getData().getItemType() == startMaterial) {
                    // Found another chest of the same type to the south or west, so that must be the origin
                    return (Chest) candidateBlock.getState();
                }
            }
        }

        // No chest to the south or west, so we must have started at the origin
        return startChest;
    }
}
