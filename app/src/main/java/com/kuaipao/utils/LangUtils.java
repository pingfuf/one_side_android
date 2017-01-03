package com.kuaipao.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;



import java.lang.ref.Reference;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

import static com.kuaipao.utils.LogUtils.i;
import static com.kuaipao.utils.LogUtils.w;

/**
 * Class to help handle basic type computation and conversion. And dynamic using of Classes.
 *
 * @author Bo Hu
 */
public class LangUtils {
    // For Event Uid2445
    public static final int PPY_UID2445_LENGTH = 26;
    public static final Random RANDOM = new Random();

    /**
     * Different Strings connecting modes.
     *
     * @author Bo Hu
     */
    public static final class ConcatMode {
        public static final int IGNORE_NULL = 0;
        public static final int IGNORE_EMPTY = 1;
        public static final int IGNORE_NONE = 2;
    }

    @NonNull
    private static final StringBuilderPool STRING_BUILDER_POOL = new StringBuilderPool();

    @NonNull
    private static final BytesPool BYTES_POOL = new BytesPool();

    private static final FormatterPool FORMATTER_POOL = new FormatterPool();

    private LangUtils() {
    }

    /**
     * @param text
     * @return
     */
    public static boolean parseBoolean(Object text) {
        if (text instanceof Integer) {
            return (Integer) text > 0;
        } else if (text instanceof Long) {
            return (Long) text > 0;
        } else if (text instanceof String) {
            if (isEmpty((String) text)) {
                return false;
            }
            return text.equals("true");
        } else if (text instanceof Boolean) {
            return (Boolean) text;
        }
        return false;
    }

    /**
     * Parse int from a String object.
     *
     * @param text
     * @return int
     */
    public static int parseInt(String text) {
        return parseInt(text, -1);
    }

    public static String parseString(Object o) {
        return parseString(o, "");
    }

    public static String parseString(Object o, String defaultValue) {
        String ret = String.valueOf(o);
        if (ret == null || "null".equalsIgnoreCase(ret)) {
            ret = defaultValue;
            // LogUtils.d("parseString ret = null o = %s",o);
        }
        return ret;
    }

    /**
     * Parse int from an Object.
     *
     * @param o
     * @return int
     */
    public static int intValue(Object o) {
        return intValue(o, -1);
    }

    /**
     * Parse int from an Object based on default value.
     *
     * @param o
     * @param defaultValue
     * @return int
     */
    public static int intValue(@CheckForNull Object o, int defaultValue) {
        if (o == null)
            return defaultValue;
        if (o instanceof Number)
            return ((Number) o).intValue();
        else if (o instanceof String)
            return parseInt((String) o, defaultValue);
        else
            LogUtils.w("unknown type to get int, %s", o);
        return defaultValue;
    }

    public static int parseInt(@CheckForNull Object text, int defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Integer.parseInt(String.valueOf(text));
        } catch (Exception e) {
            LogUtils.w("failed to parse int Object [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    public static long parseLong(@CheckForNull Object text, long defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Long.parseLong(String.valueOf(text));
        } catch (Exception e) {
            LogUtils.w("failed to parse long [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    public static double parseDouble(@CheckForNull Object text, double defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Double.parseDouble(String.valueOf(text));
        } catch (Exception e) {
            LogUtils.w("failed to parse double [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    public static int parseIntWithMax(@CheckForNull Object text, int length) {
        int ret = parseInt(text, 0);
        ret = ret >= length - 1 ? length - 1 : ret;
        return ret;
    }

    /**
     * Parse int from a String object based on a default value.
     *
     * @param text
     * @param defaultValue
     * @return
     */
    public static int parseInt(@CheckForNull String text, int defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            LogUtils.w(e, "failed to parse int [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    public static long longValue(Object o) {
        return intValue(o, -1);
    }

    public static long longValue(@CheckForNull Object o, long defaultValue) {
        if (o == null)
            return defaultValue;
        if (o instanceof Number)
            return ((Number) o).longValue();
        else if (o instanceof String)
            return parseLong((String) o, defaultValue);
        else
            LogUtils.w("unknown type to get long, %s", o);
        return defaultValue;
    }

    public static long parseLong(@CheckForNull String text, long defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Long.parseLong(text);
        } catch (Exception e) {
            LogUtils.w(e, "failed to parse long [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    public static double parseDouble(@CheckForNull String text, double defaultValue) {
        if (text == null)
            return defaultValue;
        try {
            return Double.parseDouble(text);
        } catch (Exception e) {
            LogUtils.w(e, "failed to parse double [%s], using default value [%s]", text, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Parse byte[] from a String object.
     *
     * @param s
     * @return byte[]
     */
    @NonNull
    public static byte[] getBytes(@CheckForNull String s) {
        try {
            if (s != null)
                return s.getBytes("UTF-8");
        } catch (Exception e) {
            w(e, "Failed to getBytes: " + s);
        }

        return new byte[0];
    }

    /**
     * Parse part of a String object.
     *
     * @param content
     * @param maxLen
     * @return String
     */
    public static String headString(@NonNull String content, int maxLen) {
        if (content.length() <= maxLen)
            return content;
        return content.substring(0, maxLen);
    }

    /**
     * Connect String objects.
     *
     * @param separator
     * @param strings
     * @return String
     */
    @CheckForNull
    public static String concatStrings(String separator, String... strings) {
        return concatStrings(separator, ConcatMode.IGNORE_NULL, strings);
    }

    /**
     * Connect String objects in different modes.
     *
     * @param separator
     * @param mode
     * @param strings
     * @return
     */
    @CheckForNull
    public static String concatStrings(String separator, int mode, @NonNull String... strings) {
        StringBuilder buffer = acquireStringBuilder(0);
        int count = 0;

        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];

            if (mode == ConcatMode.IGNORE_NONE
                    || (mode == ConcatMode.IGNORE_EMPTY && !LangUtils.isEmpty(s))
                    || (mode == ConcatMode.IGNORE_NULL && s != null)) {
                if (count > 0)
                    buffer.append(separator);
                buffer.append(s);
                count++;
            }
        }

        return releaseStringBuilder(buffer);
    }

    /**
     * Connect int(s) to String.
     *
     * @param separator
     * @param ints
     * @return String
     */
    @CheckForNull
    public static String concatIntArrays(String separator, @NonNull int... ints) {
        StringBuilder builder = acquireStringBuilder(0);
        for (int i = 0; i < ints.length; i++) {
            if (i > 0)
                builder.append(separator);
            builder.append(String.valueOf(i));
        }
        return releaseStringBuilder(builder);
    }

    /**
     * Trim from the left of a String object.
     *
     * @param text
     * @return String
     */
    @NonNull
    public static String leftTrim(@NonNull String text) {
        char[] charArray = text.toCharArray();
        int start = 0;
        while (start < charArray.length && charArray[start] == ' ')
            start++;

        return new String(charArray, start, charArray.length - start);
    }

    /**
     * Check if a CharSequence is empty.
     *
     * @param s
     * @return boolean
     */
    public static boolean isEmpty(@CheckForNull CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    /**
     * Check if a byte[] is not empty.
     *
     * @param s
     * @return boolean
     */
    public static boolean isNotEmpty(byte[] s) {
        return s != null && s.length > 0;
    }

    /**
     * Check if a CharSequence is not empty.
     *
     * @param s
     * @return boolean
     */
    public static boolean isNotEmpty(CharSequence s) {
        return s != null && s.length() > 0;
    }

    /**
     * @param <E>
     * @param list
     * @return
     */
    public static <E> boolean isEmpty(@CheckForNull Collection<E> list) {
        return list == null || list.size() == 0;
    }

    /**
     * @param list
     * @return
     */
    public static <E> boolean isNotEmpty(Collection<E> list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @param <E>   泛型，数组的数据类型
     *
     * @return 数组是否为空
     */
    public static<E> boolean isEmpty(E[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Convert a Object to a String object.
     *
     * @param o
     * @return String
     */
    @CheckForNull
    public static String objectToString(Object o) {
        String ret = "";
        if (o == null)
            return null;
        else if (o instanceof byte[])
            ret = LangUtils.utf8_decode(o);
        else if (o instanceof List) {
            List k = (List) o;
            ret += "[";
            int i = 0;
            for (Iterator t = k.iterator(); t.hasNext(); ) {
                ret += objectToString(t.next());
                if (i < k.size() - 1)
                    ret += ",";
                i++;
            }
            ret += "]";
        } else if (o instanceof String[]) {
            String[] sa = (String[]) o;
            ret += "[";
            for (int i = 0; i < sa.length; i++) {
                ret += sa[i];
                if (i < sa.length - 1)
                    ret += ",";
            }
            ret += "]";
        } else if (o instanceof Integer[]) {
            Integer[] ia = (Integer[]) o;
            ret += "[";
            for (int i = 0; i < ia.length; i++) {
                ret += ia[i];
                if (i < ia.length - 1)
                    ret += ",";
            }
            ret += "]";
        } else if (o instanceof Map) {
            Map h = (Map) o;
            ret += "{";
            int i = 0;
            for (Iterator<Map.Entry> t = h.entrySet().iterator(); t.hasNext(); ) {
                Map.Entry x = t.next();
                ret += objectToString(x.getKey());
                ret += ":";
                ret += objectToString(x.getValue());
                if (i < h.size() - 1)
                    ret += ",";
                i++;
            }
            ret += "}";
        } else if (o instanceof HashSet) {
            HashSet h = (HashSet) o;
            ret += "[";
            int i = 0;
            for (Iterator t = h.iterator(); t.hasNext(); ) {
                ret += objectToString(t.next());
                if (i < h.size() - 1)
                    ret += ",";
                i++;
            }
            ret += "]";
        } else
            ret = o.toString();
        return ret;
    }

    /**
     * Convert a Object array to String.
     *
     * @param array
     * @return String
     */
    public static String toString(@CheckForNull Object[] array) {
        if (array == null)
            return "null";

        StringBuilder builder = acquireStringBuilder(0);
        builder.append('[');
        for (int i = 0; i < array.length; i++) {
            Object o = array[i];
            if (o == null)
                builder.append("null");
            if (i < array.length - 1)
                builder.append(",");
        } // end of [array] iteration
        builder.append(']');

        return releaseStringBuilder(builder);
    }

    /**
     * Convert an Object to a String object.
     *
     * @param o
     * @return String
     */
    @NonNull
    public static String toString(@CheckForNull Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * Encode a String object to byte[] with UTF8 format.
     *
     * @param s
     * @return byte[]
     */
    public static byte[] utf8_encode(String s) {
        return getBytes(s);
    }

    /**
     * Encode a Object to String object with UTF8 format.
     *
     * @param o
     * @return String
     */
    public static String utf8_decode(Object o) {
        if (o instanceof byte[])
            return utf8String(((byte[]) o), null);
        else
            return toString(o);
    }

    /**
     * Get UTF8 format String from a byte[] data based on default value.
     *
     * @param data
     * @param defaultValue
     * @return String
     */
    public static String utf8String(@CheckForNull byte[] data, String defaultValue) {
        if (data == null)
            return defaultValue;

        try {
            int index = 0;
            if (data.length >= 3 && data[0] == (byte) 0xEF && data[1] == (byte) 0xBB
                    && data[2] == (byte) 0xBF)
                index = 3;

            return new String(data, index, data.length - index, "UTF-8");
        } catch (Exception e) {
            LogUtils.e(e, "Failed to get utf8String");
        }

        return defaultValue;
    }

    /**
     * translate
     *
     * @param formater
     * @param aDate
     * @return
     */
    public static String date2String(String formater, Date aDate) {
        if (formater == null || "".equals(formater))
            return null;
        if (aDate == null)
            return null;
        return (new SimpleDateFormat(formater)).format(aDate);
    }


    /**
     * compare to each other, return earlier date.
     *
     * @param d1 Date time
     * @param d2
     * @return
     */
    public static Date earlierDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            if (d1 != null) {
                return d1;
            } else if (d2 != null) {
                return d2;
            }
            return null;
        }
        return d1.before(d2) ? d1 : d2;
    }

    /**
     * compare to each other, return later date.
     *
     * @param d1 Date time
     * @param d2
     * @return
     */
    public static Date laterDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            if (d1 != null) {
                return d1;
            } else if (d2 != null) {
                return d2;
            }
            return null;
        }
        return d1.after(d2) ? d1 : d2;
    }

    /**
     * return date from timestamp text
     *
     * @param timestamp
     * @return
     */
    public static Date dateFromString(String timestamp) {
        long r = parseLong(timestamp, 0);
        return r > 0 ? new Date(r) : null;
        // return new Date(parseLong(timestamp, 0));

    }

    /**
     * @param date
     * @param sinceDate
     * @return date - sinceDate
     */
    public static long timeIntervalSinceDate(Date date, Date sinceDate) {
        if (date == null || sinceDate == null) {
            return 0;
        }
        return date.getTime() - sinceDate.getTime();
    }

    /**
     * @param date     the date to format
     * @param timeZone the timeZone used
     * @return
     */
    public static String formatDate(Date date, TimeZone timeZone) {
        // Calendar c = Calendar.getInstance();
        // c.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        format.setTimeZone(timeZone);
        String ret = format.format(date);

        return ret;
    }

    public static Date formatAllDayDate(String time) {
        return formatDate(time, "yyyy-MM-dd");
    }
    public static Date formatTypeNormalDate(String time) {
        return formatDate(time, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static Date formatType1Date(String time) {
        return formatDate(time, "yyyyMMdd'T'HHmmss");
    }

    public static String formatTypeNormalTime(Date d) {
        if (d == null) {
            return "";
        }
        String formatter = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        return format.format(d);
    }

    public static String formatType1Time(Date d) {
        if (d == null) {
            return "";
        }
        String formatter = "yyyyMMdd'T'HHmmss";
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        return format.format(d);
    }

    public static Date formatDate(String time, String f) {
        SimpleDateFormat format = new SimpleDateFormat(f);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String icsDateFormatter(Date date) {
        return formatDate(date, TimeZone.getTimeZone("GMT+0"));
    }

    /***
     * Get day of month 1-30 or 31 29
     *
     * @param date
     * @return
     */
    public static int getMonthDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get day of week 1-7
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
        if(dateEnd == null || dateStart == null) {
            return 0;
        }

        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * Get a String object from a Object.
     *
     * @param o
     * @return
     */
    @CheckForNull
    public static String stringValue(@CheckForNull Object o) {
        if (o == null)
            return null;
        if (o instanceof byte[])
            return utf8String((byte[]) o, null);
        return o.toString();
    }

    /**
     * Get a String object with a specific format.
     *
     * @param format
     * @param args
     * @return String
     */
    @NonNull
    public static String format(String format, Object... args) {
        FormatterWrapper f = FORMATTER_POOL.acquire();
        String s = f.format(format, args);
        FORMATTER_POOL.release(f);

        return s;
    }

    /**
     * Check if a String object is null.
     *
     * @param v
     * @param defaultValue
     * @return String
     */
    public static String nonNullString(@CheckForNull String v, String defaultValue) {
        return v == null ? defaultValue : v;
    }

    /**
     * Get deviceID in a String object.
     *
     * @param ctx
     * @return String
     */
    @NonNull
    public static String getDeviceID(@CheckForNull Context ctx) {
        if (ctx == null)
            return "";
        String duid = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return (duid == null || duid.length() == 0) ? "emulator" : duid;
    }

    /**
     * Check if it's emulator.
     *
     * @param ctx
     * @return boolean
     */
    public static boolean isEmulator(Context ctx) {
        return getDeviceID(ctx).equals("emulator") || "sdk".equals(android.os.Build.MODEL)
                || "google_sdk".equals(android.os.Build.MODEL);
    }

    /**
     * Get int from a list.
     *
     * @param l
     * @param index
     * @return int
     */
    public static int sgetInt(List<?> l, int index) {
        return intValue(sget(l, index, null), 0);
    }

    /**
     * Get int from a list.
     *
     * @param l
     * @param index
     * @param defaultValue
     * @return int
     */
    public static int sgetInt(List<?> l, int index, int defaultValue) {
        return intValue(sget(l, index, null), defaultValue);
    }

    public static long sgetLong(List<?> l, int index) {
        return longValue(sget(l, index, null), 0);
    }

    public static long sgetLong(List<?> l, int index, long defaultValue) {
        return longValue(sget(l, index, null), defaultValue);
    }

    /**
     * Get value from a list.
     *
     * @param <T>
     * @param l
     * @param index
     * @return
     */
    public static <T> T sget(List<T> l, int index) {
        return sget(l, index, (T) null);
    }

    /**
     * Get value from a list.
     *
     * @param <T>
     * @param l
     * @param index
     * @param defaultValue
     * @return T
     */
    public static <T> T sget(@CheckForNull List<T> l, int index, T defaultValue) {
        if (l == null || l.size() <= index)
            return defaultValue;
        try {
            return (T) l.get(index);
        } catch (Exception e) {
            LogUtils.w("failed to get value of %d from list %s: %s", index, l, e);
            return defaultValue;
        }
    }

    /**
     * Clear all the references by providing a Reference list.
     *
     * @param <T>
     * @param l
     */
    public static <T> void clearReferences(@NonNull List<? extends Reference<T>> l) {
        Iterator<? extends Reference<T>> it = l.iterator();
        while (it.hasNext()) {
            Reference<T> r = it.next();
            if (r.get() == null)
                it.remove();
        }
    }

    /**
     * Clear all the references by providing a Reference map.
     *
     * @param <K>
     * @param <V>
     * @param m
     */
    public static <K, V> void clearReferences(@NonNull Map<K, ? extends Reference<V>> m) {
        Iterator<? extends Map.Entry<K, ? extends Reference<V>>> it = m.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, ? extends Reference<V>> entry = it.next();
            if (entry.getValue() == null || entry.getValue().get() == null)
                it.remove();
        }
    }

    /**
     * Convert an empty String to null.
     *
     * @param s
     * @return
     */
    @CheckForNull
    public static String emptyAsNull(@CheckForNull String s) {
        if (s == null || s.length() == 0)
            return null;
        return s;
    }

    /**
     * Convert null to an empty String.
     *
     * @param s
     * @return String
     */
    @NonNull
    public static String nullAsEmpty(@CheckForNull CharSequence s) {
        return s == null ? "" : s.toString();
    }

    /**
     * A safe thread-sleep method.
     *
     * @param ms
     */
    public static void safeSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            i("Failed to sleep:" + e);
        }
    }

    /**
     * Acquire StringBulder from a linked list.
     *
     * @param capacity
     * @return StringBuilder
     */
    @NonNull
    public static StringBuilder acquireStringBuilder(int capacity) {
        return STRING_BUILDER_POOL.acquire(capacity);
    }

    /**
     * Acquire String data from a linked list.
     *
     * @param sb
     * @return String
     */
    @NonNull
    public static String releaseStringBuilder(@NonNull StringBuilder sb) {
        return STRING_BUILDER_POOL.release(sb);
    }

    /**
     * Release String data at the last of a Linked list.
     *
     * @param sb
     */
    public static void releaseOnly(@NonNull StringBuilder sb) {
        STRING_BUILDER_POOL.releaseOnly(sb);
    }

    /**
     * Acquire byte[] data from a linked list.
     *
     * @param minCapacity
     * @return
     */
    @NonNull
    public static byte[] acquireBytes(int minCapacity) {
        return BYTES_POOL.acquire(minCapacity);
    }

    /**
     * Release byte[] data at the last of a Linked list.
     *
     * @param buf
     */
    public static void releaseBytes(@NonNull byte[] buf) {
        BYTES_POOL.release(buf);
    }

    /**
     * Regular equals method for this class.
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equal(@CheckForNull Object a, @CheckForNull Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Manually test a int. Most for Setting and configuration setting.
     *
     * @param v
     * @param bit
     * @return boolean
     */
    public static boolean bitTest(int v, int bit) {
        return (v & (1 << bit)) > 0;
    }

    /**
     * Manually set some kind of settings.
     *
     * @param v
     * @param bit
     * @param oneOrZero
     * @return int
     */
    public static int bitSet(int v, int bit, int oneOrZero) {
        if (oneOrZero > 0)
            return v | (1 << bit);
        else
            return v & (~(1 << bit));
    }

    /**
     * Check if a specific class exists.
     *
     * @param className
     * @return
     */
    public static boolean existClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Create a new instance of a class with it's class name.
     *
     * @param <T>
     * @param className
     * @return T
     */
    @CheckForNull
    public static <T> T newInstance(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Throwable e) {
            w(e, "Failed to get instance of " + className);
        }
        return null;
    }

    public static <T> T newSubClassInstance(String className, String parent) {
        if (existClass(className))
            return (T) newInstance(className);
        return (T) newInstance(parent);
    }

    /**
     * Clear the class.
     */
    public static void clear() {
        BYTES_POOL.clear();
        STRING_BUILDER_POOL.clear(); // this line did the magic,no more
        // OutOfMemory errors on 1.5, cheers!
        FORMATTER_POOL.clear();
    }

    /**
     * Regular toString method of this class.
     *
     * @param o
     * @param defaultValue
     * @return String
     */
    public static String toString(Object o, String defaultValue) {
        return o == null ? defaultValue : o.toString();
    }

    public static String intToUnitString(int number) {
        String text;
        if (number > 1000 * 1000 * 1000) {
            text = number / (1000 * 1000 * 1000) + "g";
        } else if (number > 1000 * 1000) {
            text = number / (1000 * 1000) + "m";
        } else if (number > 1000) {
            text = number / 1000 + "k";
        } else {
            text = String.valueOf(number);
        }
        return text;
    }

    /**
     * @param length the string's length
     * @return randomString
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @param date
     * @return
     */

    public static long translateDateToTimeStamp(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return 0;// getCurrentTimeStamp();
    }


    /**
     * Simulate IOS Date method.
     *
     * @param date
     * @param delay milliseconds
     * @return
     */
    public static Date dateByAddingTimeInterval(@CheckForNull Date date, long delay) {
        return new Date(date == null ? 0 : date.getTime() + delay);
    }

    /**
     * @param date
     * @param day
     * @return
     */
    public static Date dateByAddingTimeDay(@CheckForNull Date date, int day) {
        return new Date(date.getTime() + day * 86400000l);
    }

    public static int secondsBetweenDate(Date last, Date now) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(last);
        c2.setTime(now);
        return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / 1000l);

    }

    public static int minutesBetweenDate(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);
        return (int) ((c2.getTimeInMillis() - c1.getTimeInMillis()) / 60000l);
    }

    public static int hoursBetweenDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return Integer.MAX_VALUE;
        }
        return hoursBetweenDate(date1, date2, TimeZone.getDefault());
    }

    public static int hoursBetweenDate(Date date1, Date date2, TimeZone tz) {
        if (date1 == null || date2 == null) {
            return Integer.MAX_VALUE;
        }
        return (int) ((date2.getTime() - date1.getTime()) / (60 * 60 * 1000l));
    }

    /**
     * @param date1
     * @param date2
     * @return the days date1 to date2
     */
    public static int daysBetweenDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return Integer.MAX_VALUE;
        }
        return daysBetweenDate(date1, date2, TimeZone.getDefault());

    }

    public static int daysBetweenDate(Date date1, Date date2, TimeZone tz) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return Integer.MAX_VALUE;
        }
        if (Math.abs(date1.getTime() - date2.getTime()) > 150l * 3600 * 24 * 365 * 1000) {
            return date1.before(date2) ? -150 * 365 : 150 * 365;
        }
        int off =
                (int) ((cc_dateByMovingToBeginningOfDay(date1).getTime() - cc_dateByMovingToBeginningOfDay(
                        date2).getTime()) / (3600 * 24 * 1000l));
        return off;
    }

    /**
     * @param date1
     * @param date2
     * @return years between date1 and date2
     */
    public static int yearsBetweenDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return Integer.MAX_VALUE;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        int year1 = c.get(Calendar.YEAR);
        // int allDaysOneYear1 = c.getActualMaximum(Calendar.DAY_OF_YEAR);
        c.setTime(date2);
        int year2 = c.get(Calendar.YEAR);
        return year1 - year2;
    }

    /**
     * @param date
     * @return
     */
    public static int daysToToday(Date date) {
        return daysBetweenDate(date, new Date());
    }

    /**
     * @param date
     * @param feild
     * @param timezone
     * @return
     */
    public static int getDateFeild(Date date, int feild, TimeZone timezone) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(timezone);
        c.setTime(date);
        return c.get(feild);
    }


    /**
     * Support int,long,float,double
     *
     * @param t1
     * @param t2
     * @return
     */
    public static <T extends Number> T max(T t1, T t2) {
        boolean preLarger = true;
        if (t1 instanceof Integer) {
            preLarger = (Integer) t1 > (Integer) t2;
        } else if (t1 instanceof Long) {
            preLarger = (Long) t1 > (Long) t2;
        } else if (t1 instanceof Float) {
            preLarger = (Float) t1 > (Float) t2;
        } else if (t1 instanceof Double) {
            preLarger = (Double) t1 > (Double) t2;
        }

        return preLarger ? t1 : t2;
    }

    /**
     * Support int,long,float,double
     *
     * @param t1
     * @param t2
     * @return
     */
    public static <T extends Number> T min(T t1, T t2) {
        boolean preLarger = true;
        if (t1 instanceof Integer) {
            preLarger = (Integer) t1 > (Integer) t2;
        } else if (t1 instanceof Long) {
            preLarger = (Long) t1 > (Long) t2;
        } else if (t1 instanceof Float) {
            preLarger = (Float) t1 > (Float) t2;
        } else if (t1 instanceof Double) {
            preLarger = (Double) t1 > (Double) t2;
        }

        return preLarger ? t2 : t1;
    }

    /**
     * @param dict1
     * @param dict2
     * @return different key between dict1 and dict2
     */
    public static ArrayList<String> differentKeysCompareWith(HashMap<String, Object> dict1,
                                                             HashMap<String, Object> dict2) {
        if (dict1 == null && dict2 == null) {
            return null;
        } else if (dict1 == null && dict2 != null) {
            return new ArrayList<String>(dict2.keySet());
        } else if (dict1 != null && dict2 == null) {
            return new ArrayList<String>(dict1.keySet());
        }

        ArrayList<String> modifiedKeys = new ArrayList<String>();
        ArrayList<String> allKeys = new ArrayList<String>(dict1.keySet());// [[NSMutableArray
        // alloc]
        // initWithArray:self.allKeys];

        for (String key : dict2.keySet()) {
            if (!allKeys.contains(key)) {
                allKeys.add(key);
            }
        }

        for (String key : allKeys) {
            Object originalValue = dict1.get(key);
            Object modifiedValue = dict2.get(key);
            // BOOL (^compareFunction)(id, id);
            boolean same = false;
            if (originalValue == null || modifiedValue == null) {
                if (originalValue == modifiedValue) {
                    same = true;
                } else {
                    same = false;
                }

            } else if (originalValue.getClass() != (modifiedValue.getClass())) {
                same = false;
            } else {
                if (originalValue instanceof String)
                    same = originalValue.equals(modifiedValue);

                else if (originalValue instanceof Number)
                    same = originalValue.equals(modifiedValue);

                else if (originalValue instanceof ArrayList || originalValue instanceof HashMap) {
                    String s1 = originalValue.toString();
                    String s2 = modifiedValue.toString();
                    same = s1.equals(s2);
                    // compareFunction = ^(id value1, id value2) {
                    // NSString *s1 = [PPYWebUtils jsonString:value1];
                    // NSString *s2 = [PPYWebUtils jsonString:value2];
                    // return [s1 isEqualToString:s2];
                    // };
                } else
                    same = originalValue.equals(modifiedValue);

            }

            if (!same)
                modifiedKeys.add(key);

        }

        return modifiedKeys;

    }

    /**
     * @param dict
     * @param keys
     * @return the hashmap contains the keys
     */

    public static HashMap<String, Object> dictionaryWithValuesForKeys(HashMap<String, Object> dict,
                                                                      ArrayList<String> keys) {
        if (dict == null || dict.size() <= 0 || isEmpty(keys)) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (String key : keys) {
            if (dict.containsKey(key)) {
                map.put(key, dict.get(key));
            }
        }
        return map;
    }

    /**
     * @param array
     * @param setIndex
     * @return
     */
    public static <T> ArrayList<T> objectsAtIndexes(ArrayList<T> array, Set<Integer> setIndex) {
        if (isEmpty(array) || isEmpty(setIndex)) {
            return null;
        }
        ArrayList<T> list = new ArrayList<T>();
        for (int i = 0; i < array.size(); i++) {
            if (setIndex.contains(i)) {
                list.add(array.get(i));
            }
        }
        return list;
    }

    /**
     * move to the 0 clock current timezone
     *
     * @param date
     */
    public static Date cc_dateByMovingToBeginningOfDay(Date date) {
        return cc_dateByMovingToBeginningOfDay(date, TimeZone.getDefault());
    }

    public static Date cc_dateByMovingToBeginningOfDay(Date date, TimeZone tz) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance(tz);
        // calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // date.setTime(calendar.getTimeInMillis());
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        // calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        // date.setTime(calendar.getTimeInMillis());
        return calendar.getTime();
    }

    public static Date cc_dateByMovingToFollowingDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToSameDayOfTheFollowingNumMonth(Date date, int num) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        // calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheFollowingYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    public static Date cc_dateByMovingToLastDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentWeek(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.WEEK_OF_YEAR, -1);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentMonth(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date cc_dateByMovingToFirstDayOfTheCurrentYear(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }


    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute, second);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * set year&month&day of the Caledar
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getDateYMD(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * return Arraylist
     *
     * @param text
     * @param split
     * @return
     */
    public static ArrayList<String> split(String text, String split) {
        if (isEmpty(text) || isEmpty(split)) {
            return null;
        }

        String[] array = text.split(split);
        if (array != null && array.length > 0) {
            ArrayList<String> ret = new ArrayList<String>();
            for (int i = 0; i < array.length; i++) {
                ret.add(array[i]);
            }
            return ret;
        }
        return null;
    }


    public static <E> ArrayList<E> reverse(ArrayList<E> array) {
        ArrayList<E> r = new ArrayList<E>(array);
        if (r.size() > 0) {
            int i = 0;
            int j = r.size() - 1;
            while (i < j) {
                E e = r.get(i);
                r.set(i, r.get(j));
                r.set(j, e);
                i++;
                j--;
            }
        }
        return r;

        // NSArray * result = [NSArray arrayWithArray:_a];
    }

    /**
     * @param list
     * @return the first obj or null
     */
    public static <E> E getFirstObj(List<E> list) {
        return isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * @param list
     * @return the last obj or null
     */
    public static <E> E getLastObj(ArrayList<E> list) {
        return isNotEmpty(list) ? list.get(list.size() - 1) : null;
    }

    /**
     * get enum value to keep from out of range.
     *
     * @param e
     * @param index
     * @return
     */
    public static <E> E getEnumObj(E[] e, Object index) {
        int length = e.length;
        return e[parseIntWithMax(index, length)];
    }

    /**
     * keep add value not null.
     *
     * @param dict
     * @param key
     * @param value
     */
    public static void addKeyValueIntoDict(HashMap<String, Object> dict, String key, Object value) {
        if (dict == null || isEmpty(key) || value == null) {
            return;
        }
        if (value instanceof Collection && ((Collection<?>) value).size() <= 0) {
            return;
        }
        if (value instanceof Map && ((Map<?, ?>) value).size() <= 0) {
            return;
        }
        dict.put(key, value);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH))
            return true;
        return false;
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH))
            return true;
        return false;
    }

    public static boolean isSameWeek(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR))
            return true;
        return false;
    }

    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            w(" date is invalid date1 = %s date2 = %s", date1, date2);
            return false;
        }
        Calendar calendar1 = Calendar.getInstance(), calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR))
            return true;
        return false;
    }

    /**
     * Format yyyy-MM-dd date String
     *
     * @param d
     * @return
     */
    public static String formatAlldayTime(Date d) {
        return formatAlldayTime(d, null);
    }

    public static String formatAlldayTime(Date d, TimeZone timeZone) {
        if (d == null) {
            return "";
        }
        String formatter = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.getDefault());
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format.format(d);
    }

    /**
     * Format the date to @param formater string
     * @param date
     * @param formatter
     * @return
     */
    public static String formatDate(Date date, String formatter){
        if (date == null || isEmpty(formatter)) {
            return "";
        }

        SimpleDateFormat format = new SimpleDateFormat(formatter, Locale.getDefault());
        return format.format(date);
    }

    /**
     * @param d
     * @param timeZone
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(Date d, TimeZone timeZone) {
        if (d == null) {
            return "";
        }
        String formatter = "yyyy-MM-dd'T'HH:mm:ssZ";
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        String ret = format.format(d);
        /*********** 2011-04-27T21:00:00+0800 translate into 2011-04-27T21:00:00+08:00 **************/
        if (ret != null && ret.length() > 2
                && !":".equals(ret.substring(ret.length() - 3, ret.length() - 2))) {
            ret = ret.substring(0, ret.length() - 2) + ":" + ret.substring(ret.length() - 2);
        }
        return ret;
    }

    public static String formatTime(Date date) {
        return formatTime(date, TimeZone.getTimeZone("GMT+0"));
    }


    private static Timer globalTimer;

    /**
     * Schedule a task after a delay time
     *
     * @param task
     * @param delay
     */
    public static void schedule(TimerTask task, long delay) {
        if (globalTimer == null) {
            globalTimer = new Timer("kiwi");
        }
        globalTimer.schedule(task, delay);
    }

    /**
     * Schedule a task after a delay time and repeat for a period time
     *
     * @param task
     * @param delay
     */
    public static void schedule(TimerTask task, long delay, long period) {
        if (globalTimer == null) {
            globalTimer = new Timer("kiwi");
        }
        globalTimer.schedule(task, delay, period);
    }


    /**
     * @param str 需要过滤的字符串
     * @return
     * @Description:过滤数字以外的字符
     */
    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();

    }

    /**
     * @param phoneString
     * @return
     * @Description: parse phone from phoneString coming from web
     */
    public static String[] getPhone(String phoneString) {
        if (LangUtils.isEmpty(phoneString))
            return null;
        String[] phoneArrays = null;
        if (phoneString.contains(" ")) {
            phoneArrays = phoneString.split(" ");
        } else if (phoneString.contains(",")) {
            phoneArrays = phoneString.split(",");
        } else if (phoneString.contains("，")) {
            phoneArrays = phoneString.split("，");
        } else {
            phoneArrays = new String[1];
            phoneArrays[0] = phoneString;
        }
        return phoneArrays;
    }

    /**
     * 将距离转化成文字
     *
     * @param distance 距离
     * @return 文字距离
     */
    public static String formatDistance(double distance) {
        String strDistanceTip = "距离";
        DecimalFormat mFormat = new DecimalFormat("#0.0");
        if (distance <= 0.000001) {
            return "";
        } else if (distance > 0.000001 && distance < 1000) {
            return strDistanceTip + "<1km";
        } else if (distance >= 1000 && distance < 10000) {
            return strDistanceTip + mFormat.format(distance / 1000) + "km";
        } else {
            return strDistanceTip + String.format("%dkm", (int) (distance / 1000));
        }
    }

    /**
     * 将价格转化成文字
     *
     * @param price 价格
     * @return 文字价格
     */
    public static String formatPrice(int price) {
        if(price < 0) {
            return "";
        }

        return "" + (new DecimalFormat("#0.0")).format((double)price / 100.0);
    }
}
