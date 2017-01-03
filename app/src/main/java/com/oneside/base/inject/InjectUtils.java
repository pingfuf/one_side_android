package com.oneside.base.inject;

import android.app.Activity;
import android.view.View;

import com.oneside.base.BaseFragment;

import java.lang.reflect.Field;

/**
 * User: XXoneside(www.xxoneside.com)
 * Date: 2016-05-03
 * Time: 13:54
 * Author: pingfu
 * FIXME
 */
public class InjectUtils {
    /**
     * 自动注入findViewById()
     *
     * @param activity 当前的activity
     */
    public static void autoInject(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Class<?> classAct = activity.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(activity, activity.findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 自动注入findViewById()
     *
     * @param baseFragment 当前的Fragment
     */
    public static void autoInject(BaseFragment baseFragment) {
        if (baseFragment == null || baseFragment.getView() == null) {
            return;
        }
        Class<?> classAct = baseFragment.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(baseFragment, baseFragment.getView().findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 自动注入findViewById()
     *
     * @param view 当前的View
     */
    public static void autoInject(View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        Class<?> classAct = view.getClass();
        //获取所有的变量
        Field[] fields = classAct.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(From.class)) {//判断是否为InjectView注解
                From injectView = field.getAnnotation(From.class);//获取InjectView注解
                int id = injectView.value();//获取注解的值
                if (id > 0) {
                    field.setAccessible(true);//允许范围私有变量
                    try {
                        field.set(view, view.findViewById(id));//给当前的变量赋值
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
