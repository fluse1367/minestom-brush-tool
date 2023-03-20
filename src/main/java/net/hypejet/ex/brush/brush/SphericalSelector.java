package net.hypejet.ex.brush.brush;

import net.minestom.server.coordinate.Vec;

import java.util.stream.Stream;

public class SphericalSelector extends CubicSelector {
    @Override
    public Stream<Vec> select(int radius) {
        return super.select(radius)
                    .filter(vec -> vec.distance(0, 0, 0) <= radius);
    }
}
