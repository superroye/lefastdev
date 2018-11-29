package com.zuzuche.easycomponents.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Roye
 * @date 2018/11/27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoGetUtil {
}
