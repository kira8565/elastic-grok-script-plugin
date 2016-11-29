package com.loginsigh;

/**
 * Created by kira on 16/11/28.
 */
public class GrokEntity {
    private String grokPattern;
    private String grokRegx;


    public GrokEntity(String grokPattern, String grokRegx) {
        this.grokPattern = grokPattern;
        this.grokRegx = grokRegx;
    }

    public String getGrokPattern() {
        return grokPattern;
    }


    public String getGrokRegx() {
        return grokRegx;
    }

}
