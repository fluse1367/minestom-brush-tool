package net.hypejet.ex.brush.util;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.*;

public class BlockSerializer {
    @NotNull
    public static NBTCompound serialize(@NotNull Block block) {
        return NBT.Compound(Map.of(
                "id", NBT.String(block.namespace().toString()),
                "nbt", Objects.requireNonNullElseGet(block.nbt(), NBTCompound::new)
        ));
    }

    /**
     * Attempts to deserialize a nbt compound to a block instance.
     *
     * @param root the compound
     * @return an optional wrapping the block on success, otherwise an empty optional
     */
    @NotNull
    public static Optional<Block> deserialize(@NotNull NBTCompound root) {
        return Optional.ofNullable(root.getString("id"))
                       .map(Block::fromNamespaceId)
                       .map(b -> {
                           var nbt = root.getCompound("nbt");
                           if (nbt == null || nbt.isEmpty()) return b;
                           return b.withNbt(nbt);
                       });
    }

}
