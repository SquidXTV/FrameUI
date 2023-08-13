package me.squidxtv.frameui.api.cache;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public interface ImageCache {

    @NotNull Optional<BufferedImage> get(@NotNull Path path);
    @NotNull BufferedImage getOrLoad(@NotNull Path path) throws IOException;
    void put(@NotNull Path path, BufferedImage image);

}
