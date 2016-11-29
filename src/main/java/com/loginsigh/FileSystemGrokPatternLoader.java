package com.loginsigh;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by kira on 16/11/25.
 */
public class FileSystemGrokPatternLoader implements IGrokPatternLoader {
    final ESLogger logger = Loggers.getLogger(getClass());

    @Override
    public void loadGrokPattern() {
        File file = new File("./plugins/grokplugin/pattern/");
        try {
            Files.walkFileTree(file.toPath(), new GrokFileVisitor());
        } catch (IOException e) {
            logger.error("walk grok files fail", e);
        }

    }
}
