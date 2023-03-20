package net.hypejet.ex.brush.brush;

import net.minestom.server.coordinate.Vec;

import java.util.stream.Stream;

public interface Selector {
    Stream<Vec> select(int radius);
}
