package com.loginsigh;

import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.script.AbstractSearchScript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kira on 16/11/24.
 */
public class GrokNativeScript extends AbstractSearchScript {

    private final String pattern;
    private final String fieldname;
    private final List<String> groupkeyList;

    public GrokNativeScript(String pattern, String fieldname, List<String> groupkeyList) {
        this.pattern = pattern;
        this.fieldname = fieldname;
        this.groupkeyList = groupkeyList;
    }

    final ESLogger logger = Loggers.getLogger(getClass());

    /**
     * If Has groupkeys The Function will return groupkeys values,
     * it is useful for aggregations ^_^
     *
     */
    public Object run() {
        String docValue = String.valueOf(source().get(this.fieldname));
        if (docValue != null && !docValue.isEmpty()) {
            try {
                GlobalGrokPattern.globalGrokPattern.compile(pattern);
                Match match = GlobalGrokPattern.globalGrokPattern.match(docValue);
                match.captures();
                Map<String, Object> map = match.toMap();
                if (groupkeyList.isEmpty()) {
                    return map;
                } else {
                    StringBuilder sb = new StringBuilder();
                    groupkeyList.forEach(e -> {
                        if (map.containsKey(e)) {
                            sb.append(String.valueOf(map.get(e)));
                        } else {
                            sb.append(String.valueOf(source().source().get(e)));
                        }
                    });
                    String finalResult = sb.toString();
                    if (StringUtils.isNumeric(finalResult)) {
                        return Double.valueOf(finalResult);
                    } else {
                        return finalResult;
                    }
                }
            } catch (GrokException e) {
                logger.error("Compile Grok Pattern Fail");
                e.printStackTrace();
            }
        }
        return null;
    }
}