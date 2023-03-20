package net.hypejet.ex.brush.command;

import net.hypejet.ex.brush.BrushExtension;
import net.hypejet.ex.brush.brush.Brush;
import net.hypejet.ex.brush.brush.Brush.Shape;
import net.hypejet.ex.brush.brush.place.FillPlacer;
import net.hypejet.ex.brush.brush.place.ReplacePlacer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import static net.minestom.server.command.builder.arguments.ArgumentType.Enum;
import static net.minestom.server.command.builder.arguments.ArgumentType.Integer;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

public class BrushCommand extends Command {

    public BrushCommand() {
        super("brush", "br");

        setCondition((s, str) -> s instanceof Player p && BrushExtension.canUse(p));

        var argShape = Enum("shape", Shape.class);
        var argRadius = Integer("radius").min(0).max(20);
        var argBlock = BlockState("block");
        var argReplace = BlockState("replacewith");


        addSyntax((s, c) -> {
            ((Player) s).getInventory().addItemStack(new Brush(c.get(argShape), c.get(argRadius), new FillPlacer(c.get(argBlock))).toItemStack());
        }, argShape, argRadius, Literal("fill"), argBlock);
        addSyntax((s, c) -> {
            ((Player) s).getInventory().addItemStack(new Brush(c.get(argShape), c.get(argRadius), new ReplacePlacer(c.get(argBlock), c.get(argReplace))).toItemStack());
        }, argShape, argRadius, Literal("replace"), argBlock, argReplace);
    }
}
