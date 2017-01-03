package com.kuaipao.base.inject;/**
 * Created by pc on 2016/5/3.
 */

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-03
 * Time: 13:56
 * Author: pingfu
 * FIXME
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface XAnnotation {
    /**
     *
     * @return
     */
    int layoutId() default -1;

    /**
     *
     * @return
     */
    boolean required() default false;
}
