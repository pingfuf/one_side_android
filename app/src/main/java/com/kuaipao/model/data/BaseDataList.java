package com.kuaipao.model.data;

import java.util.List;

/**
 * Created by guoming on 6/14/16.
 */
public abstract class BaseDataList {

    private int page = 0;// page or offset
    protected boolean hasMore;

    public abstract List getList();
    public abstract void initList();



    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Add another list into this
     * @param otherList  Another list
     * @param refresh Indicate whether refresh
     */
    public void addOtherList(BaseDataList otherList, boolean refresh) {
        if (otherList == null) {
            return;
        }
        List list = getList();
        if (list == null) {
            initList();
            list = getList();

        } else {
            if (refresh) {
                if (list != null) {
                    list.clear();
                }
            }
        }
        if (otherList.getList() != null)
            list.addAll(otherList.getList());
        setHasMore(otherList.isHasMore());
        if (otherList.getPage() != 0){
            setPage(otherList.getPage());
        }
    }
}
