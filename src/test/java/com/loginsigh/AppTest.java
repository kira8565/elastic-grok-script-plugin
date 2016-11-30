package com.loginsigh;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.codahale.metrics.MetricRegistry.name;

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
        reporter.start(1, TimeUnit.SECONDS);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    final ESLogger logger = Loggers.getLogger(getClass());

    public void testGrok() throws IOException, GrokException {
        List<String> logs = Files.readAllLines(Paths.get("./testset/apache.log"));
        IGrokPatternLoader iGrokPatternLoader = new FileSystemGrokPatternLoader();
        Grok grok = iGrokPatternLoader.loadGrokPattern();

        logs.forEach(e -> {
            try {
                grok.compile("%{COMMONAPACHELOG}");
                Match match = grok.match(e);
                match.captures();
                System.out.println(match.toJson());
            } catch (GrokException e1) {
                e1.printStackTrace();
            }

        });

    }

    private final MetricRegistry metrics = new MetricRegistry();

    private final CsvReporter reporter = CsvReporter
            .forRegistry(metrics)
            .formatFor(Locale.CHINESE)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build(new File("/Users/kira/loginsight/opensource/elastic-grok-script-plugin/metrics"));

    private void paralleGrok(int num, String pattern, Timer instanceTimmer, Timer grokTimer) throws IOException {
        Timer.Context instanceContext = instanceTimmer.time();
        String logs = Files.readAllLines(Paths.get("./testset/apache.log")).get(0);
        ArrayList<Object> list = new ArrayList<Object>(
                Collections.nCopies(num, new Object()));
        instanceContext.stop();
        list.parallelStream().forEach(e -> {
            try {
                Timer.Context gromContext = grokTimer.time();
                IGrokPatternLoader iGrokPatternLoader = new FileSystemGrokPatternLoader();
                Grok grok = iGrokPatternLoader.loadGrokPattern();
                grok.compile(pattern);
                Match match = grok.match(logs);
                match.captures();
                match.toMap();
                gromContext.stop();
            } catch (GrokException e1) {
                e1.printStackTrace();
            }
        });
    }

    public void testParalleGrok100ViaApache() throws IOException, GrokException {

        Timer newInstance100Time = metrics.timer(name(AppTest.class, "newInstance100Time"));
        Timer grok100Time = metrics.timer(name(AppTest.class, "grok100Time"));
        paralleGrok(100, "%{COMMONAPACHELOG}", newInstance100Time, grok100Time);
    }

    public void testParalleGrok1000ViaApache() throws IOException, GrokException {
        Timer newInstance1000Time = metrics.timer(name(AppTest.class, "newInstance1000Time"));
        Timer grok1000Time = metrics.timer(name(AppTest.class, "grok1000Time"));
        paralleGrok(1000, "%{COMMONAPACHELOG}", newInstance1000Time, grok1000Time);
    }

    public void testParalleGrok10000ViaApache() throws IOException, GrokException {
        Timer newInstance10000Time = metrics.timer(name(AppTest.class, "newInstance10000Time"));
        Timer grok10000Time = metrics.timer(name(AppTest.class, "grok10000Time"));
        paralleGrok(10000, "%{COMMONAPACHELOG}", newInstance10000Time, grok10000Time);
    }

    public void testParalleGrok100000ViaApache() throws IOException, GrokException {
        Timer newInstance100000Time = metrics.timer(name(AppTest.class, "newInstance100000Time"));
        Timer grok100000Time = metrics.timer(name(AppTest.class, "grok100000Time"));
        paralleGrok(100000, "%{COMMONAPACHELOG}", newInstance100000Time, grok100000Time);
    }


    public void testParalleGrok100ViaWord() throws IOException, GrokException {
        Timer newSimpleInstance100Time = metrics.timer(name(AppTest.class, "newSimpleInstance100Time"));
        Timer grokSimple100Time = metrics.timer(name(AppTest.class, "grokSimple100Time"));
        paralleGrok(100, "%{WORD}", newSimpleInstance100Time, grokSimple100Time);
    }

    public void testParalleGrok1000ViaWord() throws IOException, GrokException {
        Timer newSimpleInstance1000Time = metrics.timer(name(AppTest.class, "newSimpleInstance1000Time"));
        Timer grokSimple1000Time = metrics.timer(name(AppTest.class, "grokSimple1000Time"));
        paralleGrok(1000, "%{WORD}", newSimpleInstance1000Time, grokSimple1000Time);
    }


    public void testParalleGrok10000ViaWord() throws IOException, GrokException {
        Timer newSimpleInstance10000Time = metrics.timer(name(AppTest.class, "newSimpleInstance10000Time"));
        Timer grokSimple10000Time = metrics.timer(name(AppTest.class, "grokSimple10000Time"));
        paralleGrok(10000, "%{WORD}", newSimpleInstance10000Time, grokSimple10000Time);
    }


    public void testParalleGrok100000ViaWord() throws IOException, GrokException {
        Timer newSimpleInstance100000Time = metrics.timer(name(AppTest.class, "newSimpleInstance100000Time"));
        Timer grokSimple100000Time = metrics.timer(name(AppTest.class, "grokSimple100000Time"));
        paralleGrok(100000, "%{WORD}", newSimpleInstance100000Time, grokSimple100000Time);
    }

}
