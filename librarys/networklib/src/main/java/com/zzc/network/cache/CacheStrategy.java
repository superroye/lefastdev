package com.zzc.network.cache;

/**
 * @author Roye
 * @date 2018/9/14
 */
public class CacheStrategy {

    public static final String HEADER_KEY = "cachestg";

    protected static final String KEY_NETWORK = "network";
    protected static final String KEY_CACHE = "cache";
    protected static final String KEY_REFRESH = "refresh";
    protected static final String KEY_CACHE_AND_REFRESH = "getandrefresh";
    protected static final String KEY_ONLY_CACHE = "onlycache";
    protected static final String KEY_CACHE_1_HOUR = "cache1hour";

    public static final String NETWORK = HEADER_KEY + ":" + KEY_NETWORK;
    public static final String CACHE = HEADER_KEY + ":" + KEY_CACHE;
    public static final String REFRESH = HEADER_KEY + ":" + KEY_REFRESH;

    /**
     * # 此策略需要配合call和PriorityCacheResponseCallback类实现
     */
    public static final String CACHE_AND_REFRESH = HEADER_KEY + ":" + KEY_CACHE_AND_REFRESH;
    public static final String ONLY_CACHE = HEADER_KEY + ":" + KEY_ONLY_CACHE;
    public static final String CACHE_1_HOUR = HEADER_KEY + ":" + KEY_CACHE_1_HOUR;
}
