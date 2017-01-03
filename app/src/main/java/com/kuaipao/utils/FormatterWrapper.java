package com.kuaipao.utils;

import java.util.Formatter;

/**
 * @author Bo Hu
 *         created at 2/28/11, 11:07 PM
 */
public class FormatterWrapper {
    private Formatter formatter;
    private StringBuilder buf;

    FormatterWrapper() {
        buf = new StringBuilder(256);
        formatter = new Formatter(buf);
    }

    public String format(String format, Object... args) {
        try {
            buf.setLength(0);
            formatter.format(format, args);
            String v = buf.toString();
            return v;
        } catch (Exception e) {
            LogUtils.e(e, "Failed to format " + format);
            return format;
        }
    }
}
