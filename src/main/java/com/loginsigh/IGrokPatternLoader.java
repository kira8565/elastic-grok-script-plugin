package com.loginsigh;

import oi.thekraken.grok.api.Grok;

import java.util.List;

/**
 * Created by kira on 16/11/25.
 */
public interface IGrokPatternLoader {
    List<GrokEntity> loadGrokPattern();
}
