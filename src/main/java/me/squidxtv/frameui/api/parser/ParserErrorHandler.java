package me.squidxtv.frameui.api.parser;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class ParserErrorHandler implements ErrorHandler {

    private final @NotNull Logger logger;

    ParserErrorHandler(@NotNull Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warning(SAXParseException exception) {
        logger.log(Level.WARNING, "A warning occurred while parsing an XML document.", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw new SAXException(exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new SAXException(exception);
    }
}
