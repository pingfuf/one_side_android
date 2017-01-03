package com.oneside.utils;

import static com.oneside.utils.LogUtils.d;

import java.util.LinkedList;

import com.oneside.base.CardConfig;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * a pool of temporary byte[] buffers
 *
 * @author Bo Hu
 *         created at 1/31/11, 10:41 AM
 */
public class BytesPool {
    private LinkedList<byte[]> pool = new LinkedList<byte[]>();

    private static int creation_count = 0;

    @NonNull
    public synchronized byte[] acquire(int minCapacity) {
        int size = pool.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (pool.get(i).length >= minCapacity) {
                index = i;
                break;
            }
        }

        byte[] ret = null;
        if (index != -1)
            ret = pool.remove(index);
        else if (pool.isEmpty()) {
            ret = new byte[minCapacity];
            if (CardConfig.DEV_BUILD) {
                creation_count++;
                d("bytes creation: " + creation_count);
            }
        } else
            ret = pool.remove(0);
        return ret;
    }

    public synchronized void release(@NonNull byte[] buf) {
        if (!pool.contains(buf))
            pool.addLast(buf);
    }

    public synchronized void clear() {
        pool.clear();
    }
}
