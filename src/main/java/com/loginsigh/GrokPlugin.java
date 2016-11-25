package com.loginsigh;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.*;

import java.io.IOException;

/**
 * Created by kira on 16/11/24.
 */
public class GrokPlugin extends Plugin {

    final ESLogger logger = Loggers.getLogger(getClass());

    private final Settings settings;

    public GrokPlugin(Settings settings) {
        this.settings = settings;
    }

    public String name() {
        return "grokplugin";
    }

    public String description() {
        return "An Plugin For ElasticSearch too Reduce Grok Script";
    }


    public void onModule(ScriptModule scriptModule) throws IOException {
        logger.info(settings.names().toString());
        logger.info("############## Init Grok Patterns #####################");
        IGrokPatternLoader grokPatternLoader = new FileSystemGrokPatternLoader();
        grokPatternLoader.loadGrokPattern();
        logger.info("############## Init Grok Patterns Success #####################");
        scriptModule.registerScript("grokscript", GrokNativeScriptFactory.class);
    }

}
