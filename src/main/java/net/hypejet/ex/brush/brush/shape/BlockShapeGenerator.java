package net.hypejet.ex.brush.brush.shape;

import net.minestom.server.coordinate.Vec;

import java.util.stream.Stream;

public interface BlockShapeGenerator {
    Stream<Vec> select(int radius);
}
