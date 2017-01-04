package com.oneside.utils;

import static com.oneside.utils.IOUtils.*;
import static com.oneside.utils.LangUtils.*;
import static com.oneside.utils.LogUtils.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oneside.base.CardConfig;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Class help to handle web processes.
 *
 * @author Bo Hu
 */
public class WebUtils {

    private WebUtils() {
    }

    @NonNull
    private static final byte[] S1 = new byte[8];
    @NonNull
    private static final byte[] S2 = new byte[8];

    static {
        for (int i = 0; i < 8; i++) {
            S1[i] = (byte) (0x00);
            S2[i] = (byte) (0x00);
        }
    }

    private static final IOUtils.Crypto C1 = new IOUtils.Crypto("kiwi", S1, 1); //$NON-NLS-1$
    private static final IOUtils.Crypto C2 = new IOUtils.Crypto("kiwi", S2, 1); //$NON-NLS-1$

    private static final ObjectCache<String, URL> url_cache = new ObjectCache<String, URL>(100);

    /**
     * Create a URL object from String.
     *
     * @param relativeString
     * @return URL
     */
    @CheckForNull
    public static URL createURL(String relativeString) {
        URL baseUrl = CardConfig.getDefaultWebUrl();
        if(relativeString.startsWith("http")) {
            baseUrl = null;
        }

        return createURL(relativeString, baseUrl);
    }

    /**
     * Create a URL object based on original URL.
     *
     * @param relativeString
     * @param base
     * @return null means malformed uri
     */
    @CheckForNull
    public static URL createURL(@CheckForNull String relativeString, @CheckForNull URL base) {
        try {
            if (relativeString == null)
                return base;

            URL url = url_cache.get(relativeString);

            if (url == null) {
                if (base == null)
                    url = new URL(relativeString);
                else
                    url = new URL(base, relativeString);
            }

            if (url != null)
                url_cache.put(relativeString, url);

            return url;
        } catch (Exception e) {
            LogUtils.e("Failed to createURI %s, %s", relativeString, base);
        }

        return null;
    }

    /**
     * A faster way to compare URLs
     *
     * @param u1
     * @param u2
     * @return boolean
     */
    public static boolean urlEquals(@CheckForNull URL u1, @CheckForNull URL u2) {
        if (u1 == u2)
            return true;
        if (u1 != null && u2 != null)
            return u1.toString().equals(u2.toString()); // URL.equals() is very
        // expensive
        return false;
    }

    /**
     * 判断两个网络请求的URL是否一样
     *
     * @param url1 url1
     * @param url2 url2
     * @return
     */
    public static boolean urlEquals(String url1, String url2) {
        if (LangUtils.isEmpty(url1) || LangUtils.isEmpty(url2)) {
            return false;
        }

        String tmp = "%s";
        if (!(url1.contains(tmp) || url2.contains(tmp))) {
            return url1.equals(url2);
        }

        boolean flag = true;
        String[] array1 = url1.split("/");
        String[] array2 = url1.split("/");
        if (array1.length == 0 || array2.length == 0 || array1.length != array2.length) {
            flag = false;
        } else {
            for (int i = 0; i < array1.length; i++) {
                if (!tmp.equals(array1[i]) && !tmp.equals(array2[i]) && !array1[i].equals(array2[i])) {
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }

    /**
     * Encode Uri Components to a String.
     *
     * @param s
     * @return String
     */
    public static String encodeUriComponent(String s) {
        return Uri.encode(s);
    }


    /**
     * This method never return null, use .length() == 0 to check if the ctx is empty
     *
     * @param json
     * @return
     */
    @NonNull
    public static JSONObject parseJsonObject(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json);
        } catch (JSONException e) {
            LogUtils.w(e, "Failed to parseJsonObject, %s", json);
            return null;
        }
    }

    public static JSONArray parseJsonArray(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseArray(json);
        } catch (JSONException e) {
            LogUtils.w(e, "Failed to parseJsonArray, %s", json);
            return null;
        }
    }


    /**
     * Get specific value from a JSON object.
     *
     * @param json
     * @param key
     * @return Object
     */
    public static Object getJsonValue(JSONObject json, String key) {
        return getJsonValue(json, key, null);
    }

    /**
     * Get specific value from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return Object
     */
    public static Object getJsonValue(@CheckForNull JSONObject json, String key, Object defaultValue) {
        if (json == null)
            return defaultValue;
        try {
            if (json.containsKey(key)) {
                Object value = json.get(key);
                return value == null ? defaultValue : value;
            }
        } catch (JSONException e) {
            LogUtils.e(e, "Failed to getJsonValue %s", key);
        }

        return defaultValue;
    }

    /**
     * Get Boolean from a JSON object.
     *
     * @param json
     * @param key
     * @return Boolean
     */
    @CheckForNull
    public static Boolean getJsonBoolean(JSONObject json, String key) {
        return getJsonBoolean(json, key, false);
    }

    /**
     * Get Boolean from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return Boolean
     */
    @CheckForNull
    public static Boolean getJsonBoolean(@CheckForNull JSONObject json, String key,
                                         Boolean defaultValue) {
        if (json == null)
            return defaultValue;
        try {
            if (json.containsKey(key)) {
                Boolean value = json.getBoolean(key);
                return value == null ? defaultValue : value;
            }
        } catch (Exception e) {
//      LogUtils.e(e, "Failed to getJsonBoolean %s", key);
        }

        return defaultValue;
    }


    /**
     * Get String from a JSON object.
     *
     * @param json
     * @param key
     * @return String
     */
    @CheckForNull
    public static String getJsonString(JSONObject json, String key) {
        return getJsonString(json, key, null);
    }

    /**
     * Get String from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return String
     */
    @CheckForNull
    public static String getJsonString(@CheckForNull JSONObject json, String key, String defaultValue) {
        if (json == null)
            return defaultValue;
        try {
            if (json.containsKey(key)) {
                String value = json.getString(key);
                return value == null || value.equals("null") ? defaultValue : value;
            }
        } catch (Exception e) {
//      LogUtils.e(e, "Failed to getJsonString %s", key);
        }

        return defaultValue;
    }

    /**
     * Get String from a JSONArray.
     *
     * @param array
     * @param index
     * @return String
     */
    public static String getJsonString(JSONArray array, int index) {
        return getJsonString(array, index, null);
    }

    /**
     * Get String from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return String
     */
    public static String getJsonString(@CheckForNull JSONArray array, int index, String defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            if (index < array.size())
                return array.getString(index);
        } catch (Exception e) {
//      LogUtils.e(e, "Failed to getJsonString %d, %d", array.length(), index);
        }

        return defaultValue;
    }

    /**
     *
     */
    public static JSONArray getJsonArray(@CheckForNull JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.getJSONArray(index);
        } catch (Exception e) {
            LogUtils.e(e, "Failed to getJsonString %d, %d", array.size(), index);
        }

        return null;
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @return int
     */
    public static int getJsonInt(JSONObject json, String key) {
        return getJsonInt(json, key, -1);
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return int
     */
    public static int getJsonInt(@CheckForNull JSONObject json, String key, int defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                int value = json.getIntValue(key);
                return value;
            } catch (Exception e) {
//        LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * get float from json
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return
     */
    public static double getJsonDouble(@CheckForNull JSONObject json, String key, double defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                double value = json.getDouble(key) == null ? defaultValue : json.getDouble(key);
                return value;
            } catch (Exception e) {
            }
        }

        return defaultValue;
    }

    /**
     * @param json
     * @param key
     * @param defaultValue
     * @return
     */
    public static float getJsonFloat(@CheckForNull JSONObject json, String key, float defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                float value = json.getFloat(key);
                return value;
            } catch (Exception e) {
//        LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     *
     * @return long
     */
    public static long getJsonLong(@CheckForNull JSONObject json, String key) {
        return getJsonLong(json, key, 0l);
    }

    /**
     * Get int from a JSON object.
     *
     * @param json
     * @param key
     * @param defaultValue
     * @return int
     */
    public static long getJsonLong(@CheckForNull JSONObject json, String key, Long defaultValue) {
        if (json == null)
            return defaultValue;
        if (json.containsKey(key)) {
            try {
                long value = json.getLong(key);
                return value;
            } catch (Exception e) {
//        LogUtils.e(e, "Failed to getJsonInt %s", key);
            }
        }

        return defaultValue;
    }

    /**
     * Get int from a JSONArray.
     *
     * @param array
     * @param index
     * @return int
     */
    public static int getJsonInt(JSONArray array, int index) {
        return getJsonInt(array, index, -1);
    }

    /**
     * Get int from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return int
     */
    public static int getJsonInt(@CheckForNull JSONArray array, int index, int defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            return array.getIntValue(index);
        } catch (Exception e) {
//      LogUtils.e(e, "Failed to get JsonInt %d, %d", array.length(), index);
        }

        return defaultValue;
    }

    /**
     * Get long from a JSONArray.
     *
     * @param array
     * @param index
     * @param defaultValue
     * @return long
     */
    public static long getJsonLong(@CheckForNull JSONArray array, int index, long defaultValue) {
        if (array == null)
            return defaultValue;
        try {
            return array.getLong(index);
        } catch (Exception e) {
//      LogUtils.e(e, "Failed to get JsonInt %d, %d", array.length(), index);
        }

        return defaultValue;
    }

    /**
     * Get a JSONObject from a JSONObject.
     *
     * @param json
     * @param key
     * @return JSONObject
     */
    @CheckForNull
    public static JSONObject getJsonObject(@CheckForNull JSONObject json, String key) {
        if (json == null)
            return null;
        try {
            if (json.containsKey(key))
                return json.getJSONObject(key);
        } catch (Exception e) {
            LogUtils.e("Failed to getJsonObject key = %s value = %s ", key, getJsonValue(json, key));
        }

        return null;
    }

    /**
     * Get a JSONArray from a JSONObject.
     *
     * @param json
     * @param key
     * @return JSONArray
     */
    @CheckForNull
    public static JSONArray getJsonArray(@CheckForNull JSONObject json, String key) {
        if (json == null)
            return null;
        try {
            if (json.containsKey(key))
                return json.getJSONArray(key);
        } catch (Exception e) {
            LogUtils.e(e, "Failed to getJsonArray %s", key);
        }

        return null;
    }

    @CheckForNull
    public static JSONArray getJsonArray(@CheckForNull String content) {
        if (content == null)
            return null;
        try {
            JSONArray array = JSON.parseArray(content);
            return array;
        } catch (Exception e) {
            LogUtils.e(e, "Failed to getJsonArray from %s", content);
        }

        return null;
    }

    /**
     * Get a JSONObject from a JSONArray.
     *
     * @param array
     * @param index
     * @return JSONObject
     */
    @CheckForNull
    public static JSONObject getJsonObject(@CheckForNull JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.getJSONObject(index);
        } catch (Exception e) {
            LogUtils.e("Failed to getJsonObject %d", index);
        }
        return null;
    }

    public static Object getArrayObject(@CheckForNull JSONArray array, int index) {
        if (array == null)
            return null;
        try {
            return array.get(index);
        } catch (Exception e) {
            LogUtils.e(e, "Failed to getJsonObject %d", index);
        }
        return null;
    }


    /**
     * Modify JSON data to a String.
     *
     * @param js
     * @return String
     */
    @NonNull
    public static String escapeJS(@CheckForNull CharSequence js) {
        if (LangUtils.isEmpty(js))
            return "";
        StringBuilder builder = acquireStringBuilder(js.length());
        for (int i = 0; i < js.length(); i++) {
            char c = js.charAt(i);
            if (c == '\'')
                builder.append("\\'");
            else if (c == '\r')
                builder.append("\\r");
            else if (c == '\n')
                builder.append("\\n");
            else if (c == '\b')
                builder.append("\\b");
            else if (c == '\t')
                builder.append("\\t");
            else if (c == '\f')
                builder.append("\\f");
            else if (c == '\\')
                builder.append("\\\\");
            else
                builder.append(c);
        }

        return releaseStringBuilder(builder);
    }

  /*
   * @CheckForNull public static URL rewriteURL(@CheckForNull URL url) {//deprecated if (url ==
   * null) return null; String absolute = url.toString(); if (absolute.contains("__m=")) return url;
   * return createURL(absolute.contains("?") ? absolute + "&" + PapayaConfig.DEFAULT_WEB_SUFFIX :
   * absolute + "?" + PapayaConfig.DEFAULT_WEB_SUFFIX, null); }
   */

    /**
     * Get content from URL.
     */
    @CheckForNull
    public static String contentFromURL(URL url) {
        return contentFromURL(url, true);
    }

    /**
     * Get content from URL to String.
     *
     * @param url
     * @param decode
     * @return String
     */
    @CheckForNull
    public static String contentFromURL(URL url, boolean decode) {
        String r = null;

        InputStreamReader reader = null;
        StringBuilder b = acquireStringBuilder(1024);
        try {
            reader = new InputStreamReader(inputStreamFromURL(url), "utf-8");

            char[] buf = new char[1024];
            int len = -1;
            while ((len = reader.read(buf)) != -1) {
                b.append(buf, 0, len);
            }

            r = releaseStringBuilder(b);

            if (decode) {
                synchronized (C2) {
                    r = C2.decrypt(r);
                }
            }
        } catch (Exception e) {
            LogUtils.e("failed to get content from url: %s", e);
        } finally {
            IOUtils.close(reader);
            releaseOnly(b);
        }

        // LogUtils.w("content from url %s, \n%s", url, r);

        return r;
    }

    /**
     * Convert URL content to input stream.
     *
     * @param url
     * @return InputStream
     */
    @CheckForNull
    public static InputStream inputStreamFromURL(@CheckForNull URL url) {
        if (url == null)
            return null;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(30 * 1000);
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return getInputStream(connection);
            } else
                LogUtils.w("inputstream error code %d", connection.getResponseCode());
        } catch (Exception e) {
            LogUtils.w("failed to open inputstream, %s", e);
        }

        return null;
    }

    /**
     * Get input stream of a connection.
     *
     * @param con
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getInputStream(@NonNull URLConnection con) throws Exception {
        InputStream ret = con.getInputStream();
        String encoding = con.getContentEncoding();
        if (encoding != null && encoding.toLowerCase(Locale.getDefault()).contains("gzip")) {
            ret = new GZIPInputStream(ret);
        } else if (encoding != null && encoding.toLowerCase(Locale.getDefault()).contains("deflate"))
            ret = new InflaterInputStream(ret);
        return ret;
    }

    /**
     * Modify Url's content to a String.
     *
     * @param url
     * @param params
     * @return String
     */
    @NonNull
    public static String compositeUrl(@NonNull String url, @CheckForNull Map<String, Object> params) {
        StringBuilder b = acquireStringBuilder(url.length()).append(url);
        if (params != null && !params.isEmpty()) {
            if (!url.contains("?"))
                b.append('?');
            else
                b.append('&');
            LogUtils.d("compositeUrl: url is %s", b.toString());
            int i = 0;
            Set<Entry<String, Object>> set = params.entrySet();
            for (Entry<String, Object> entry : set) {
                i++;
                b.append(entry.getKey()).append('=')
                        .append(entry.getValue() == null ? "" : Uri.encode(entry.getValue().toString()));
                if (i < set.size())
                    b.append('&');
            }
        }

        return releaseStringBuilder(b);
    }

    /**
     * Encrypt a message.
     *
     * @param s
     * @return String
     */
    @NonNull
    public static String encrypt(@CheckForNull String s) {
        if (s == null)
            s = "";
        synchronized (C1) {
            return C1.encrypt(s);
        }
    }

    /**
     * Decrypt a message to String object.
     *
     * @param s
     * @return
     */
    @NonNull
    public static String decrypt(@CheckForNull String s) {
        if (s == null)
            return "";
        synchronized (C2) {
            return C2.decrypt(s);
        }
    }


    /**
     * Get the index of a dialog's button. Help to decide UI's change.
     *
     * @param which
     * @return int
     */
    public static int dialogButtonToWebIndex(int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE)
            which = 0;
        else if (which == DialogInterface.BUTTON_NEUTRAL)
            which = 1;
        else
            which = 2;
        return which;
    }

    public static HashMap<String, Object> jsonToMap(JSONObject json) {
        HashMap<String, Object> ht = new HashMap<String, Object>();
        Iterator<?> keys = json.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                Object value = json.get(key);
                if (value.equals("null")) {
                    value = "";
                }
                if (value instanceof JSONArray) {
                    value = jsonToArrayString((JSONArray) value);
                }
                if (value instanceof JSONObject) {
                    value = jsonToMap((JSONObject) value);
                }
                ht.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return ht;

    }

    public static ArrayList<?> jsonToArrayString(JSONArray array) {
        if (array == null || array.size() <= 0) {
            return null;
        }

        try {
            ArrayList<Object> ret = new ArrayList<Object>(array.size());
            for (int i = 0; i < array.size(); i++) {
                Object v = array.get(i);
                if (v instanceof JSONArray) {
                    v = jsonToArrayString((JSONArray) v);
                } else if (v instanceof JSONObject) {
                    v = jsonToMap((JSONObject) v);
                }
                ret.add(v);
            }


            return ret;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }


    /**
     * Convert Java Object to JSON object.
     *
     * @param o
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    public static Object java2jsonValue(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof List) {
            JSONArray a = new JSONArray();
            for (Object t : (List) o) {
                a.add(java2jsonValue(t));
            }
            return a;
        } else if (o instanceof Map) {
            JSONObject a = new JSONObject();
            Map m = (Map) o;
            for (Object k : m.keySet())
                try {
                    a.put((String) k, java2jsonValue(m.get(k)));
                } catch (Exception e) {
                    e(e, "Failed to convert javamap to json");
                }
            return a;
        } else if (o instanceof byte[])
            return LangUtils.utf8String((byte[]) o, "");
        else
            return o;
    }

    /**
     * Clear URL cache.
     */
    public static void clear() {
        url_cache.clear();
    }

    /**
     * Copy from SDK trunk Get and Modify content from POST URL String.
     *
     * @param url
     * @param data
     * @param timeout
     * @return byte[]
     */
    public static byte[] contentFromPOSTString(URL url, byte[] data, int timeout) {

        URLConnection conn = null;
        try {
            conn = url.openConnection();

            if (conn instanceof HttpURLConnection)
                ((HttpURLConnection) conn).setRequestMethod("POST"); //$NON-NLS-1$
            conn.setRequestProperty("Accept-Encoding", "gzip");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            conn.setRequestProperty("Connection", "Keep-Alive"); //$NON-NLS-1$ //$NON-NLS-2$
            conn.setRequestProperty("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
            if (timeout != 0)
                conn.setConnectTimeout(timeout);
            else
                conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(60 * 1000);
            OutputStream output = conn.getOutputStream();
            output.write(data);
            output.close();
        } catch (IOException e1) {
            LogUtils.w("Failed to post  url: %s, data: %s, ex: %s", url.toString(),
                    LangUtils.utf8_decode(data), e1.toString());
            return null;
        }

        InputStream input = null;
        ByteArrayOutputStream output = null;
        byte[] buf = LangUtils.acquireBytes(1024);
        int len = -1;
        try {
            input = getInputStream(conn);
            output =
                    new ByteArrayOutputStream(conn.getContentLength() < 1 ? 1024 : conn.getContentLength());
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            return output.toByteArray();
        } catch (Exception e) {
            if (LogUtils.DW)
                LogUtils.dw(e, "Failed to get data from %s", url); //$NON-NLS-1$
            return null;
        } finally {
            IOUtils.close(input);
            IOUtils.close(output);
            LangUtils.releaseBytes(buf);
        }
    }

    // /**
    // * Get and modify content from POST dictionary.
    // *
    // * @param url
    // * @param params
    // * @param timeout
    // * @return byte[]
    // */
    // public static byte[] contentFromPOSTDict(URL url, Map<String, String> params, int timeout) {
    // HttpURLConnection conn = null;
    // try {
    // String boundary = MultiPartFormOutputStream.createBoundary();
    // conn = (HttpURLConnection) MultiPartFormOutputStream.createConnection(url);
    //			conn.setRequestProperty("Accept", "*/*"); //$NON-NLS-1$ //$NON-NLS-2$
    // conn.setRequestProperty("Accept-Encoding", "gzip");
    //			conn.setRequestProperty("Content-Type", MultiPartFormOutputStream.getContentType(boundary)); //$NON-NLS-1$
    // // set some other request headers...
    //			conn.setRequestProperty("Connection", "Keep-Alive"); //$NON-NLS-1$ //$NON-NLS-2$
    //			conn.setRequestProperty("Cache-Control", "no-cache"); //$NON-NLS-1$ //$NON-NLS-2$
    // // no need to connect cuz getOutputStream() does it
    //
    // if (timeout != 0)
    // conn.setConnectTimeout(timeout);
    // else
    // conn.setConnectTimeout(60 * 1000);
    // conn.setReadTimeout(60 * 1000);
    // MultiPartFormOutputStream out = new MultiPartFormOutputStream(conn.getOutputStream(),
    // boundary);
    // if (params != null) {
    // for (Entry<String, String> entry : params.entrySet())
    // out.writeField(entry.getKey(), entry.getValue());
    // }
    //
    // out.close();
    //
    // if (conn.getResponseCode() != 200) {
    //				LogUtils.e("Failed to open connection, status:%d", conn.getResponseCode()); //$NON-NLS-1$
    // return null;
    // }
    // } catch (Exception e) {
    //			LogUtils.e("Failed to write data into output: %s", e); //$NON-NLS-1$
    // return null;
    // }
    //
    // InputStream input = null;
    // ByteArrayOutputStream output = null;
    // byte[] buf = LangUtils.acquireBytes(1024);
    // int len = -1;
    // try {
    // input = getInputStream(conn);
    // output = new ByteArrayOutputStream(conn.getContentLength() < 1 ? 1024 :
    // conn.getContentLength());
    // while ((len = input.read(buf)) != -1) {
    // output.write(buf, 0, len);
    // }
    // return output.toByteArray();
    // } catch (Exception e) {
    // if (LogUtils.DW)
    //				LogUtils.dw(e, "Failed to get data from %s", url); //$NON-NLS-1$
    // return null;
    // } finally {
    // IOUtils.close(input);
    // IOUtils.close(output);
    // LangUtils.releaseBytes(buf);
    // }
    // }

    /**
     * Add signature headers to a URL connection.
     *
     * @param con
     */
    public static void addHeaders(URLConnection con) {
        if (con != null) {
        }
    }

    /**
     * remove papaya_cache_file header
     *
     * @param content
     * @return
     */
    public static String removeCacheHeader(String content) {
        if (isEmpty(content)) {
            return "";
        }
        String header1 = "papaya_cache_file://../";
        String header2 = "papaya_cache_file://";
        if (content.contains(header1) || content.contains(header2)) {
            return content.replaceAll(header1, "").replaceAll(header2, "");
        }
        return content;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean checkEmail(String email) {
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = p.matcher(email);
        return matcher.matches();
    }

    public static boolean checkUserId(String name) {
        Pattern p = Pattern.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]*");
        Matcher matcher = p.matcher(name);
        return matcher.matches();
    }

    public static String getNicknameFromEmail(String email) {
        int index = email.indexOf("@");
        if (index >= 0) {
            return email.substring(0, email.indexOf("@"));
        } else {
            return email;
        }
    }

    /**
     * @param token a randomed String as token
     * @param pwd   original password
     * @return
     */

    public static String formatPassword(String token, String pwd) {
        String hashedPwd = sha256(pwd);
        String finishePwd = sha256(format("%s%s", token, hashedPwd));
        return finishePwd;
    }

    public static String decodeFromHtml(String url) {
        url =
                url.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                        .replaceAll("&apos;", "\'").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ")
                        .replaceAll("&copy;", "@").replaceAll("&reg;", "?");
        return url;
    }

    public static String encodeFromHtml(String url) {
        url =
                url.replaceAll("&;", "&amp").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                        .replaceAll("\'", "&apos;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;")
                        .replaceAll("@", "&copy;").replaceAll("?", "&reg;");
        return url;
    }

    /**
     * @param date
     * @return time stamp by second
     */
    public static long parseSecondStamp(Date date) {
        if (date == null) {
            return 0;
        }
        return (date.getTime() / 1000);
    }

    /**
     * Check whether network connected
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(e.toString());
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    // public static boolean testHttp(String url) {
    // boolean bRes = false;
    // HttpClient client = new DefaultHttpClient();
    //
    // try {
    // HttpGet request = new HttpGet(new URI(url));
    // client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
    // client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
    //
    // HttpResponse response = client.execute(request);
    //
    // int nCode = response.getStatusLine().getStatusCode();
    //
    // if (nCode == HttpStatus.SC_OK){ //HttpStatus.SC_OK 200
    // bRes = true;
    // HttpEntity entity = response.getEntity();
    // if (entity != null) {
    // }
    // }
    // } catch (URISyntaxException e) {
    // bRes = false;
    // e.printStackTrace();
    // } catch (ClientProtocolException e) {
    // bRes = false;
    // e.printStackTrace();
    // } catch (IOException e) {
    // bRes = false;
    // e.printStackTrace();
    // }
    // return bRes;
    // }
}
