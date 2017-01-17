package com.oneside.base;

/**
 * Created by pingfu on 16-7-20.
 */
public abstract class BaseLazyLoadFragment extends BaseFragment {
    private boolean isFirstLoadData = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint() && getView() != null && !isFirstLoadData) {
            lazyLoad();
            isFirstLoadData = true;
        }
    }

    /**
     * 延缓ViewPager的预加载
     */
    protected abstract void lazyLoad();
}