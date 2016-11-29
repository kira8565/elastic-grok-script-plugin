package com.loginsigh;

import com.google.common.io.Resources;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    final ESLogger logger = Loggers.getLogger(getClass());

    public void testGrok() throws IOException, GrokException {
        String content = "2016-11-25T02:44:26.142Z GET /posts [200] 50ms";
        Files.walkFileTree(new File("/Users/kira/loginsight/elastic-grok-script-plugin/src/test/resources/pattern").toPath(),
                new GrokFileVisitor());
        Grok grok = new Grok();
        GlobalGrokPattern.globalGrokPatternList.forEach(e -> {
            try {
                grok.addPattern(e.getGrokPattern(), e.getGrokRegx());
            } catch (GrokException e1) {
                logger.error(e1.getMessage(), e1);
            }
        });

        grok.compile("%{WORD:wd}");
        Match match = grok.match(content);
        match.captures();
        System.out.println(match.toJson());
    }

    public void testParalleGrok() throws IOException, GrokException {
        String content = "2016-11-25T02:44:26.142Z GET /posts [200] 50ms";
        ArrayList<Object> list = new ArrayList<Object>(
                Collections.nCopies(1000, new Object()));
        list.parallelStream().forEach(e -> {
            try {
                Grok grok = new Grok();
                GlobalGrokPattern.globalGrokPatternList.forEach(o -> {
                    try {
                        grok.addPattern(o.getGrokPattern(), o.getGrokRegx());
                    } catch (GrokException e1) {
                        logger.error(e1.getMessage(), e1);
                    }
                });

                grok.compile("%{WORD:wd}");
                Match match = grok.match(content);
                match.captures();
                match.toJson();
            } catch (GrokException e1) {
                e1.printStackTrace();
            }
        });

    }

}
