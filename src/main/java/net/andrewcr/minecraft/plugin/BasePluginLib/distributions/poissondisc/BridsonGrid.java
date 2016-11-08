package net.andrewcr.minecraft.plugin.BasePluginLib.distributions.poissondisc;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BridsonGrid {
    @Data
    private class GridLocation {
        public final int x;
        public final int y;
    }

    @Getter private int gridCellSize;

    private final Map<GridLocation, List<IPoissonDiscPoint>> grid;

    public BridsonGrid(Iterable<? extends IPoissonDiscPoint> points, int spacing) {
        this.grid = new HashMap<>();
        this.gridCellSize = (int) (spacing / Math.sqrt(2));

        for (IPoissonDiscPoint point : points) {
            this.addPointToGrid(point);
        }
    }

    public List<IPoissonDiscPoint> getPoints(int x, int y) {
        GridLocation loc = new GridLocation(x, y);
        if (this.grid.containsKey(loc)) {
            return this.grid.get(loc);
        }

        return null;
    }

    public void addPointToGrid(IPoissonDiscPoint point) {
        GridLocation loc = new GridLocation(
            point.getX() / this.gridCellSize,
            point.getY() / this.gridCellSize);

        List<IPoissonDiscPoint> points;
        if (this.grid.containsKey(loc)) {
            points = this.grid.get(loc);
        } else {
            points = new ArrayList<>();
            this.grid.put(loc, points);
        }

        points.add(point);
    }
}
