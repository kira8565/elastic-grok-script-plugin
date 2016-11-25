package com.loginsigh;

import com.google.common.io.Resources;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public static void traverseFolder(String path) {

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                System.out.println("文件夹是空的!");
                return;
            } else {
                for (File file2 : files) {
                    if (!file2.isDirectory()) {
                        List<String> str = null;

                        try {
                            str = Files.readAllLines(file2.toPath());

                            str.forEach(e -> {
                                if (!e.isEmpty() && !e.startsWith("#")) {
                                    String pattern = e.split(" ")[0];
                                    String regx = e.substring(pattern.length());
                                    try {
                                        GlobalGrokPattern.globalGrokPattern.addPattern(pattern.trim(), regx.trim());
                                    } catch (GrokException e1) {
                                        e1.printStackTrace();
                                        System.out.println(pattern);

                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }

    public void testLoadGrokPattern() throws IOException, GrokException {
        String content = "2016-11-25T02:44:26.142Z GET /posts [200] 50ms";
        traverseFolder("/Users/kira/loginsight/elastic-grok-script-plugin/src/test/resources/pattern");
        GlobalGrokPattern.globalGrokPattern.compile("%{DATE}");
        Match match = GlobalGrokPattern.globalGrokPattern.match(content);
        match.captures();
        System.out.println(match.toJson());
    }

}
