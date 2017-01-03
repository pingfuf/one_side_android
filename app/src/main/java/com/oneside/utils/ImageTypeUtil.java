package com.oneside.utils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ZhanTao on 4/25/16.
 */
public class ImageTypeUtil {

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            String hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件路径获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileType(String filePath) {
        String bytes = getFileHeader(filePath);
        if (bytes != null) {
            if (bytes.contains("FFD8FF")) {
                return "jpg";
            } else if (bytes.contains("89504E47")) {
                return "png";
            } else if (bytes.contains("47494638")) {
                return "gif";
            } else if (bytes.contains("49492A00")) {
                return "tif";
            } else if (bytes.contains("424D")) {
                return "bmp";
            }
        }
        return null;
    }


    /**
     * 根据文件路径获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }
}
