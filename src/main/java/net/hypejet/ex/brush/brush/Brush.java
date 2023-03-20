package net.hypejet.ex.brush.brush;

import eu.software4you.ulib.core.reflect.ReflectUtil;
import eu.software4you.ulib.core.util.Expect;
import net.hypejet.ex.brush.BrushExtension;
import net.hypejet.ex.brush.util.BlockSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.instance.block.Block;
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

public record Brush(@NotNull Type type, int radius, @NotNull Block block) {

    private static final Tag<String> TAG_BRUSH_TYPE = Tag.String("brush");
    private static final Tag<NBT> TAG_BLOCK = Tag.NBT("block");
    private static final Tag<Integer> TAG_RADIUS = Tag.Integer("radius");

    public enum Type {
        cuboid(new CubicSelector()),
        sphere(new SphericalSelector());

        private final Selector selector;

        Type(Selector selector) {this.selector = selector;}
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
                         .map(str -> ReflectUtil.getEnumEntry(Type.class, str))
                         .orElse(null);
        if (type == null) return empty();

        var block = Expect.ofNullable(stack.getTag(TAG_BLOCK))
                          .map(nbt -> BlockSerializer.deserialize((NBTCompound) nbt))
                          .map(op -> op.orElse(null))
                          .orElse(null);
        if (block == null) return empty();

        var radius = Expect.ofNullable(stack.getTag(TAG_RADIUS)).orElse(null);
        if (radius == null) return empty();

        return of(new Brush(type, radius, block));
    }

    /**
     * Serializes the brush object to an item stack.
     *
     * @return the serialized item stack
     */
    @NotNull
    public ItemStack toItemStack() {
        return ItemStack.builder(BrushExtension.BRUSH_MATERIAL)
                        .displayName(Component.translatable(block.registry().translationKey(), NamedTextColor.GOLD))
                        .set(TAG_BRUSH_TYPE, type.name())
                        .set(TAG_BLOCK, BlockSerializer.serialize(block))
                        .set(TAG_RADIUS, radius)
                        .lore(text("Brush: ", GREEN).append(text(type.name(), GRAY)),
                              text("Radius: ", GREEN).append(text(radius, GRAY)),
                              text("Block: ", GREEN).append(text(block.name(), GRAY))
                        ).build();
    }

    public void brush(@NotNull Point center, Instance instance) {
        var batch = new RelativeBlockBatch();
        type.selector.select(radius)
                     .forEach(vec -> batch.setBlock(vec.blockX(), vec.blockY(), vec.blockZ(), block));
        batch.apply(instance, center, null);
    }

}
