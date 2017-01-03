package com.kuaipao.ui.photopicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaipao.base.BaseActivity;
import com.kuaipao.utils.Constant;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.SysUtils;
import com.kuaipao.utils.ViewUtils;
import com.kuaipao.ui.photopicker.PhotoPickerGridAdapter.OnItemCheckListener;
import com.kuaipao.ui.photopicker.PhotoPickerGridAdapter.OnPhotoClickListener;
import com.kuaipao.manager.R;

/**
 * 选择照片界面
 */
public class PhotoPickerActivity extends BaseActivity implements View.OnClickListener {

    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public final static String EXTRA_CROP_IMAGE_AFTER_PICK = "crop_image_after_pick";
    public final static String EXTRA_CROP_BG_IMAGE_AFTER_PICK = "crop_bg_image_after_pick";


    public final static int REQUEST_CODE_PHOTO_PREVIEW = 7777;
    public final static int REQUEST_CODE_PHOTO_PREVIEW_AFTER_PICK = 7778;

    private int maxCount = Constant.DEFAULT_MAX_COUNT;
    private boolean bWillCropImage = false;
    private boolean bWillCropForBg = false;

    private ArrayList<String> mSelectedPhotosOriginal;
    private volatile boolean bIsOriginal = true;

    private ImageCaptureManager captureManager;
    private PhotoPickerGridAdapter photoGridAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private PopupWindow mPopupWindow;
    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirectory> directories;

    private RelativeLayout layoutSwitchDirectory;
    private TextView tvSwitchDirectory;
    private TextView tvPreview;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_picker);

        TextView textView = new TextView(getApplicationContext());
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(15);
        textView.setOnClickListener(this);
        textView.setText(R.string.yes);
        int padding = ViewUtils.rp(15);
        textView.setPadding(padding, 0, padding, 0);
        setTitle(getString(R.string.title_activity_choose_pic), true, textView);
        mTitleBar.setBackPressedImageResource(R.drawable.btn_back_white);
        mTitleBar.setBackgroundColor(getResources().getColor(R.color.papaya_primary_color));

        initData();
        initView();
    }

    private void initData() {
        directories = new ArrayList<>();
        captureManager = new ImageCaptureManager(this);
        mSelectedPhotosOriginal = getIntent().getStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS);

        MediaStoreHelper.getPhotoDirs(this, new MediaStoreHelper.PhotosResultCallback() {
            @Override
            public void onResultCallback(List<PhotoDirectory> directories) {
                PhotoPickerActivity.this.directories.clear();
                PhotoPickerActivity.this.directories.addAll(directories);

                if (bIsOriginal && LangUtils.isNotEmpty(mSelectedPhotosOriginal)) {
                    bIsOriginal = false;

                    List<String> currentPhotoes = photoGridAdapter.getCurrentPhotoPaths();

                    // add
                    for (String selectedPhoto : mSelectedPhotosOriginal) {
                        int index = currentPhotoes.indexOf(selectedPhoto);
                        photoGridAdapter.toggleSelection(photoGridAdapter.getCurrentPhotos().get(index));
                    }
                    int selected = photoGridAdapter.getSelectedPhotoPaths().size();
                    if (selected < 1) {
                        tvPreview.setText(R.string.preview);
                        tvPreview.setTextColor(getResources().getColor(R.color.alpha_30_percent_white));
                        tvPreview.setOnClickListener(null);
                    } else {
                        if (selected <= maxCount)
                            tvPreview.setText(getString(R.string.preview_count, selected));

                        tvPreview.setTextColor(getResources().getColorStateList(R.color.btn_white_text));
                        tvPreview.setOnClickListener(PhotoPickerActivity.this);
                    }
                }

                photoGridAdapter.notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        maxCount = getIntent().getIntExtra(Constant.EXTRA_MAX_COUNT, Constant.DEFAULT_MAX_COUNT);
        boolean showCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        bWillCropImage = getIntent().getBooleanExtra(EXTRA_CROP_IMAGE_AFTER_PICK, false) && maxCount == 1;
        bWillCropForBg = getIntent().getBooleanExtra(EXTRA_CROP_BG_IMAGE_AFTER_PICK, false) && maxCount == 1;
        photoGridAdapter = new PhotoPickerGridAdapter(PhotoPickerActivity.this, directories);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_photos);
        layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(photoGridAdapter);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());

        layoutSwitchDirectory = (RelativeLayout) findViewById(R.id.layout_choose_dir);
        tvSwitchDirectory = (TextView) findViewById(R.id.tv_choose_dir);
        tvPreview = (TextView) findViewById(R.id.tv_preview);

        initPopupView();

        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                final int index = showCamera ? position - 1 : position;

                ArrayList<String> photos = (ArrayList<String>) photoGridAdapter.getCurrentPhotoPaths();
                ArrayList<String> selectedPhotos = photoGridAdapter.getSelectedPhotoPaths();

                PhotoPreviewWhenPickIntent intent = new PhotoPreviewWhenPickIntent(PhotoPickerActivity.this);
                intent.setCurrentIndex(index);
                intent.setPhotoMaxCount(maxCount);
                intent.setSelectedPhotoes(selectedPhotos);
                intent.setTotalPhotoes(photos);
                startActivityForResult(intent, REQUEST_CODE_PHOTO_PREVIEW);
            }
        });

        photoGridAdapter.setOnCameraClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = captureManager.dispatchTakePictureIntent();
                    startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                } catch (IOException e) {
                    LogUtils.d(">>>> dispatchTakePictureIntent 4");
                    e.printStackTrace();
                }
            }
        });

        photoGridAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean OnItemCheck(int position, Photo photo, final boolean isCheck, int selectedItemCount) {
                int total = selectedItemCount + (isCheck ? -1 : 1);

                if (total < 1) {
                    tvPreview.setText(R.string.preview);
                    tvPreview.setTextColor(getResources().getColor(R.color.alpha_30_percent_white));
                    tvPreview.setOnClickListener(null);
                } else {
                    if (total <= maxCount)
                        tvPreview.setText(getString(R.string.preview_count, total));

                    tvPreview.setTextColor(getResources().getColorStateList(R.color.btn_white_text));
                    tvPreview.setOnClickListener(PhotoPickerActivity.this);
                }

                if (maxCount <= 1) {
                    List<Photo> photos = photoGridAdapter.getSelectedPhotos();
                    if (!photos.contains(photo)) {
                        photos.clear();
                        photoGridAdapter.notifyDataSetChanged();
                    }
                    return true;
                }

                if (total > maxCount) {
                    Toast.makeText(PhotoPickerActivity.this,
                            getString(R.string.over_max_count_tips, maxCount), Toast.LENGTH_LONG).show();
                    return false;
                }

                return true;
            }
        });

        layoutSwitchDirectory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                mPopupWindow.showAsDropDown(layoutSwitchDirectory, 0, 0);

                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
            }
        });

        tvPreview.setOnClickListener(null);
    }

    private void initPopupView() {
        listAdapter = new PopupDirectoryListAdapter(this, directories);

        View contentView = LayoutInflater.from(this).inflate(R.layout.list_dir, null);
        ListView lv = (ListView) contentView.findViewById(R.id.id_list_dir);
        lv.setAdapter(listAdapter);
        mPopupWindow = new PopupWindow(contentView, SysUtils.WIDTH, (int) (SysUtils.HEIGHT * 0.7), true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupWindow.dismiss();

                if (listAdapter.getCurrentCheckedIndex() != position) {
                    PhotoDirectory directory = directories.get(position);

                    tvSwitchDirectory.setText(directory.getName());

                    listAdapter.setCurrentCheckedIndex(position);
                    photoGridAdapter.setCurrentDirectoryIndex(position);
                    photoGridAdapter.notifyDataSetChanged();
                    layoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        });
    }





    private void doFinish(ArrayList<String> selectedPhotos) {
        if ((bWillCropImage || bWillCropForBg) && LangUtils.isNotEmpty(selectedPhotos)) {
            Intent intent = new Intent(PhotoPickerActivity.this, PhotoPreviewAfterPickActivity.class);
            LogUtils.d(">>>> doFinish() selectedPhotos=%s", selectedPhotos);
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_PHOTO_PATH, selectedPhotos.get(0));
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_WILL_CROP_IMAGE, bWillCropImage);
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_WILL_CROP_IMG_FOR_BG, bWillCropForBg);
            startActivityForResult(intent, REQUEST_CODE_PHOTO_PREVIEW_AFTER_PICK);
        } else {
            Intent intent = new Intent();
            intent.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, selectedPhotos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO) {

            if (captureManager == null || LangUtils.isEmpty(captureManager.getCurrentPhotoPath())) {
                ViewUtils.showToast(getResources().getString(R.string.take_photo_failed), Toast.LENGTH_SHORT);
                return;
            }
            File file = new File(captureManager.getCurrentPhotoPath());
            LogUtils.d(">>>> onActivityResult() captureManager.getCurrentPhotoPath()=%s", captureManager.getCurrentPhotoPath());
            if (!file.exists() || file.length() == 0) {
                LogUtils.d(">>>> onActivityResult() !file.exists() file.length=%s", file.length());
                return;
            }

            Intent intent = new Intent(PhotoPickerActivity.this, PhotoPreviewAfterPickActivity.class);
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_PHOTO_PATH, captureManager.getCurrentPhotoPath());
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_WILL_CROP_IMAGE, bWillCropImage);
            intent.putExtra(PhotoPreviewAfterPickActivity.EXTRA_WILL_CROP_IMG_FOR_BG,bWillCropForBg);
            startActivityForResult(intent, REQUEST_CODE_PHOTO_PREVIEW_AFTER_PICK);

//      captureManager.galleryAddPic();
//      if (directories.size() > 0) {
//        String path = captureManager.getCurrentPhotoPath();
//        PhotoDirectory directory = directories.get(MediaStoreHelper.INDEX_ALL_PHOTOS);
//        Photo newPic = new Photo(path.hashCode(), path);
//        directory.getPhotos().add(MediaStoreHelper.INDEX_ALL_PHOTOS, newPic);
//        directory.setCoverPath(path);
//        if (maxCount <= 1) {
//          photoGridAdapter.getSelectedPhotos().clear();
//        }else if(photoGridAdapter.getSelectedPhotos().size() < maxCount){
//          photoGridAdapter.toggleSelection(newPic);
//        }
//        photoGridAdapter.notifyDataSetChanged();
//      }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            boolean isCompleteDirectly =
                    data.getBooleanExtra(PhotoPreviewWhenPickActivity.EXTRA_IS_COMPLETE_DIRECTLY, false);

            ArrayList<String> selectedPhotoesPath =
                    data.getStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS);

            if (isCompleteDirectly) {
                doFinish(selectedPhotoesPath);
            } else {
                List<String> lastSelectedPhotoesPath = photoGridAdapter.getSelectedPhotoPaths();
                List<String> currentPhotoes = photoGridAdapter.getCurrentPhotoPaths();

                // add
                for (String selectedPhoto : selectedPhotoesPath) {
                    if (LangUtils.isEmpty(lastSelectedPhotoesPath)
                            || !lastSelectedPhotoesPath.contains(selectedPhoto)) {
                        int index = currentPhotoes.indexOf(selectedPhoto);
                        photoGridAdapter.toggleSelection(photoGridAdapter.getCurrentPhotos().get(index));
                    }
                }

                // delete
                if (!LangUtils.isEmpty(lastSelectedPhotoesPath)) {
                    Iterator<Photo> iterator = photoGridAdapter.getSelectedPhotos().iterator();
                    while (iterator.hasNext()) {
                        Photo lastSelectedPhoto = iterator.next();
                        if (LangUtils.isEmpty(selectedPhotoesPath) || !selectedPhotoesPath.contains(lastSelectedPhoto.getPath())) {
                            iterator.remove();
                        }
                    }
                }

                photoGridAdapter.notifyDataSetChanged();

                int selected = photoGridAdapter.getSelectedPhotoPaths().size();
                if (selected < 1) {
                    tvPreview.setText(R.string.preview);
                    tvPreview.setTextColor(getResources().getColor(R.color.alpha_30_percent_white));
                    tvPreview.setOnClickListener(null);
                } else {
                    if (selected <= maxCount)
                        tvPreview.setText(getString(R.string.preview_count, selected));

                    tvPreview.setTextColor(getResources().getColorStateList(R.color.btn_white_text));
                    tvPreview.setOnClickListener(PhotoPickerActivity.this);
                }
            }
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW_AFTER_PICK) {
            String selectedPhoto = data.getStringExtra(PhotoPreviewAfterPickActivity.EXTRA_PHOTO_PATH);

            if (LangUtils.isEmpty(selectedPhoto)) {
                ViewUtils.showToast(getString(R.string.file_save_fail), Toast.LENGTH_LONG);
            } else {
                galleryAddPic(selectedPhoto);

                ArrayList<String> selectedPhotoesPath = new ArrayList<String>(1);
                selectedPhotoesPath.add(selectedPhoto);

                Intent intent = new Intent();
                intent.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, selectedPhotoesPath);
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }

    private void galleryAddPic(String filePath) {
        if (LangUtils.isEmpty(filePath))
            return;
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, contentUri));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        captureManager.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(tvPreview)) {
            ArrayList<String> selectedPhotos = photoGridAdapter.getSelectedPhotoPaths();


            PhotoPreviewWhenPickIntent intent = new PhotoPreviewWhenPickIntent(PhotoPickerActivity.this);
            intent.setCurrentIndex(0);
            intent.setPhotoMaxCount(maxCount);
            intent.setSelectedPhotoes(selectedPhotos);
            intent.setTotalPhotoes(selectedPhotos);
            startActivityForResult(intent, REQUEST_CODE_PHOTO_PREVIEW);
        }else if(v instanceof  TextView){
            //Right click
            if (LangUtils.isEmpty(photoGridAdapter.getSelectedPhotoPaths())) {
                ViewUtils.showToast(getResources().getString(R.string.done_fail_tip), Toast.LENGTH_LONG);
            } else
                doFinish(photoGridAdapter.getSelectedPhotoPaths());
        }
    }

}
