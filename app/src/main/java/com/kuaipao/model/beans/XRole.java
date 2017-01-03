package com.kuaipao.model.beans;

import java.io.Serializable;

/**
 * 用户角色信息
 *
 * Created by pingfu on 16-10-27.
 */
public class XRole implements Serializable {
    public long id;
    public String name;
    public boolean deletable;
    public boolean editable;
}
