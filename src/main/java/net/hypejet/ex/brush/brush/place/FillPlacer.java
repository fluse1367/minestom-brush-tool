package net.hypejet.ex.brush.brush.place;

import net.hypejet.ex.brush.util.BlockSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.Batch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTString;

import java.util.Map;

public class FillPlacer implements BlockPlacer {

    protected final Block block;

    public FillPlacer(Block block) {
        this.block = block;
    }

    @Override
    public void place(Batch<?> batch, Instance instance, Vec posRelative, Pos posAbsolute) {
        batch.setBlock(posRelative, block);
    }

    @Override
    public @NotNull NBTCompound serialize() {
        return new NBTCompound(Map.of(
                "type", new NBTString("fill"),
                "block", BlockSerializer.serialize(block)
        ));
    }

    @Override
    public @NotNull Component name() {
        return Component.translatable(block.registry().translationKey(), NamedTextColor.GOLD);
    }

    @Override
    public @NotNull Component description() {
        return Component.text("Fill with ")
                        .append(Component.translatable(block.registry().translationKey()));
    }
}
