package com.kuaipao.base.model;

import com.kuaipao.model.beans.XEmoji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pingfu on 16-7-6.
 */
public class EmojiData {
    public static List<XEmoji> beerEmojis;
    public static List<XEmoji> iosEmojis;
    static {
        beerEmojis = new ArrayList<>();
        beerEmojis.add(new XEmoji(1, 0, ""));
    }
    //系统表情
    public static int[] sysEmojiData = {
            0x1F601, 0x1F602,
            0x1F603, 0x1F604,
            0x1F605, 0x1F606,
            0x1F609, 0x1F60A,
            0x1F60B, 0x1F60C,
            0x1F60D, 0x1F60F,
            0x1F612, 0x1F613,
            0x1F614, 0x1F616,
            0x1F618, 0x1F61A,
            0x1F61C, 0x1F61D,
            0x1F61E, 0x1F620,
            0x1F621, 0x1F622,
            0x1F623, 0x1F624,
            0x1F625, 0x1F628,
            0x1F629, 0x1F62A,
            0x1F62B, 0x1F62D,
            0x1F630, 0x1F631,
            0x1F632, 0x1F633,
            0x1F635, 0x1F637,
            0x1F638, 0x1F639,
            0x1F63A, 0x1F63B,
            0x1F63C, 0x1F63D,
            0x1F63E, 0x1F63F,
            0x1F640, 0x1F645,
            0x1F646, 0x1F647,
            0x1F648, 0x1F649,
            0x1F64A, 0x1F64B,
            0x1F64C, 0x1F64D,
            0x1F64E, 0x1F64F
    };

    public static int[] userEmojiData = {
            0x0F001,0x0F002,
            0x0F003,0x0F004,
            0x0F005,0x0F006,
            0x0F007,0x0F008,
            0x0F009,0x0F00A,
            0x0F00B,0x0F00C,
            0x0F00D,0x0F00E,
            0x0F00F,0x0F010,
            0x0F011,0x0F012,
            0x0F013,0x0F014,
            0x0F015,0x0F016,
            0x0F017,0x0F018,
            0x0F019,0x0F01A,
            0x0F01B,0x0F01C,
            0x0F01D
    };

    /**
     * 获取表情源数据
     */
    public static ArrayList initEmojiString() {
        ArrayList list = new ArrayList<>();
        for (int i = 0; i < sysEmojiData.length; i++) {
            list.add(getEmojiStringByUnicode(sysEmojiData[i]));
        }

        return list;
    }

    /**
     * 将int对应的表情转换为String类型
     */
    public static String getEmojiStringByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static boolean isUserEmoji(int i) {
        return i >= userEmojiData[0] && i <= userEmojiData[userEmojiData.length - 1];
    }
}
