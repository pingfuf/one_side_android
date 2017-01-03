package com.oneside.model.response;

import com.oneside.base.net.model.BaseResult;

import java.util.List;

/**
 * Created by pingfu on 16-10-8.
 */
public class AddCourseResponse extends BaseResult {
    public List<String> members;
    public String startTime;
    public List<String> types;
}
