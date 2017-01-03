package com.oneside.manager;

import static com.oneside.utils.IOUtils.*;
import static com.oneside.utils.LogUtils.*;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.oneside.utils.SysUtils;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Created by Guo Ming on 15-3-31.
 */
public class CardCacheManager {

    private static CardCacheManager mManager;

//  private CacheManager(String cacheDirName, Context context) {
//    this.cacheDirName = cacheDirName;
//    this.context = context;
//    initCache();
//  }

    private CardCacheManager() {
        cacheDirName = "ppy_card_new";
        context = CardManager.getApplicationContext();
        initCache();

        try {
            File oldFile = new File(Environment.getExternalStorageDirectory(), "ppy_card");
            deleteOldFile(oldFile);
        }catch (Exception e){

        }

    }

    private void deleteOldFile(File oldFile) {
        if (oldFile == null || !oldFile.exists()) {
            return;
        }

        if (oldFile.isDirectory()) {
            File[] childFile = oldFile.listFiles();
            if (childFile == null || childFile.length == 0) {
                oldFile.delete();
                return;
            }
            for (File f : childFile) {
                deleteOldFile(f);
            }
            oldFile.delete();
        }

    }

    public static CardCacheManager getManager() {
        if (mManager == null) {
            mManager = new CardCacheManager();
        }
        return mManager;
    }

    /**
     * The cache dir's name.
     */
    protected String cacheDirName = "ppy_card_new";


    /**
     * The cache dir.
     */
    protected File dir;

    /**
     * context.
     */
    protected Context context;


    // protected StorageCache(String cacheDirName, Context context) {
    // this.cacheDirName = cacheDirName;
    // this.context = context;
    // }

//  public static void initCache(String cacheDirName, Context context) {
//    if (mManager == null)
//      mManager = new CacheManager(cacheDirName, context);
//  }

    public Context getContext() {
        return context;
    }

    public String getCacheDirName() {
        return cacheDirName;
    }

    @NonNull
    public File getCacheDir() {
        return this.dir;
    }

    public boolean initCache() {
        prepareCacheDir();
        return exist(dir);
    }

    public boolean clearCache() {
        clearDir(dir);
        return true;
    }

    /**
     * Save a byte array into a cache file with the key.
     *
     * @param key
     * @param data
     * @return
     */
    public boolean saveBytesWithKey(String key, byte[] data) {
        File file = getCacheFile(key);
        mkdirs(file);
        return writeBytesToFile(file, data);
    }

    private File getKeyFile(String md5) {
        String subKey = md5.substring(0, 2);
        File subDir = new File(dir, subKey);
        File newFile = new File(subDir, md5);


        return newFile;
    }

    public File getCacheFile(String key) {
        String md5 = keyToStoreName(key);
        return getKeyFile(md5);
    }

    public File getRelativeFile(String path) {
        return new File(dir.getAbsolutePath() + "/" + path);
    }

    public String getUriPath(File file) {
        if (file == null)
            return "";
        String relativePath = file.getAbsolutePath().substring(dir.getAbsolutePath().length() + 1);
        return relativePath;
    }

    /**
     * Load a byte array from cache(a cache file) with the key
     *
     * @param key
     * @return
     */
    @CheckForNull
    public byte[] loadBytesWithKey(String key) {
        return dataFromFile(getCacheFile(key));
    }

    /**
     * open a file or just check if it exist. XXX:strange design, should refactor it.
     */
    @CheckForNull
    public synchronized File cachedFile(String absoluteUriString, boolean checkExist) {
        try {
            File file = getCacheFile(absoluteUriString);
            if (checkExist && !file.exists())
                file = null;
            return file;
        } catch (Exception e) {
            e(e, "Failed to invoke cachedFile");
        }
        return null;
    }


    /**
     * Create the cache dir on SD card or in phone storage.
     */
    protected void prepareCacheDir() {
        if (SysUtils.EXTERNAL_STORAGE_WRITABLE) {
            dir = new File(Environment.getExternalStorageDirectory(), cacheDirName);
            if (!dir.exists())
                dir.mkdirs();
            i("cache dir in external storage");
        } else {
            dir = context.getDir(cacheDirName, Context.MODE_PRIVATE);
            i("cache dir in phone storage");
        }

        if (!dir.exists())
            w("cache dir %s, doesn't exist", dir);
    }

    protected String keyToStoreName(@NonNull String key) {
        return md5(key);
    }
}
