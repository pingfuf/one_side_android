package com.kuaipao.utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * @author Bo Hu
 *         created at 1/25/11, 3:44 PM
 */
//@ThreadSafe
public class StringBuilderPool {
    @NonNull
    private List<StringBuilder> pool = new ArrayList<StringBuilder>();

    @NonNull
    public synchronized StringBuilder acquire(int capacity) {
        int size = pool.size();
        int index = -1;
        for (int i = 0; i < size; i++) {
            StringBuilder sb = pool.get(i);
            if (sb.capacity() >= capacity) {
                index = i;
                break;
            }
        }

        StringBuilder sb = null;
        if (index != -1)
            sb = pool.remove(index);
        else if (pool.isEmpty())
            sb = new StringBuilder(capacity);
        else
            sb = pool.remove(0);
        sb.setLength(0);
        return sb;
    }

    /**
     * Release the StringBuilder and get the string
     *
     * @param sb
     * @return
     */
    @NonNull
    public synchronized String release(@NonNull StringBuilder sb) {
        if (!pool.contains(sb))
            pool.add(sb);
        return sb.toString();
    }

    public synchronized void releaseOnly(@NonNull StringBuilder sb) {
        if (!pool.contains(sb))
            pool.add(sb);
    }

    public synchronized void clear() {
        pool.clear();
    }

    public void debug() {
        int sum = 0;
        for (StringBuilder sb : pool)
            sum += sb.capacity();
        Log.d(LogUtils.LOG_ID, "pool size: " + sum);
    }
}
