package com.loginsigh;

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

        String pattern = params == null ? null :
                XContentMapValues.nodeStringValue(params.get("pattern"), null);

        String fieldName = params == null ? null :
                XContentMapValues.nodeStringValue(params.get("fieldName"), null);

        String groupkeys = params == null ? null :
                XContentMapValues.nodeStringValue(params.get("groupkeys"), null);


        if (fieldName == null || "".equals(fieldName)) {
            throw new ScriptException("Missing field parameter");
        }
        if (pattern == null || "".equals(pattern)) {
            throw new ScriptException("Missing field parameter");
        }

        List<String> groupkeyList = new ArrayList<>();

        if (groupkeys != null && !"".equals(groupkeys)) {
            groupkeyList = Arrays.asList(groupkeys.split(","));
        }

        return new GrokNativeScript(pattern, fieldName, groupkeyList);
    }

    public boolean needsScores() {
        return false;
    }
}