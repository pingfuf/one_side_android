package com.oneside.kotlin.base

import com.oneside.model.beans.XUser
import com.oneside.model.response.MemberDetailInfoResponse
import org.greenrobot.eventbus.EventBus

/**
 * Created by fupingfu on 2017/12/18.
 */
class GlobalParam private constructor(){
    //用户名
    var userName : String? = null

    /** 用户ID */
    var userId : String? = null

    companion object {
        @Volatile
        private var userName : String = ""

        @Volatile
        private var mInstance : GlobalParam? = null

        fun getInstance() : GlobalParam {
            if (mInstance == null) {
                synchronized(GlobalParam::class) {
                    if (mInstance == null) {
                        mInstance = GlobalParam()
                    }
                }
            }

            return mInstance!!
        }
    }

    fun setUser(user : XUser) {
        userName = user.avatar
        userId = user.id.toString()
        EventBus.getDefault().post(userName)
    }
}