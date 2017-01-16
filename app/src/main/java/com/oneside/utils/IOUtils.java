package com.oneside.utils;

import static com.oneside.utils.LangUtils.*;
import static com.oneside.utils.LogUtils.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;

import com.alibaba.fastjson.JSONArray;
import com.oneside.manager.CardManager;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Class to help handle IO processes.
 *
 * @author Bo Hu
 */
public class IOUtils {
    private static final String KIWI = "kiwi";
    public static final byte[] EMPTY_BYTES = new byte[0];

    private IOUtils() {
    }

    public static class Coder {

        // Mapping table from 6-bit nibbles to Base64 characters.
        @NonNull
        private static char[] map1 = new char[64];

        static {
            int i = 0;
            for (char c = 'A'; c <= 'Z'; c++)
                map1[i++] = c;
            for (char c = 'a'; c <= 'z'; c++)
                map1[i++] = c;
            for (char c = '0'; c <= '9'; c++)
                map1[i++] = c;
            map1[i++] = '+';
            map1[i++] = '/';
        }

        // Mapping table from Base64 characters to 6-bit nibbles.
        @NonNull
        private static byte[] map2 = new byte[128];

        static {
            for (int i = 0; i < map2.length; i++)
                map2[i] = -1;
            for (int i = 0; i < 64; i++)
                map2[map1[i]] = (byte) i;
        }

        @NonNull
        public static String encode(@NonNull byte[] in) {
            return new String(encode(in, 0, in.length));
        }

        @NonNull
        public static char[] encode(byte[] in, int iOff, int iLen) {
            int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
            int oLen = ((iLen + 2) / 3) * 4; // output length including padding
            char[] out = new char[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;
            int op = 0;
            while (ip < iEnd) {
                int i0 = in[ip++] & 0xff;
                int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
                int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
                int o0 = i0 >>> 2;
                int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
                int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
                int o3 = i2 & 0x3F;
                out[op++] = map1[o0];
                out[op++] = map1[o1];
                out[op] = op < oDataLen ? map1[o2] : '=';
                op++;
                out[op] = op < oDataLen ? map1[o3] : '=';
                op++;
            }
            return out;
        }

        @NonNull
        public static String decodeString(@NonNull String s) {
            return new String(decode(s));
        }

        @NonNull
        public static byte[] decode(@NonNull String s) {
            return decode(s.toCharArray(), 0, s.length());
        }

        @NonNull
        public static byte[] decode(char[] in, int iOff, int iLen) {
            if (iLen % 4 != 0)
                throw new IllegalArgumentException(
                        "Length of Base64 encoded input string is not a multiple of 4.");
            while (iLen > 0 && in[iOff + iLen - 1] == '=')
                iLen--;
            int oLen = (iLen * 3) / 4;
            byte[] out = new byte[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;
            int op = 0;
            while (ip < iEnd) {
                int i0 = in[ip++];
                int i1 = in[ip++];
                int i2 = ip < iEnd ? in[ip++] : 'A';
                int i3 = ip < iEnd ? in[ip++] : 'A';
                if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                int b0 = map2[i0];
                int b1 = map2[i1];
                int b2 = map2[i2];
                int b3 = map2[i3];
                if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                int o0 = (b0 << 2) | (b1 >>> 4);
                int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
                int o2 = ((b2 & 3) << 6) | b3;
                out[op++] = (byte) o0;
                if (op < oLen)
                    out[op++] = (byte) o1;
                if (op < oLen)
                    out[op++] = (byte) o2;
            }
            return out;
        }

        private Coder() {
        }
    }

    /**
     * Class to handle conversion between different types of data.
     *
     * @author Bo Hu
     */
    public static class Base64 {

        // Mapping table from 6-bit nibbles to Base64 characters.
        @NonNull
        private static char[] map1 = new char[64];

        static {
            int i = 0;
            for (char c = 'A'; c <= 'Z'; c++)
                map1[i++] = c;
            for (char c = 'a'; c <= 'z'; c++)
                map1[i++] = c;
            for (char c = '0'; c <= '9'; c++)
                map1[i++] = c;
            map1[i++] = '+';
            map1[i++] = '/';
        }

        // Mapping table from Base64 characters to 6-bit nibbles.
        @NonNull
        private static byte[] map2 = new byte[128];

        static {
            for (int i = 0; i < map2.length; i++)
                map2[i] = -1;
            for (int i = 0; i < 64; i++)
                map2[map1[i]] = (byte) i;
        }

        /**
         * Encode an byte[] object to a String object.
         *
         * @param in
         * @return String
         */
        @NonNull
        public static String encode(@NonNull byte[] in) {
            return new String(encode(in, 0, in.length));
        }

        /**
         * Encode part of a byte[] object to a String object.
         *
         * @param in
         * @param iOff
         * @param iLen
         * @return char[]
         */
        @NonNull
        public static char[] encode(byte[] in, int iOff, int iLen) {
            int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
            int oLen = ((iLen + 2) / 3) * 4; // output length including padding
            char[] out = new char[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;
            int op = 0;
            while (ip < iEnd) {
                int i0 = in[ip++] & 0xff;
                int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
                int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
                int o0 = i0 >>> 2;
                int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
                int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
                int o3 = i2 & 0x3F;
                out[op++] = map1[o0];
                out[op++] = map1[o1];
                out[op] = op < oDataLen ? map1[o2] : '=';
                op++;
                out[op] = op < oDataLen ? map1[o3] : '=';
                op++;
            }
            return out;
        }

        /**
         * Decode a String object.
         *
         * @param s
         * @return String
         */
        @NonNull
        public static String decodeString(@NonNull String s) {
            return new String(decode(s));
        }

        /**
         * Decode a String object to a byte[] object.
         *
         * @param s
         * @return byte[]
         */
        @NonNull
        public static byte[] decode(@NonNull String s) {
            return decode(s.toCharArray(), 0, s.length());
        }

        /**
         * Encode part of a char[] object to a byte[] object
         *
         * @param in
         * @param iOff
         * @param iLen
         * @return byte[]
         */
        @NonNull
        public static byte[] decode(char[] in, int iOff, int iLen) {
            if (iLen % 4 != 0)
                throw new IllegalArgumentException(
                        "Length of Base64 encoded input string is not a multiple of 4.");
            while (iLen > 0 && in[iOff + iLen - 1] == '=')
                iLen--;
            int oLen = (iLen * 3) / 4;
            byte[] out = new byte[oLen];
            int ip = iOff;
            int iEnd = iOff + iLen;
            int op = 0;
            while (ip < iEnd) {
                int i0 = in[ip++];
                int i1 = in[ip++];
                int i2 = ip < iEnd ? in[ip++] : 'A';
                int i3 = ip < iEnd ? in[ip++] : 'A';
                if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                int b0 = map2[i0];
                int b1 = map2[i1];
                int b2 = map2[i2];
                int b3 = map2[i3];
                if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
                    throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
                int o0 = (b0 << 2) | (b1 >>> 4);
                int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
                int o2 = ((b2 & 3) << 6) | b3;
                out[op++] = (byte) o0;
                if (op < oLen)
                    out[op++] = (byte) o1;
                if (op < oLen)
                    out[op++] = (byte) o2;
            }
            return out;
        }

        private Base64() {
        }
    }

    /**
     * Class to handle Encryption and Decryption.
     *
     * @author do
     */
    public static class Crypto {
        private Key skey;
        private PBEParameterSpec spec;
        private Cipher ciph;

        /**
         * Creates a new Crypto instance that providing Security utilities.
         *
         * @param key
         * @param salt
         * @param iter
         */
        public Crypto(@NonNull String key, byte[] salt, int iter) {
            try {
                this.skey =
                        SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(
                                new PBEKeySpec(key.toCharArray()));
                spec = new PBEParameterSpec(salt, iter);
                ciph = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
                ciph = Cipher.getInstance("PBEWithMD5AndDES");
            } catch (Exception e) {
                // LogUtils.e(e, "failed to init");
            }
        }

        /**
         * Encrypt a message.
         *
         * @param msg
         * @return String
         */
        @NonNull
        public String encrypt(@NonNull String msg) {
            try {
                return encrypt(msg.getBytes("UTF-8"));
            } catch (Exception e) {
                LogUtils.e(e, "Failed to encrypt");
            }
            return msg;
        }

        /**
         * Encrypt a message to a String object.
         *
         * @param bytes
         * @return String
         */
        @NonNull
        public String encrypt(byte[] bytes) {
            try {
                ciph.init(Cipher.ENCRYPT_MODE, skey, spec);
                byte[] doFinal = ciph.doFinal(bytes);
                return Base64.encode(doFinal);
            } catch (Exception e) {
                // LogUtils.e(e, "failed to encrypt %s", msg);
            }

            return new String(bytes);
        }

        /**
         * Decrypt a message to String object.
         *
         * @param secret
         * @return String
         */
        @NonNull
        public String decrypt(@NonNull String secret) {
            try {
                ciph.init(Cipher.DECRYPT_MODE, skey, spec);
                return new String(ciph.doFinal(Base64.decode(secret)), "UTF-8");
            } catch (Exception e) {
                // LogUtils.e("error %s, failed to decrypt '%s'",
                // e.getMessage(), secret);
            }

            return secret;
        }
    }

    /**
     * Check if a file is already existed.
     *
     * @param file
     * @return boolean
     */
    public static boolean exist(@CheckForNull File file) {
        return file != null && file.exists();
    }

    /**
     * Delete a specific file.
     *
     * @param file
     * @return boolean
     */
    public static boolean deleteFile(@CheckForNull File file) {
        if (file == null)
            return true;
        if (file.isFile() && file.exists())
            return file.delete();
        return !file.exists();
    }

    /**
     * Delete a specific file or directory.
     *
     * @param file
     * @return boolean
     */
    public static boolean deleteFileOrDir(@NonNull File file) {
        if (!exist(file))
            return true;
        if (file.isFile())
            return file.delete();
        else if (file.isDirectory()) {
            File[] subs = file.listFiles();
            if (subs.length == 0)
                return file.delete();
            else {
                boolean result = true;
                for (File sub : subs)
                    result = result && deleteFileOrDir(sub);
                return result && file.delete();
            }
        } else {
            w("unknown file type: %s", file);
            return false;
        }
    }

    /**
     * Clear a specific directory.
     *
     * @param dir
     */
    public static void clearDir(@NonNull File dir) {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (D)
                d("clear %d files in %s", files.length, dir);
            for (File sub : files)
                deleteFileOrDir(sub);
        }
    }

    /**
     * Close an IO channel.
     *
     * @param io
     * @return boolean
     */
    public static boolean close(@CheckForNull Closeable io) {
        if (io != null)
            try {
                io.close();
                return true;
            } catch (IOException e) {
                e(e, "Failed to close IO %s", io);
                return false;
            }

        return true;
    }

    /**
     * Close a ParcelFileDescriptor.
     *
     * @param descriptor
     * @return boolean
     */
    public static boolean close(@CheckForNull ParcelFileDescriptor descriptor) {
        if (descriptor != null)
            try {
                descriptor.close();
                return true;
            } catch (IOException e) {
                w(e, "Failed to close descriptor");
                return false;
            }
        else
            return true;
    }

    public static boolean mkdirs(File file) {
        if (file == null)
            return false;
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs())
                return false;
        }
        return true;
    }

    /**
     * Write byte[] data to a file.
     *
     * @param file
     * @param data
     * @return boolean
     */
    public static boolean writeBytesToFile(@CheckForNull File file, @CheckForNull byte[] data) {
        if (file == null || data == null)
            return false;
        mkdirs(file);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(data);
            return true;
        } catch (IOException e) {
            e(e, "Failed to write bytes %s", file);
            return false;
        } finally {
            close(outputStream);
        }
    }

    /**
     * Write streams data to a file.
     *
     * @param file
     * @param input
     * @return
     */
    public static boolean writeStreamToFile(File file, @NonNull InputStream input) {
        if (file == null || input == null)
            return false;
        mkdirs(file);

        OutputStream outputStream = null;
        byte[] buf = acquireBytes(1024);
        try {

            int len = -1;
            outputStream = new FileOutputStream(file);
            while ((len = input.read(buf)) != -1)
                outputStream.write(buf, 0, len);
            return true;
        } catch (IOException e) {
            e(e, "Failed to write stream %s", file);
            return false;
        } finally {
            close(outputStream);
            close(input);
            releaseBytes(buf);
        }
    }

    /**
     * Create byte[] data from a file.
     *
     * @param file
     * @return byte[]
     */
    @CheckForNull
    public static byte[] dataFromFile(@CheckForNull File file) {
        if (file == null || !file.exists())
            return null;
        try {
            return dataFromStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            w(e, "Failed to load dataFromFile %s", file);
        }

        return null;
    }

    /**
     * Create String data from a input stream.
     *
     * @param input
     * @return String
     */
    @NonNull
    public static String stringFromStream(@NonNull InputStream input) {
        StringBuilder builder = LangUtils.acquireStringBuilder(0);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            char[] buf = new char[1024];
            int len = -1;
            while ((len = reader.read(buf)) != -1) {
                builder.append(buf, 0, len);
            }
        } catch (IOException e) {
            w(e, "Failed to load stringFromStream");
        } finally {
            close(reader);
        }

        return LangUtils.releaseStringBuilder(builder);
    }

    /**
     * Convert input stream data to byte[] data.
     *
     * @param input
     * @return byte[]
     */
    public static byte[] dataFromStream(@CheckForNull InputStream input) {
        if (input == null)
            return EMPTY_BYTES;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = acquireBytes(1024);
        int len = -1;
        try {
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            return output.toByteArray();
        } catch (IOException e) {
            w(e, "Failed to load dataFromStream");
        } finally {
            close(input);
            releaseBytes(buf);
        }
        return EMPTY_BYTES;
    }

    /**
     * Create a collection of String from input stream.
     *
     * @param input
     * @param container
     * @return Collection<String>
     */
    @CheckForNull
    public static Collection<String> linesFromStream(@CheckForNull InputStream input,
                                                     @CheckForNull Collection<String> container) {
        if (container == null)
            container = new ArrayList<String>();
        if (input == null)
            return container;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null)
                container.add(line.trim());
        } catch (Exception e) {
            w(e, "Failed to load linesFromStream");
        } finally {
            close(reader);
        }

        return container;
    }

    /**
     * Create a map collection of String from input stream.
     *
     * @param input
     * @param container
     * @return Map<String, String>
     */
    public static Map<String, String> mapFromStream(InputStream input, Map<String, String> container) {
        if (container == null)
            container = new HashMap<String, String>();
        if (input == null)
            return container;

        BufferedReader r = null;
        String line = null;
        try {
            r = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) {
                    int index = line.indexOf('=');
                    if (index != -1) {
                        container.put(line.substring(0, index), line.substring(index + 1));
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.w("Faied to load string resources: " + e);
        } finally {
            IOUtils.close(r);
        }

        return container;
    }

    /**
     * Write data from ByteBuffer to file.
     *
     * @param file
     * @param buffer
     * @return boolean
     */
    public static boolean writeBufferToFile(@NonNull File file, @NonNull ByteBuffer buffer) {
        if (file == null)
            return false;
        mkdirs(file);
        FileChannel channel = null;
        try {
            channel = new FileOutputStream(file).getChannel();
            channel.write(buffer);
            return true;
        } catch (IOException e) {
            e(e, "Failed to write buffer %s", file);
            return false;
        } finally {
            close(channel);
        }
    }

    /**
     * Decode data to a String object.
     *
     * @param data
     * @param encoding
     * @return String
     */
    @NonNull
    public static String decodeData(@CheckForNull byte[] data, String encoding) {
        if (data == null)
            return "";
        try {
            return new String(data, encoding);
        } catch (UnsupportedEncodingException e) {
            w(e, "failed to decode data %s", encoding);
            return new String(data);
        }
    }

    /**
     * Calculate the MD5 code of specific content.
     *
     * @param content
     * @return String
     */

    public static String md5(@CheckForNull String content) {
        return md5(LangUtils.getBytes(content));
    }

    @NonNull
    public static String md5(byte[] defaultBytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() > 1)
                    hexString.append(hex);
                else
                    hexString.append('0').append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.w(e, "Failed to compute md5"); //$NON-NLS-1$
        }

        return null;
    }

    /**
     * Get a AssetFileDescriptor of a file.
     *
     * @param manager
     * @param path
     * @return AssetFileDescriptor
     */
    @CheckForNull
    public static AssetFileDescriptor getAssetFileDescriptor(@NonNull AssetManager manager,
                                                             @CheckForNull String path) {
        if (path == null)
            return null;
        try {
            AssetFileDescriptor ret = manager.openFd(path);
            if (ret == null)
                w("%s is null??", path);
            return ret;
        } catch (Exception e) {
            w(e, "%s", path);
        }

        return null;
    }

    /**
     * Get a ParcelFileDescriptor of a file.
     *
     * @param file
     * @param mode
     * @return ParcelFileDescriptor
     */
    @CheckForNull
    public static ParcelFileDescriptor getParcelFileDescriptor(@CheckForNull File file, int mode) {
        if (file == null || !file.exists())
            return null;
        try {
            return ParcelFileDescriptor.open(file, mode);
        } catch (Exception e) {
            e(e, "Failed to getParcelFileDescriptor");
        }

        return null;
    }

    /**
     * Compress input stream to output stream.
     *
     * @param input
     * @param o
     * @return boolean
     */
    public static boolean compressZlib(@CheckForNull InputStream input, @CheckForNull OutputStream o) {
        if (input == null || o == null)
            return false;
        byte[] buf = acquireBytes(1024);
        int len = -1;
        boolean succ = true;
        DeflaterOutputStream output = new DeflaterOutputStream(o);
        try {
            while ((len = input.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (Exception e) {
            e(e, "failed to compress");
            succ = false;
        } finally {
            close(input);
            close(output);
            releaseBytes(buf);
        }

        return succ;
    }

    /**
     * Compress byte[] data.
     *
     * @param data
     * @return byte[]
     */
    public static byte[] compressZlib(@CheckForNull byte[] data) {
        byte[] ret = null;
        if (data != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            if (compressZlib(new ByteArrayInputStream(data), output))
                ret = output.toByteArray();
        }
        if (D) {
            if (ret != null)
                d("compress ratio: %f, %d/%d", (ret.length + 0.0) / data.length, ret.length, data.length);
        }

        return ret;
    }

    /**
     * Decompress input stream.
     *
     * @param input
     * @return byte[]
     */
    @CheckForNull
    public static byte[] decompressZlib(InputStream input) {
        InputStream zipInputStream = new InflaterInputStream(input);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buf = acquireBytes(1024);
        int len = -1;
        boolean fail = false;
        try {
            while ((len = zipInputStream.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
        } catch (IOException e) {
            e(e, "failed to decompress");
            fail = true;
        } finally {
            close(zipInputStream);
            close(output);
            releaseBytes(buf);
        }

        return fail ? null : output.toByteArray();
    }

    /**
     * Decompress from input stream to output stream.
     *
     * @param input
     * @param output
     * @return boolean
     */
    public static boolean decompressGZip(@NonNull InputStream input, @NonNull OutputStream output) {
        GZIPInputStream in = null;
        byte[] buf = acquireBytes(1024);
        boolean ret = false;
        try {
            in = new GZIPInputStream(input);

            int len = -1;
            while ((len = in.read(buf)) != -1)
                output.write(buf, 0, len);

            ret = true;
        } catch (Exception e) {
            w(e, "Failed to decompressGZip");
        } finally {
            IOUtils.close(in);
            IOUtils.close(output);
            releaseBytes(buf);
        }

        return ret;
    }

    /**
     * Decompress from input stream to a file.
     *
     * @param input
     * @param output
     * @return boolean
     */
    public static boolean decompressGZipToFile(@CheckForNull InputStream input,
                                               @CheckForNull File output) {
        if (input == null || output == null)
            return false;
        File parentFile = output.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();
        try {
            return decompressGZip(input, new FileOutputStream(output));
        } catch (FileNotFoundException e) {
            e("Failed to decompressGZipToFile, %s", output);
        }

        return false;
    }

    /**
     * Save a bitmap to a file.
     *
     * @param bm
     * @param localFile
     * @param format
     * @param quality
     */
    public static void saveBitmap(@NonNull Bitmap bm, @NonNull File localFile,
                                  Bitmap.CompressFormat format, int quality) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(localFile);
            bm.compress(format, quality, output);
            output.flush();
        } catch (Exception e) {
            w(e, "Failed to save bitmap");
        } finally {
            close(output);
        }
    }


    /**
     * Guo Ming
     *
     * @param map
     * @return the string of map
     */
    public static String outFromMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }

    @NonNull
    public static String sha256(@CheckForNull String content) {
        if (content == null)
            content = "";
        try {
            byte[] defaultBytes = content.getBytes();

            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuilder hexString = acquireStringBuilder(content.length());
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() > 1)
                    hexString.append(hex);
                else
                    hexString.append('0').append(hex);
            }
            return releaseStringBuilder(hexString);
        } catch (NoSuchAlgorithmException e) {
            w(e, "Failed to compute sha256");
        }

        return "";
    }

    public static void savePreferenceValue(String key, String value) {
        SharedPreferences p =
                CardManager.getApplicationContext().getSharedPreferences(KIWI, Context.MODE_PRIVATE);
        Editor editor = p.edit();
        editor.putString(key, value);
        editor.apply();
        // return editor.commit();
    }

    public static String getPreferenceValue(String key) {
        SharedPreferences p =
                CardManager.getApplicationContext().getSharedPreferences(KIWI, Context.MODE_PRIVATE);
        return p.getString(key, "");
    }

    public static void removePreferenceValue(String key) {
        SharedPreferences p = CardManager.getApplicationContext().getSharedPreferences(KIWI, Context.MODE_PRIVATE);
        Editor editor = p.edit();
        editor.remove(key);
        editor.apply();
        // return editor.commit();
    }

    public static void clearPreference(ArrayList<String> keepKeys) {
        if(LangUtils.isEmpty(keepKeys)) {
            return;
        }

        SharedPreferences p = CardManager.getApplicationContext().getSharedPreferences(KIWI, Context.MODE_PRIVATE);
        Editor e = p.edit();
        for(String key: keepKeys) {
            if(!LangUtils.isEmpty(key)) {
                e.remove(key);
            }
        }

        // e.commit();
        e.apply();
    }

    public static final String[] s_uid2445_char_table = {"a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0",
            "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static final int PPY_UID2445_LENGTH = 26;

    /**
     * generate uid 2445 of calendar
     *
     * @return
     */
    public static String generateUID2445() {
        StringBuilder result = new StringBuilder(PPY_UID2445_LENGTH);
        int table_length = s_uid2445_char_table.length;// remove last two char
        Random random = new Random();
        for (int i = 0; i < PPY_UID2445_LENGTH; i++) {
            result.append(format("%s", s_uid2445_char_table[Math.abs(random.nextInt() % table_length)]));
            // [result appendFormat:@"%c", s_uid2445_char_table[arc4random() %
            // table_length]];
        }
        return result.toString();
    }

    public static Bitmap getBitmap(byte[] data, int wid, int hei) {
        SoftReference<Bitmap> bm = null;
        if (data != null && data.length > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            d("bitmap real width %s , height %s ,data is %s KB", options.outWidth, options.outHeight,
                    data.length / 1024f);
            int width = 350;
            int height = 350;
            if (wid > 0 && hei > 0) {
                width = wid;
                height = hei;
            }
            final int minSideLength = Math.min(width, height);
            options.inSampleSize = IOUtils.computeSampleSize(options, minSideLength, width * height);
            options.inJustDecodeBounds = false;
            options.inInputShareable = true;
            options.inPurgeable = true;
            bm = new SoftReference<Bitmap>(BitmapFactory.decodeByteArray(data, 0, data.length, options));
            d("bitmap...width %s height %s", bm.get().getWidth(), bm.get().getHeight());
            return bm.get();

        } else {
            return null;
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound =
                (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static ArrayList<?> parseSqlArray(Object o) {
        if (o instanceof ArrayList) {
            return (ArrayList<?>) o;
        } else if (o instanceof byte[]) {
            return deserializeArrayList((byte[]) o);
        } else if (o instanceof JSONArray) {
            return WebUtils.jsonToArrayString((JSONArray) o);
        } else {
            return null;
        }
    }

    public static byte[] serialize(Object o) {
        try {
            ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(mem_out);

            out.writeObject(o);

            out.close();
            mem_out.close();

            byte[] bytes = mem_out.toByteArray();
            return bytes;
        } catch (IOException e) {
            return null;
        }
    }

    public static Object deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(mem_in);

            Object o = in.readObject();

            in.close();
            mem_in.close();

            return o;
        } catch (StreamCorruptedException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] serializeHashMap(HashMap<String, Object> hashMap) {
        return serialize(hashMap);
    }

    public static ArrayList<?> deserializeArrayList(byte[] bytes) {
        return (ArrayList<?>) deserialize(bytes);
    }

}
