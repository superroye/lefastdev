package com.zuzuche.demo.fastdev.component.network;

import com.zuzuche.demo.fastdev.component.network.bean.TaobaoTest;
import com.zzc.network.cache.CacheStrategy;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @Headers("Cache-Control: max-age=640000") 直接请求，有缓存且未过期返回缓存，否则读网络并写缓存
 * @Headers("Cache-Control:no-cache") 直接请求网络，不做缓存
 * @Headers("Cache-Control:no-store") 直接请求网络，不存临时缓存
 * @Headers("Cache-Control:public, only-if-cached, max-stale=2419200") 直接请求缓存
 * @GET("select/search.php")
 * @GET("book/{id}")
 * @POST
 * @FormUrlEncoded
 */
public interface CoolAPi {

    @Headers(CacheStrategy.ONLY_CACHE)
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchOnlyCache(@Query("q") String keyword);

    @Headers(CacheStrategy.NETWORK)
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchNetwork(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE_1_HOUR)
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchCacheAge(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE_AND_REFRESH)
    @GET("sug?code=utf-8")
    public Call<TaobaoTest> testSearchSceneCacheCall(@Query("q") String keyword);

    @Headers(CacheStrategy.CACHE)
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchSceneCache(@Query("q") String keyword);

    @Headers(CacheStrategy.REFRESH)
    @GET("sug?code=utf-8")
    public Observable<TaobaoTest> testSearchSceneRefresh(@Query("q") String keyword);

}
