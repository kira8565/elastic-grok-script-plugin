package com.loginsigh;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kira on 16/11/25.
 */
public class FileSystemGrokPatternLoader implements IGrokPatternLoader {
    final ESLogger logger = Loggers.getLogger(getClass());

    @Override
    public List<GrokEntity> loadGrokPattern() {
        File file = new File("./plugins/grokplugin/pattern/");
        try {
            GrokFileVisitor fileVisitor = new GrokFileVisitor();
            Files.walkFileTree(file.toPath(), fileVisitor);
            return fileVisitor.getGrokEntityList();
        } catch (IOException e) {
            logger.error("walk grok files fail", e);
            return new ArrayList<>();
        }

    }
}
