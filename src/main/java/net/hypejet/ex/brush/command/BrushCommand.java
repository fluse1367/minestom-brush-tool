package net.hypejet.ex.brush.command;

import net.hypejet.ex.brush.BrushExtension;
import net.hypejet.ex.brush.brush.Brush;
import net.hypejet.ex.brush.brush.Brush.Type;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import static net.minestom.server.command.builder.arguments.ArgumentType.Enum;
import static net.minestom.server.command.builder.arguments.ArgumentType.Integer;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

public class BrushCommand extends Command {

    public BrushCommand() {
        super("brush", "br");

        setCondition((s, str) -> s instanceof Player p && BrushExtension.canUse(p));

        var argType = Enum("type", Type.class);
        var argRadius = Integer("radius").min(0).max(20);
        var argBlock = BlockState("block");

        addSyntax((s, c) -> ((Player) s).getInventory().addItemStack(new Brush(c.get(argType), c.get(argRadius), c.get(argBlock)).toItemStack()), argType, argRadius, argBlock);
    }
}
