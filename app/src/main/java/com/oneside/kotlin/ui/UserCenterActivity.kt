package com.oneside.kotlin.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView

import com.oneside.R
import com.oneside.base.BaseActivity

class UserCenterActivity : BaseActivity() {
    lateinit var mDialog : Dialog
    lateinit var mIvHeader : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_center)
        setTitle("个人资料", true)
    }
}
