package net.hypejet.ex.brush.brush;

import eu.software4you.ulib.core.reflect.ReflectUtil;
import eu.software4you.ulib.core.tuple.Tuple;
import eu.software4you.ulib.core.util.Expect;
import net.hypejet.ex.brush.BrushExtension;
import net.hypejet.ex.brush.brush.place.BlockPlacer;
import net.hypejet.ex.brush.brush.shape.*;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;

public record Brush(@NotNull Brush.Shape shape, int radius, @NotNull BlockPlacer placer) {

    private static final Tag<String> TAG_BRUSH_TYPE = Tag.String("brush");
    private static final Tag<NBT> TAG_PLACER = Tag.NBT("placer");
    private static final Tag<Integer> TAG_RADIUS = Tag.Integer("radius");

    public enum Shape {
        cuboid(new CubicBlockShapeGenerator()),
        sphere(new SphericalBlockShapeGenerator());

        private final BlockShapeGenerator blockShapeGenerator;

        Shape(BlockShapeGenerator blockShapeGenerator) {this.blockShapeGenerator = blockShapeGenerator;}
    }

    /**
     * Attempts to deserialize an item stack to a brush object.
     *
     * @param stack the item stack
     * @return an optional wrapping the brush object on success, otherwise an empty optional
     */
    @NotNull
    public static Optional<Brush> fromItemStack(@NotNull ItemStack stack) {
        if (stack.material() != BrushExtension.BRUSH_MATERIAL) return empty();

        var type = Expect.ofNullable(stack.getTag(TAG_BRUSH_TYPE))
                         .map(str -> ReflectUtil.getEnumEntry(Shape.class, str))
                         .orElse(null);
        if (type == null) return empty();

        var placer = Expect.ofNullable(stack.getTag(TAG_PLACER))
                           .map(nbt -> BlockPlacer.deserialize((NBTCompound) nbt))
                           .map(op -> op.orElse(null))
                           .orElse(null);
        if (placer == null) return empty();

        var radius = Expect.ofNullable(stack.getTag(TAG_RADIUS)).orElse(null);
        if (radius == null) return empty();

        return of(new Brush(type, radius, placer));
    }

    /**
     * Serializes the brush object to an item stack.
     *
     * @return the serialized item stack
     */
    @NotNull
    public ItemStack toItemStack() {
        return ItemStack.builder(BrushExtension.BRUSH_MATERIAL)
                        .displayName(placer.name())
                        .set(TAG_BRUSH_TYPE, shape.name())
                        .set(TAG_PLACER, placer.serialize())
                        .set(TAG_RADIUS, radius)
                        .lore(text("Brush: ", GREEN).append(text(shape.name(), GRAY)),
                              text("Radius: ", GREEN).append(text(radius, GRAY)),
                              placer.description()
                        ).build();
    }

    public void brush(@NotNull Point center, Instance instance) {
        var batch = new RelativeBlockBatch();
        shape.blockShapeGenerator.select(radius)
                                 .map(vec -> Tuple.of(vec, new Pos(center.add(vec))))
                                 .forEach(pair -> placer.place(batch, instance, pair.getFirst(), pair.getSecond()));
        batch.apply(instance, center, null);
    }

}
