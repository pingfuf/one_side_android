package com.oneside.ui.photopicker;

import java.util.ArrayList;

import com.oneside.utils.Constant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhotoPreviewWhenPickIntent extends Intent {

    private PhotoPreviewWhenPickIntent() {
    }

    private PhotoPreviewWhenPickIntent(Intent o) {
        super(o);
    }

    private PhotoPreviewWhenPickIntent(String action) {
        super(action);
    }

    private PhotoPreviewWhenPickIntent(String action, Uri uri) {
        super(action, uri);
    }

    private PhotoPreviewWhenPickIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public PhotoPreviewWhenPickIntent(Context packageContext) {
        super(packageContext, PhotoPreviewWhenPickActivity.class);
    }

    public void setPhotoMaxCount(int photoMaxCount) {
        this.putExtra(Constant.EXTRA_MAX_COUNT, photoMaxCount);
    }

    public void setCurrentIndex(int currentIndex) {
        this.putExtra(PhotoPreviewWhenPickActivity.EXTRA_CURRENT_ITEM, currentIndex);
    }

    public void setSelectedPhotoes(ArrayList<String> selectedPhotoes) {
//    if(!LangUtils.isEmpty(selectedPhotoes))
        this.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, selectedPhotoes);
    }

    public void setTotalPhotoes(ArrayList<String> totalPhotoes) {
//    if(!LangUtils.isEmpty(totalPhotoes))
        this.putStringArrayListExtra(PhotoPreviewWhenPickActivity.EXTRA_TOTAL_PHOTOS, totalPhotoes);
    }


    public void setOnlyShowDelete(boolean bOnlyShowDelete) {
        this.putExtra(PhotoPreviewWhenPickActivity.EXTRA_ONLY_DELETE, bOnlyShowDelete);
    }
}
