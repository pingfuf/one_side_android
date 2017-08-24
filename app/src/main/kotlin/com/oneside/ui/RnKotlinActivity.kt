package com.oneside.ui

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler

import com.oneside.R
import com.oneside.base.BaseActivity
import com.oneside.base.rn.CardReactActivityDelegate
import com.oneside.base.rn.RNConfig

class RnKotlinActivity : BaseActivity() , DefaultHardwareBackBtnHandler{
    lateinit var mDelegate : CardReactActivityDelegate
    lateinit var mRlContent: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rn_kotlin)
        mDelegate = CardReactActivityDelegate(this, RNConfig.MAIN_COMPONENT_NAME)
        mRlContent = findViewById(R.id.rl_container) as RelativeLayout
        mRlContent.addView(mDelegate.reactRootView)
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun invokeDefaultOnBackPressed() {
        mDelegate.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mDelegate.onActivityResult(requestCode, resultCode, data)
    }
}