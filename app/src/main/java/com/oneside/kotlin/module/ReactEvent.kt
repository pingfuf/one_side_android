package com.oneside.kotlin.module

import com.alibaba.fastjson.JSON
import java.io.Serializable

/**
 * Created by fupingfu on 2017/8/30.
 */
class ReactEvent : Serializable {
    val serialVersionUID = 4117251956057845067L
    /** event 类型 */
    lateinit var type : String

    /** event 参数 */
    lateinit var params : Map<String, String>

    fun toJsonString() : String {
        var json = ""
        json += "type:" + type
        json += ", params:"
        json += JSON.toJSONString(params)
        json += "}"

        return json
    }
}