package net.hypejet.ex.brush.brush.place;

import net.hypejet.ex.brush.util.BlockSerializer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.Batch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.Block.Comparator;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTString;

import java.util.Map;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.translatable;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;

public class ReplacePlacer extends FillPlacer {

    private final Block replace;

    public ReplacePlacer(Block block, Block replace) {
        super(block);
        this.replace = replace;
    }

    @Override
    public void place(Batch<?> batch, Instance instance, Vec posRelative, Pos posAbsolute) {
        var b = instance.getBlock(posAbsolute);
        if (!block.compare(b, Comparator.ID)) return;
        if (!block.compare(b, Comparator.STATE)) return;

        batch.setBlock(posRelative, replace);
    }


    @Override
    public @NotNull NBTCompound serialize() {
        return new NBTCompound(Map.of(
                "type", new NBTString("replace"),
                "block", BlockSerializer.serialize(block),
                "replace", BlockSerializer.serialize(replace)
        ));
    }

    @Override
    public @NotNull Component name() {
        return translatable(block.registry().translationKey(), GOLD)
                .append(text(" -> ", GRAY))
                .append(translatable(replace.registry().translationKey(), GOLD));
    }

    @Override
    public @NotNull Component description() {
        return text("Replace ")
                .append(Component.translatable(block.registry().translationKey()))
                .append(text(" with "))
                .append(Component.translatable(replace.registry().translationKey()));
    }
}
