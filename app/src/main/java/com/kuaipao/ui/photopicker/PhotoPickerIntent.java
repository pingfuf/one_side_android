package com.kuaipao.ui.photopicker;

import com.kuaipao.utils.Constant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoPickerIntent extends Intent {

    private PhotoPickerIntent() {
    }

    protected PhotoPickerIntent(Parcel in) {
        readFromParcel(in);
    }

    private PhotoPickerIntent(Intent o) {
        super(o);
    }

    private PhotoPickerIntent(String action) {
        super(action);
    }

    private PhotoPickerIntent(String action, Uri uri) {
        super(action, uri);
    }

    private PhotoPickerIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public PhotoPickerIntent(Context packageContext) {
        super(packageContext, PhotoPickerActivity.class);
    }

    public void setPhotoMaxCount(int photoMaxCount) {
        this.putExtra(Constant.EXTRA_MAX_COUNT, photoMaxCount);
    }

    public void setShowCamera(boolean showCamera) {
        this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
    }

    public void setCropImageAfterPick(boolean willCropImage) {
        this.putExtra(PhotoPickerActivity.EXTRA_CROP_IMAGE_AFTER_PICK, willCropImage);
    }

    public void setCropImageForBgAfterPick(boolean willCropImage){
        this.putExtra(PhotoPickerActivity.EXTRA_CROP_BG_IMAGE_AFTER_PICK, willCropImage);
    }

//  public void setSelectedPhotoes(ArrayList<String> selectedPhotoes) {
//    if(!LangUtils.isEmpty(selectedPhotoes))
//      this.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, selectedPhotoes);
//  }

    public static final Parcelable.Creator<Intent> CREATOR
            = new Parcelable.Creator<Intent>() {
        public Intent createFromParcel(Parcel in) {
            return new PhotoPickerIntent(in);
        }
        public Intent[] newArray(int size) {
            return new Intent[size];
        }
    };
}
