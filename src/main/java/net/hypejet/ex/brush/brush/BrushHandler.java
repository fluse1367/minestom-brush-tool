package net.hypejet.ex.brush.brush;

import net.hypejet.ex.brush.BrushExtension;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;

import java.util.Comparator;

/**
 * Handles world interactions with a brush item.
 */
public final class BrushHandler {

    public static void register() {
        var man = MinecraftServer.getGlobalEventHandler();
        var handler = new BrushHandler();
        man.addListener(PlayerUseItemEvent.class, handler::onUse);
        man.addListener(PlayerUseItemOnBlockEvent.class, handler::onUse);
    }

    private BrushHandler() {}

    private void onUse(PlayerUseItemEvent e) {
        var p = e.getPlayer();

        if (!BrushExtension.canUse(p)) return;
        Brush.fromItemStack(e.getItemStack())
             .ifPresent(br -> {
                 var pos = p.getPosition();
                 // raycast
                 p.getLineOfSight(BrushExtension.MAX_DISTANCE)
                  .stream()
                  // nearest
                  .min(Comparator.comparingDouble(pos::distance))
                  // brush!
                  .ifPresent(center -> br.brush(center, p.getInstance()));
             });

    }

    private void onUse(PlayerUseItemOnBlockEvent e) {
        if (!BrushExtension.canUse(e.getPlayer())) return;
        Brush.fromItemStack(e.getItemStack())
             .ifPresent(br -> br.brush(e.getPosition(), e.getPlayer().getInstance()));
    }


}
