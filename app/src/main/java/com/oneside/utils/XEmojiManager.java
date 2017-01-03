package com.oneside.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.LruCache;

import com.oneside.base.CardApplication;
import com.oneside.model.beans.XEmoji;
import com.oneside.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情包管理类
 *
 * Created by pingfu on 16-7-7.
 */
public class XEmojiManager {
    public static final int BEER_EMOJI_TYPE = 0;
    public static final int IOS_EMOJI_TYPE = 1;
    public static final int DELETE_EMOJI_TYPE = 2;

    private static final String BEER_EMOJI_PATH = "beer_emoji";
    private static final String IOS_EMOJI_PATH = "ios_emoji";
    private static final int MAX_BITMAP_CACHE_SIZE = 100;

    private List<XEmoji> mBeerEmojiList;
    private List<XEmoji> mIOSEmojiList;

    private static XEmojiManager instance;
    private HashMap<String, String> beerEmojiDataMap;

    private static LruCache<Integer, Bitmap> bitmapCache;

    private XEmojiManager() {
        initBeerData();
    }

    public static XEmojiManager getInstance() {
        XEmojiManager manager = instance;
        if(instance == null) {
            synchronized (XEmojiManager.class) {
                manager = instance;
                if(manager == null) {
                    manager = new XEmojiManager();
                    instance = manager;
                }
            }
        }

        return manager;
    }

    private void initBeerData() {
        beerEmojiDataMap = new HashMap<>();
        beerEmojiDataMap.put("gaoxing_1", "高兴");
        beerEmojiDataMap.put("daxiao_2", "大笑");
        beerEmojiDataMap.put("weixiao_3", "微笑");
        beerEmojiDataMap.put("xingfen_4", "兴奋");
        beerEmojiDataMap.put("keai_5", "可爱");
        beerEmojiDataMap.put("mengbi_6", "懵逼");
        beerEmojiDataMap.put("lenghan_7", "冷汗");
        beerEmojiDataMap.put("kuxiao_8", "哭笑");
        beerEmojiDataMap.put("nanguo_9", "难过");
        beerEmojiDataMap.put("daku_10", "大哭");
        beerEmojiDataMap.put("zhayan_11", "眨眼");
        beerEmojiDataMap.put("xiai_12", "喜爱");
        beerEmojiDataMap.put("kouzhao_13", "口罩");
        beerEmojiDataMap.put("guilian_14", "鬼脸");
        beerEmojiDataMap.put("pogai_15", "扑街");
        beerEmojiDataMap.put("baiyan_16", "白眼");
        beerEmojiDataMap.put("fennu_17", "愤怒");
        beerEmojiDataMap.put("beishang_18", "悲伤");
        beerEmojiDataMap.put("wodong_19", "我懂");
        beerEmojiDataMap.put("qinqin_20", "亲亲");
        beerEmojiDataMap.put("mogui_21", "魔鬼");
        beerEmojiDataMap.put("jingqi_22", "惊奇");
        beerEmojiDataMap.put("gandong_23", "感动");
        beerEmojiDataMap.put("huaixiao_24", "坏笑");
        beerEmojiDataMap.put("caimi_25", "财迷");
        beerEmojiDataMap.put("xiasi_26", "吓死");
        beerEmojiDataMap.put("yiwen_27", "疑问");
        beerEmojiDataMap.put("mengquan_28", "蒙圈");
        beerEmojiDataMap.put("doge_29", "doge");
    }

    /**
     * 得到小熊表情包
     *
     * @return 小熊表情包列表
     */
    public List<XEmoji> getBeerEmojis() {
        if(!LangUtils.isEmpty(mBeerEmojiList)) {
            return mBeerEmojiList;
        }
        mBeerEmojiList= new ArrayList<>();

        AssetManager am = CardApplication.getApplication().getResources().getAssets();
        try {
            String[] files = am.list(BEER_EMOJI_PATH);
            if(!LangUtils.isEmpty(files)) {
                for(int i = 0; i < files.length; i++) {
                    XEmoji emoji = new XEmoji();
                    emoji.fileName = files[i];
                    int code = 0;
                    if(!LangUtils.isEmpty(emoji.fileName)){
                        int j = emoji.fileName.indexOf('.');
                        if(beerEmojiDataMap != null) {
                            String key = emoji.fileName.substring(0, j);
                            emoji.serverCode = "[" + beerEmojiDataMap.get(key) + "]";
                        }
                        String[] array = emoji.fileName.substring(0, j).split("_");
                        if(array.length > 1) {
                            code = LangUtils.parseInt(array[1]);
                        }
                    }
                    emoji.code = code;
                    emoji.type = BEER_EMOJI_TYPE;
                    mBeerEmojiList.add(emoji);
                }

                Collections.sort(mBeerEmojiList, new Comparator<XEmoji>() {
                    @Override
                    public int compare(XEmoji lhs, XEmoji rhs) {
                        return lhs.code - rhs.code;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mBeerEmojiList;
    }

    /**
     * 得到IOS表情包列表
     *
     * @return IOS表情包列表
     */
    public List<XEmoji> getIosEmojis() {
        if(!LangUtils.isEmpty(mIOSEmojiList)) {
            return mIOSEmojiList;
        }
        mIOSEmojiList = new ArrayList<>();

        AssetManager am = CardApplication.getApplication().getResources().getAssets();
        try {
            String[] files = am.list(IOS_EMOJI_PATH);
            if(!LangUtils.isEmpty(files)) {
                for(int i = 0; i < files.length; i++) {
                    XEmoji emoji = new XEmoji();
                    emoji.fileName = files[i];
                    int code = 0;
                    if(emoji.fileName != null && emoji.fileName.length() > 2) {
                        int j = emoji.fileName.indexOf('.');
                        emoji.serverCode = emoji.fileName.substring(0, j);
                        String codeStr = emoji.fileName.substring(2, j);
                        try {
                            code = Integer.parseInt(codeStr, 16);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    emoji.code = code;
                    emoji.type = IOS_EMOJI_TYPE;
                    mIOSEmojiList.add(emoji);
                }

                Collections.sort(mIOSEmojiList, new Comparator<XEmoji>() {
                    @Override
                    public int compare(XEmoji lhs, XEmoji rhs) {
                        return lhs.code - rhs.code;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mIOSEmojiList;
    }

    /**
     * 将表情转化成bitmap
     *
     * @param emoji 表情
     * @return bitmap
     */
    public static Bitmap getBitmapFromEmoji(XEmoji emoji) {
        if(emoji == null) {
            return null;
        }

        if(bitmapCache == null) {
            bitmapCache = new LruCache<>(MAX_BITMAP_CACHE_SIZE);
        }

        if(bitmapCache.get(emoji.code) != null) {
            return bitmapCache.get(emoji.code);
        }

        Bitmap image = null;
        if(emoji.type == DELETE_EMOJI_TYPE) {
            Drawable drawable = CardApplication.getApplication().getResources().getDrawable(R.drawable.ic_face_close);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable != null) {
                image = bitmapDrawable.getBitmap();
            }
        } else {
            AssetManager am = CardApplication.getApplication().getResources().getAssets();
            try {
                InputStream is = am.open(getEmojiPath(emoji));
                image = BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(image != null) {
            bitmapCache.put(emoji.code, image);
        }

        return image;
    }

    /**
     * 通过key值得到表情
     *
     * @param key  表情key值，上传服务器的值
     * @return
     */
    public static XEmoji getEmoji(String key) {
        XEmoji emoji = getEmoji(key, BEER_EMOJI_TYPE);
        if(emoji == null) {
            emoji = getEmoji(key, IOS_EMOJI_TYPE);
        }

        return emoji;
    }

    /**
     * 清空cache
     */
    public static void clearEmojiCache() {
        if(bitmapCache != null && bitmapCache.size() > 0) {
            bitmapCache.evictAll();
        }
    }

    public static XEmoji createDeleteEmoji() {
        XEmoji emoji = new XEmoji();
        emoji.code = -1;
        emoji.type = DELETE_EMOJI_TYPE;

        return emoji;
    }

    /**
     * 通过key值得到表情
     *
     * @param key  表情key值，上传服务器的值
     * @param type 表情类型
     * @return
     */
    public static XEmoji getEmoji(String key, int type) {
        XEmoji emoji = null;
        if(type == BEER_EMOJI_TYPE) {
            List<XEmoji> emojis = XEmojiManager.getInstance().getBeerEmojis();
            if(!LangUtils.isEmpty(emojis)) {
                for(int i = 0; i < emojis.size(); i++) {
                    XEmoji e = emojis.get(i);
                    if(e.serverCode != null && e.serverCode.contains(key)) {
                        emoji = e;
                        break;
                    }
                }
            }
        } else {
            List<XEmoji> emojis = XEmojiManager.getInstance().getIosEmojis();
            if(!LangUtils.isEmpty(emojis)) {
                for(int i = 0; i < emojis.size(); i++) {
                    XEmoji e = emojis.get(i);
                    if(e.serverCode != null && e.serverCode.contains(key)) {
                        emoji = e;
                        break;
                    }
                }
            }
        }

        return emoji;
    }

    /**
     * 解析IOS表情
     *
     * @param builder SpannableStringBuilder
     * @param start   起始位置，如果为0，则解析所有匹配的表情
     */
    public static void parseIOSEmojis(SpannableStringBuilder builder, int start) {
        String regex = "0x" + "[0-9a-fA-F]{5}";
        Pattern pattern = Pattern.compile(regex);
        parseSpannable(builder, pattern, IOS_EMOJI_TYPE, start);
    }

    /**
     * 解析小熊表情
     *
     * @param builder SpannableStringBuilder
     * @param start   起始位置，如果为0，则解析所有匹配的表情
     */
    public static void parseBeerEmojis(SpannableStringBuilder builder, int start) {
        String regex = "\\[[^\\]]+\\]";
        Pattern pattern = Pattern.compile(regex);
        parseSpannable(builder, pattern, BEER_EMOJI_TYPE, start);
    }

    private static void parseSpannable(SpannableStringBuilder builder, Pattern pattern, int type, int start) {
        Matcher matcher = pattern.matcher(builder);
        while(matcher.find()) {
            if(matcher.start() < start) {
                continue;
            }
            String key = matcher.group();
            XEmoji emoji = getEmoji(key, type);
            Bitmap bitmap = XEmojiManager.getBitmapFromEmoji(emoji);
            Drawable drawable = new BitmapDrawable(CardApplication.getApplication().getResources(), bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            //要让图片替代指定的文字就要用ImageSpan
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static String getEmojiPath(XEmoji emoji) {
        if(emoji == null) {
            return "";
        }

        String path = "";
        if(emoji.type == 0) {
            path = BEER_EMOJI_PATH + File.separator + emoji.fileName;
        } else {
            path = IOS_EMOJI_PATH + File.separator + emoji.fileName;
        }

        return path;
    }
}
