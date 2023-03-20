package net.hypejet.ex.brush.brush.shape;

import net.minestom.server.coordinate.Vec;

import java.util.stream.Stream;

public class SphericalBlockShapeGenerator extends CubicBlockShapeGenerator {
    @Override
    public Stream<Vec> select(int radius) {
        return super.select(radius)
                    .filter(vec -> vec.distance(0, 0, 0) <= radius);
    }
}
