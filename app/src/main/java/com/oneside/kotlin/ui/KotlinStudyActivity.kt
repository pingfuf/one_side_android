package com.oneside.kotlin.ui

import android.os.Bundle
import android.widget.TextView

import com.oneside.R
import com.oneside.base.BaseActivity
import com.oneside.base.utils.BusinessStateHelper

class KotlinStudyActivity : BaseActivity() {
    private var mStateHelper : BusinessStateHelper? = null
    private lateinit var mTvTemp : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_study)

        mTvTemp = findViewById(R.id.tv_temp) as TextView
        mStateHelper = BusinessStateHelper.build(this, mTvTemp)
        mStateHelper?.setState(BusinessStateHelper.BusinessState.LOADING)

        mTvTemp.postDelayed({
            mStateHelper?.setState(BusinessStateHelper.BusinessState.FINISHED)
        }, 2000)
    }
}
