package com.loginsigh;

import oi.thekraken.grok.api.Grok;
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
    public Grok loadGrokPattern() {
        File file = new File("./plugins/grokplugin/pattern/");
        try {
            GrokFileVisitor fileVisitor = new GrokFileVisitor();
            Files.walkFileTree(file.toPath(), fileVisitor);
            return fileVisitor.getGrok();
        } catch (IOException e) {
            logger.error("walk grok files fail", e);
            return Grok.EMPTY;
        }

    }
}
