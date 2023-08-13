package me.squidxtv.frameui.api.parser;

import me.squidxtv.frameui.FrameUI;
import me.squidxtv.frameui.core.content.ScreenModel;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ScreenParserImpl implements ScreenParser {

    private static final String SCHEMA_PATH = "/frameui.xsd";

    private final @NotNull DocumentBuilder documentBuilder;

    public ScreenParserImpl(@NotNull FrameUI plugin) throws SAXException, ParserConfigurationException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(getClass().getResource(SCHEMA_PATH));

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setSchema(schema);
        documentBuilderFactory.setNamespaceAware(true);

        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new ParserErrorHandler(plugin));
    }

    @Override
    public @NotNull ScreenModel parse(@NotNull Path xml) throws IOException, SAXException {
        return parse(xml.toFile());
    }

    @Override
    public @NotNull ScreenModel parse(@NotNull File file) throws IOException, SAXException {
        Document document = documentBuilder.parse(file);
        Element root = document.getDocumentElement();
        return new ScreenModel(root);
    }

    public @NotNull DocumentBuilder getDocumentBuilder() {
        return documentBuilder;
    }
}
