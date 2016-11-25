package com.loginsigh;

import oi.thekraken.grok.api.exception.GrokException;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by kira on 16/11/25.
 */
public class FileSystemGrokPatternLoader implements IGrokPatternLoader {
    final ESLogger logger = Loggers.getLogger(getClass());

    @Override
    public void loadGrokPattern() {
        File file = new File("./plugins/grokplugin/pattern/");

        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return;
            } else {
                for (File patternFile : files) {
                    if (!patternFile.isDirectory()) {
                        List<String> str;
                        try {
                            str = Files.readAllLines(patternFile.toPath());
                            logger.info("Loading Pattern:" + patternFile.toString());

                            str.forEach(e -> {
                                if (!e.isEmpty() && !e.startsWith("#")) {
                                    String pattern = e.split(" ")[0];
                                    String regx = e.substring(pattern.length());
                                    try {
                                        GlobalGrokPattern.globalGrokPattern.addPattern(pattern.trim(), regx.trim());
                                    } catch (GrokException e1) {
                                        e1.printStackTrace();
                                        logger.error(String.format("Load Pattern %s Fail", pattern));
                                    }
                                }
                            });
                        } catch (IOException e) {
                            logger.error("Loading Pattern Fail:" + patternFile.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
