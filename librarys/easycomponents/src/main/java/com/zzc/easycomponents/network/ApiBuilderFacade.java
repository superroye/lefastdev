package com.zzc.easycomponents.network;

import com.zzc.network.ApiBuilderOuter;
import com.zzc.network.support.GlobalRequestAdapter;

/**
 * @author Roye
 * @date 2018/10/19
 */
public interface ApiBuilderFacade<T> {

    public ApiBuilderFacade baseUrl(String baseUrl);
    public ApiBuilderFacade setGlobalRequestAdapter(GlobalRequestAdapter globalRequestAdapter);
    public ApiBuilderOuter<T> getApiBuilder();
    public T build();

}
