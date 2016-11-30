package com.loginsigh;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.inject.internal.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;
import org.elasticsearch.script.ScriptException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by kira on 16/11/24.
 */
public class GrokNativeScriptFactory implements NativeScriptFactory {
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {

        String pattern = params == null ? "" :
                XContentMapValues.nodeStringValue(params.get("pattern"), "");

        String fieldName = params == null ? "" :
                XContentMapValues.nodeStringValue(params.get("fieldName"), "");

        String groupkeys = params == null ? "" :
                XContentMapValues.nodeStringValue(params.get("groupkeys"), "");

        String isHashMap = params == null ? "" :
                XContentMapValues.nodeStringValue(params.get("isHashMap"), "");


        if (StringUtils.isBlank(fieldName)) {
            throw new ScriptException("Missing field parameter");
        }
        if (StringUtils.isBlank(pattern)) {
            throw new ScriptException("Missing field parameter");
        }

        List<String> groupkeyList = new ArrayList<>();
        if (StringUtils.isNotBlank(groupkeys)) {
            groupkeyList = Arrays.asList(groupkeys.split(","));
        }

        Boolean isHashMapBoolean;
        if (StringUtils.isBlank(isHashMap) || "false".equals(isHashMap.toLowerCase())) {
            isHashMapBoolean = false;
        } else {
            isHashMapBoolean = true;
        }

        return new GrokNativeScript(pattern, fieldName, groupkeyList, isHashMapBoolean);
    }

    public boolean needsScores() {
        return false;
    }
}