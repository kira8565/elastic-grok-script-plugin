package com.loginsigh;

import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.exception.GrokException;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kira on 16/11/29.
 */
public class GrokFileVisitor extends SimpleFileVisitor<Path> {
    final ESLogger logger = Loggers.getLogger(getClass());

    private Grok grok = new Grok();

    public Grok getGrok() {
        return this.grok;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory() && !path.getFileName().toString().startsWith(".")) {
            try {
                grok.addPatternFromFile(String.valueOf(path.toAbsolutePath()));
            } catch (GrokException e) {
                logger.error("Create Grok Fail", e);
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
