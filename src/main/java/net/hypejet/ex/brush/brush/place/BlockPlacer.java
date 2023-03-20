package net.hypejet.ex.brush.brush.place;

import net.hypejet.ex.brush.util.BlockSerializer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.Batch;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.Optional;

public interface BlockPlacer {

    @NotNull
    static Optional<? extends BlockPlacer> deserialize(@NotNull NBTCompound root) {
        var type = root.getString("type");
        if (type == null) return Optional.empty();
        return switch (type) {
            case "fill" ->
                    Optional.ofNullable(root.getCompound("block")).flatMap(BlockSerializer::deserialize).map(FillPlacer::new);
            case "replace" -> {
                var block = Optional.ofNullable(root.getCompound("block")).flatMap(BlockSerializer::deserialize).orElse(null);
                var replace = Optional.ofNullable(root.getCompound("replace")).flatMap(BlockSerializer::deserialize).orElse(null);
                if (block == null || replace == null) yield Optional.empty();
                yield Optional.of(new ReplacePlacer(block, replace));
            }
            default -> Optional.empty();
        };
    }

    /**
     * Places a block in the batch.
     *
     * @param batch       the relative batch
     * @param instance    the instance
     * @param posRelative the relative block position
     * @param posAbsolute the absolute block position
     */
    void place(Batch<?> batch, Instance instance, Vec posRelative, Pos posAbsolute);

    @NotNull
    NBTCompound serialize();

    @NotNull
    Component name();

    @NotNull
    Component description();
}
