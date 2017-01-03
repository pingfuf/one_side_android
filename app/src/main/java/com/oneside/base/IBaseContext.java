package com.oneside.base;

import android.app.Activity;

import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.RequestDelegate;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-09
 * Time: 16:14
 * Author: pingfu
 * FIXME
 */
public interface IBaseContext extends RequestDelegate {
    void xStartActivity(Class<? extends Activity> clazz);

    void xStartActivity(Class<? extends Activity> clazz, BasePageParam pageParam);

    void xStartActivity(Class<? extends Activity> clazz, BasePageParam pageParam, int requestCode);

    BaseActivity getContext();
}
