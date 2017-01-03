package com.kuaipao.base.view.filter.model;

import java.io.Serializable;
import java.util.List;

/**
 * User: XXKuaipao(www.xxkuaipao.com)
 * Date: 2016-05-18
 * Time: 14:14
 * Author: pingfu
 * FIXME
 */
public class MultiColumnItemData extends FilterData {
    /**
     *
     */
    public int x;

    /**
     *
     */
    public int y;

    /**
     *
     */
    public int type;

    public MultiColumnItemData parentData;

    /**
     *
     */
    public List<MultiColumnItemData> items;
}
