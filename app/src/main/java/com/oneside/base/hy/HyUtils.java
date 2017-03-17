package com.oneside.base.hy;

import com.alibaba.fastjson.JSON;
import com.oneside.base.BaseActivity;
import com.oneside.base.CardConfig;
import com.oneside.base.hy.action.HyScriptAction;
import com.oneside.base.hy.action.StoryDetailAction;
import com.oneside.utils.IOUtils;

import java.net.URLEncoder;

/**
 * Created by fupingfu on 2017/1/9.
 */
public class HyUtils {
    public static void gotoStoryDetailWebPage(BaseActivity activity, String title, String storyId) {
        StoryDetailAction action = new StoryDetailAction();
        action.url = "story/detail";
        action.type = 101;
        action.storyId = storyId;
        action.title = title;

        startWebActivity(activity, action);
    }

    private static void startWebActivity(BaseActivity activity, HyScriptAction action) {
        String a = null;
        try {
            a = URLEncoder.encode(JSON.toJSONString(action), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String server = IOUtils.getPreferenceValue(CardConfig.SERVER_URL);
        server += ":8080/hybrid/hy/index";
        activity.startWebActivity(server, a, true);
    }
}
