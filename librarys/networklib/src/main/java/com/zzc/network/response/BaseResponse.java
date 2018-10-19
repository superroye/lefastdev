package com.zzc.network.response;

import java.io.Serializable;

/**
 * Created by Roye on 2016-2-22.
 */
public class BaseResponse implements Serializable {
    public int code;
    public int result;
    public String msg;
}
