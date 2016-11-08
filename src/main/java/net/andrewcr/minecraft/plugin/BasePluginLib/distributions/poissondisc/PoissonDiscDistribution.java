package net.andrewcr.minecraft.plugin.BasePluginLib.distributions.poissondisc;

import net.andrewcr.minecraft.plugin.BasePluginLib.util.RandomUtil;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PoissonDiscDistribution {
    private static final int MAX_ATTEMPTS = 30;

    private final Iterable<? extends IPoissonDiscPoint> pointSet;
    private BridsonGrid grid;
    private int spacing;

    public PoissonDiscDistribution(Iterable<? extends IPoissonDiscPoint> pointSet, int spacing) {
        this.pointSet = pointSet;
        this.spacing = spacing;
    }

    public void setSpacing(int value) {
        this.grid = null;
        this.spacing = value;
    }

    public Point getNextPoint() {
        if (this.grid == null) {
            this.grid = new BridsonGrid(this.pointSet, this.spacing);
        }

        // Build the "active" list of all spawn points that haven't yet failed to generate a nearby point
        List<IPoissonDiscPoint> active = StreamSupport.stream(this.pointSet.spliterator(), false)
            .filter(p -> !p.getIsSearched())
            .collect(Collectors.toList());

        while (!active.isEmpty()) {
            IPoissonDiscPoint current = RandomUtil.getRandomElement(active);

            for (int i = 0; i < MAX_ATTEMPTS; i++) {
                // Choose a point lying at a distance of between r and 2r from the current point
                double phi = 2 * Math.PI * Math.random();
                double r = this.spacing + (Math.random() * this.spacing);

                // Convert from polar to rectangular
                int x = current.getX() + (int) (r * Math.cos(phi));
                int y = current.getY() + (int) (r * Math.sin(phi));

                // Check distance against points in nearby grid locations
                if (this.evaluatePoint(x, y)) {
                    // Found a good point!
                    return new Point(x, y);
                }
            }

            // Failed to find a nearby point that meets our criteria - mark the active point as thoroughly searched and try the next
            current.setIsSearched(true);
            active.remove(current);
        }

        // Should Never Happen™
        throw new AssertionError("PoissonDiscDistribution failed to generate a point!");
    }

    public void addPoint(IPoissonDiscPoint point) {
        if (this.grid != null) {
            this.grid.addPointToGrid(point);
        }
    }

    public void notifyPointChanged(IPoissonDiscPoint point) {
        // Point has changed - we need to rebuild the grid and make the point searchable again
        point.setIsSearched(false);
        this.grid = null;
    }

    private boolean evaluatePoint(int x, int y) {
        int gridX = x / this.grid.getGridCellSize();
        int gridY = y / this.grid.getGridCellSize();

        // Bridson's algorithm speeds up the generation of a Poisson disc distribution with a grid sized so each cell can
        //  contain at most one point without violating the spacing constraints.  When checking a newly-generated point
        //  against the constraints, only the neighboring grid cells need be considered.  The default algorithm assumes that
        //  the grid size is fixed and finite, however, and we allow the spacing to be changed after points have been generated,
        //  so each cell of our grid will contain a list of points instead.  This still avoids having to check each point against
        //  all other points.
        // See: http://www.cs.ubc.ca/~rbridson/docs/bridson-siggraph07-poissondisk.pdf

        for (int gx = gridX - 3; gx <= gridX + 3; gx++) {
            for (int gy = gridY - 3; gy <= gridY + 3; gy++) {
                List<IPoissonDiscPoint> points = this.grid.getPoints(gx, gy);
                if (points == null) {
                    continue;
                }

                for (IPoissonDiscPoint point : points) {
                    int dx = point.getX() - x;
                    int dy = point.getY() - y;

                    if ((dx * dx) + (dy * dy) < (this.spacing * this.spacing)) {
                        // Too close to another point, bail
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
