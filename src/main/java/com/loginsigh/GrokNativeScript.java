package com.loginsigh;

import oi.thekraken.grok.api.Grok;
import oi.thekraken.grok.api.Match;
import oi.thekraken.grok.api.exception.GrokException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
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
    private final Boolean isHashMap;

    public GrokNativeScript(String pattern, String fieldname, List<String> groupkeyList, Boolean isHashMap) {
        this.pattern = pattern;
        this.fieldname = fieldname;
        this.groupkeyList = groupkeyList;
        this.isHashMap = isHashMap;
    }

    final ESLogger logger = Loggers.getLogger(getClass());

    /**
     * If Has groupkeys The Function will return groupkeys values,
     * it is useful for aggregations ^_^
     */
    public Object run() {

        String docValue = String.valueOf(source().get(this.fieldname));
        if (docValue == null && docValue.isEmpty()) {
            return null;
        } else {
            try {

                /**
                 *Because ElasticSearch run plugin as paralle,and the grok lib has error in paralle.
                 * So i new a grok in here an it will be fine .....
                 */

                Grok grok = new Grok();

                GlobalGrokPattern.globalGrokPatternList.forEach(e -> {
                    try {
                        grok.addPattern(e.getGrokPattern().trim(), e.getGrokRegx().trim());
                    } catch (GrokException e1) {
                        logger.error(String.format("Load Pattern %s Fail", pattern), e1);
                    }
                });

                grok.compile(pattern);
                Match match = grok.match(docValue);
                match.captures();
                Map<String, Object> map = match.toMap();
                if (groupkeyList.isEmpty()) {
                    return map;
                } else {
                    StringBuilder sb = new StringBuilder();
                    if (isHashMap) {
                        HashMap<String, Object> targetHashMap = new HashMap<>();

                        groupkeyList.forEach(e -> {
                            targetHashMap.put(e, map.get(e));
                        });
                        return targetHashMap;
                    } else {
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
                }
            } catch (GrokException e) {
                logger.error("Compile Grok Pattern Fail", e);
                return null;
            }
        }
    }
}