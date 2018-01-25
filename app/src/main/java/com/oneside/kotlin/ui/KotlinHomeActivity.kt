package com.oneside.kotlin.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.EditText
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.oneside.R
import com.oneside.base.BaseActivity
import com.oneside.ui.view.TabView

class KotlinHomeActivity : BaseActivity() {
    private var mItemsView : TabView? = null
    private var mEdtName : EditText? = null
    private var mTvName : TextView? = null
    private var mFragments : List<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_home)

        initView()
    }

    /**
     *
     */
    fun initView() {
        mEdtName = findViewById(R.id.edt_name) as EditText
        mTvName = findViewById(R.id.tv_name) as TextView
        mItemsView = findViewById(R.id.tv_items) as TabView

        RxTextView.afterTextChangeEvents(mEdtName!!).subscribe()
    }
}
