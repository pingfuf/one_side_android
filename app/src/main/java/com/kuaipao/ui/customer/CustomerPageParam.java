package com.kuaipao.ui.customer;

import com.kuaipao.base.model.BasePageParam;
import com.kuaipao.model.beans.XUserPhysicalRecord;

/**
 * Created by pingfu on 16-11-2.
 */
public class CustomerPageParam extends BasePageParam {
    public String headerUrl;
    public String name;
    public String phone;
    public String gender;

    public XUserPhysicalRecord record;

    public String photoUrl;

    public String photoThumbUrl;
}
