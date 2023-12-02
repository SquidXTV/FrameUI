package me.squidxtv.frameui.api.parser;

import me.squidxtv.frameui.core.content.ScreenModel;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface ScreenParser {

    @NotNull ScreenModel parse(@NotNull Path xml) throws IOException, SAXException;
    @NotNull ScreenModel parse(@NotNull File file) throws IOException, SAXException;
    @NotNull ScreenModel parse(@NotNull InputStream stream) throws IOException, SAXException;

}
