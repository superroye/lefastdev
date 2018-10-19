package com.zzc.network.cache;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

/**
 * @author Roye
 * @date 2018/9/10
 */
public class CacheStrategyUtil {

    public static void checkSame(final Object resopnse1, Object resopnse2, Consumer<String> consumer) {
        Observable.just(resopnse1, resopnse2).flatMap(new Function<Object, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final Object resopnse) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        try {
                            Gson gson = new Gson();
                            String md5 = ByteString.encodeUtf8(gson.toJson(resopnse)).md5().hex();
                            e.onNext(String.valueOf(md5));
                        } catch (Exception ex) {
                            e.onNext("");
                            ex.printStackTrace();
                        }

                        e.onComplete();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation());
            }
        }).reduce(new BiFunction<String, String, String>() {
                      @Override
                      public String apply(String o, String o2) {
                          return o.equals(o2) ? "y" : "n";
                      }
                  }
        ).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(consumer);
    }

    public static Request getCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "max-age=86400").build();
    }

    public static Request getNoCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "no-cache").build();
    }

    public static Request getOnlyCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=2419200").build();
    }

    public static Request get1HourCacheRequest(Request oRequest) {
        return oRequest.newBuilder().header("Cache-Control", "max-age=3600").build();
    }

    public static Request getRefreshRequest(Request oRequest) {
        return getNoCacheRequest(oRequest);
    }

    static WeakHashMap<String, Integer> cacheAndRefreshFlag = new WeakHashMap<>();

    public static Response doForCacheInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategy.HEADER_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        Request request = oRequest;
        if (adrcache.equals(CacheStrategy.KEY_CACHE)) {

            request = getCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_ONLY_CACHE)) {

            request = getOnlyCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_CACHE_1_HOUR)) {

            request = get1HourCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_REFRESH)) {

            request = getRefreshRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_NETWORK)) {

            request = getNoCacheRequest(oRequest);

        } else if (adrcache.equals(CacheStrategy.KEY_CACHE_AND_REFRESH)) {

            String tmpKey = Cache.key(oRequest.url());
            if (!cacheAndRefreshFlag.containsKey(tmpKey)) {//第一次
                request = getCacheRequest(oRequest);
                cacheAndRefreshFlag.put(tmpKey, 1);
            } else {
                request = getNoCacheRequest(oRequest);
                cacheAndRefreshFlag.remove(tmpKey);
            }

        }
        return chain.proceed(request);
    }

    public static Response doForNetworkInterceptor(Interceptor.Chain chain, Request oRequest) throws IOException {
        String adrcache = oRequest.header(CacheStrategy.HEADER_KEY);

        if (TextUtils.isEmpty(adrcache)) {
            return null;
        }

        CacheControl cacheControl = oRequest.cacheControl();

        Request request = oRequest.newBuilder().removeHeader(CacheStrategy.HEADER_KEY).build();
        Response originalResponse = chain.proceed(request);
        Response.Builder builder = originalResponse.newBuilder()
                .removeHeader("Pragma")//清除响应体对Cache有影响的信息
                .removeHeader("Cache-Control");//清除响应体对Cache有影响的信息

        if (cacheControl.maxAgeSeconds() > 0 || cacheControl.onlyIfCached()) {
            builder.header("Cache-Control", cacheControl.toString());
        } else {
            builder.header("Cache-Control", "max-age=86400"); //秒
        }

        return builder.build();
    }

}
