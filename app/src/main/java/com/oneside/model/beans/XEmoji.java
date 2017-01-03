package com.oneside.model.beans;

import java.io.Serializable;

/**
 * 表情包数据结构
 * <p/>
 * 表情对应的文字为：小熊表情 "[中文名称]"，例如：[微笑], iosEmoji:code(例如HexCode: 0x123121)
 * </p>
 * 表情包分为小熊表情包和iOS表情包，小熊表情包的type=0，iOS表情包type=1
 * Created by pingfu on 16-7-7.
 */
public class XEmoji implements Serializable {
    private static long serialVersionUID = 42L;

    /**
     * 表情编号
     */
    public int code;

    /**
     * 表情类型：0为小熊表情包，1为iOS表情包, 2为删除图标
     */
    public int type;

    /**
     * 表情文件名称
     */
    public String fileName;

    /**
     * 上传服务器的code
     */
    public String serverCode;

    public XEmoji() {
    }

    public XEmoji(int code, int type, String name) {
        this.code = code;
        this.type = type;
        this.fileName = name;
    }
}
