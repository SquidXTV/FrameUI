package me.squidxtv.frameui.api;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ImageCacheImpl implements ImageCache {

    private final @NotNull Cache<Path, BufferedImage> cache;

    public ImageCacheImpl(/*ToDo: add config*/) {
        cache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public @NotNull Optional<BufferedImage> get(@NotNull Path path) {
        return Optional.ofNullable(cache.getIfPresent(path));
    }

    @Override
    public @NotNull BufferedImage getOrLoad(@NotNull Path path) throws IOException {
        BufferedImage image = cache.getIfPresent(path);
        if (image == null) {
            image = ImageIO.read(Files.newInputStream(path));
            cache.put(path, image);
        }
        return image;
    }

    @Override
    public void put(@NotNull Path path, BufferedImage image) {
        cache.put(path, image);
    }
}
