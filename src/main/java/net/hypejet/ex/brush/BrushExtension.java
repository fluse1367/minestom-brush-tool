package net.hypejet.ex.brush;

import net.minestom.server.extensions.Extension;

public class BrushExtension extends Extension {
    @Override
    public void initialize() {
        getLogger().info("Hello World!");
    }

    @Override
    public void terminate() {
        getLogger().info("Goodbye World!");
    }
}
