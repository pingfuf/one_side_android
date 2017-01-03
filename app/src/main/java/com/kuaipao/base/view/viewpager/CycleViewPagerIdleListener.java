package com.kuaipao.base.view.viewpager;

import android.view.View;

/**
 * viewpager处于空闲状态监听器
 * 
 * @author YD
 *
 */
public interface CycleViewPagerIdleListener {
	void onPagerSelected(View view, int position);
}
