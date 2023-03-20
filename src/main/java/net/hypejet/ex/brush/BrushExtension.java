package net.hypejet.ex.brush;

import net.hypejet.ex.brush.brush.BrushHandler;
import net.hypejet.ex.brush.command.BrushCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.Material;

public class BrushExtension extends Extension {

    public static final Material BRUSH_MATERIAL = Material.BLAZE_ROD;
    public static final int MAX_DISTANCE = 100;

    public static boolean canUse(Player player) {
        // TODO
        return true;
    }

    @Override
    public void initialize() {
        MinecraftServer.getCommandManager().register(new BrushCommand());
        BrushHandler.register();
    }

    @Override
    public void terminate() {

    }
}
