package com.loginsigh;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * Created by kira on 16/11/29.
 */
public class GrokFileVisitor extends SimpleFileVisitor<Path> {
    final ESLogger logger = Loggers.getLogger(getClass());

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory() && !path.getFileName().toString().startsWith(".")) {
            List<String> lines = Files.readAllLines(path);
            lines.stream()
                    .filter(e -> !e.isEmpty() && !e.startsWith("#"))
                    .map(e -> {
                        String pattern = e.split(" ")[0];
                        String regx = e.substring(pattern.length());
                        GrokEntity grokEntity = new GrokEntity(pattern, regx);
                        return grokEntity;
                    })
                    .forEach(e -> {
                        GlobalGrokPattern.globalGrokPatternList.add(e);
                    });
        }
        return FileVisitResult.CONTINUE;
    }
}