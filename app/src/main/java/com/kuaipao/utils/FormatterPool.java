package com.kuaipao.utils;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * @author Bo Hu
 *         created at 2/28/11, 11:05 PM
 */
public class FormatterPool {
    private List<FormatterWrapper> pool = new ArrayList<FormatterWrapper>();

    @NonNull
    public synchronized FormatterWrapper acquire() {
        if (pool.isEmpty())
            return new FormatterWrapper();
        else
            return pool.remove(0);
    }

    public synchronized void release(FormatterWrapper fw) {
        if (!pool.contains(fw))
            pool.add(fw);
    }

    public synchronized void clear() {
        pool.clear();
    }
}
