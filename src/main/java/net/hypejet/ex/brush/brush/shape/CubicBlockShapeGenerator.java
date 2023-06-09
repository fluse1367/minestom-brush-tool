package net.hypejet.ex.brush.brush.shape;

import net.minestom.server.coordinate.Vec;

import java.util.stream.Stream;

public class CubicBlockShapeGenerator implements BlockShapeGenerator {
    @Override
    public Stream<Vec> select(int radius) {
        var b = Stream.<Vec>builder();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    b.add(new Vec(x, y, z));
                }
            }
        }

        return b.build();
    }
}
