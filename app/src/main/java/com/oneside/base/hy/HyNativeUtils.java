package com.oneside.base.hy;

import com.alibaba.fastjson.JSON;
import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.hy.action.HyScriptAction;
import com.oneside.base.hy.action.JokeDetailAction;
import com.oneside.base.hy.action.StoryDetailAction;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by fupingfu on 2017/1/12.
 */
public class HyNativeUtils {
    public static void gotoStoryDetailWebPage(BaseActivity activity, String title, String storyId) {
        StoryDetailAction action = new StoryDetailAction();
        action.url = "story/detail";
        action.type = 101;
        action.storyId = storyId;
        action.title = title;

        startWebActivity(activity, "storyDetail.html", action);
    }

    public static void gotoStoryPicDetailWebPage(BaseActivity activity, String title, String id) {
        StoryDetailAction action = new StoryDetailAction();
        action.url = "pic/storyDetail";
        action.type = 102;
        action.storyId = id;
        action.title = title;

        startWebActivity(activity, "storyPicDetail.html", action);
    }

    public static void gotoConnotativePicDetailWebPage(BaseActivity activity, String title, String id) {
        StoryDetailAction action = new StoryDetailAction();
        action.url = "pic/detail";
        action.type = 101;
        action.storyId = id;
        action.title = title;

        startWebActivity(activity, "connotativePicDetail.html", action);
    }

    public static void gotoJokeDetailWebPage(BaseActivity activity, String title, String text) {
        JokeDetailAction action = new JokeDetailAction();
        action.title = title;
        String temp = null;
        try {
            temp = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        action.text = stringJson(temp);
        action.url = "joke/detail";

        startWebActivity(activity, "jokeDetail.html", action);
    }

    private static void startWebActivity(BaseActivity activity, String path, HyScriptAction action) {
        if (activity == null || activity.isFinishing() || action == null) {
            return;
        }

        String actionStr = null;
        try {
            actionStr = URLEncoder.encode(JSON.toJSONString(action), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        WebPageParam pageParam = new WebPageParam();
        pageParam.url = getUrl(action, path);
        pageParam.action = actionStr;
        pageParam.hasBack = true;

        activity.xStartActivity(NativeWebActivity.class, pageParam);
    }

    private static String getUrl(HyScriptAction action, String path) {
        if(CardConfig.isMock()) {
            return CardConfig.getMockServerUrl() + ":8080/hybrid/" + action.url;
        } else {
            return  "file:///android_asset/" + path;
        }
    }

    private static String stringJson(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
