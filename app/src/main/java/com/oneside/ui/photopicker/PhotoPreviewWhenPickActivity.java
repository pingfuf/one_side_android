package com.oneside.ui.photopicker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oneside.base.BaseActivity;
import com.oneside.ui.CustomDialog;
import com.oneside.utils.Constant;
import com.oneside.utils.LangUtils;
import com.oneside.R;
import com.oneside.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewWhenPickActivity extends BaseActivity implements OnClickListener {


    public final static String EXTRA_ONLY_DELETE = "ONLY_DELETE";
    public final static String EXTRA_CURRENT_ITEM = "CURRENT_ITEM";
    public final static String EXTRA_TOTAL_PHOTOS = "TOTAL_PHOTOS";
    public final static String EXTRA_IS_COMPLETE_DIRECTLY = "is_complete_directory";


    private int maxCount = Constant.DEFAULT_MAX_COUNT;

    private boolean bOnlyDelete = false;


    private List<String> mListSelected;
    private List<String> mListTotal;
    private int mCurrentItem = 0;

    private RelativeLayout mLayoutBottom;
    private TextView tvSelect;
    private ImageView ivSelect;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;

    private TextView tvRight;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_preview_when_pick);
        tvRight = ViewUtils.createTitleBarRightTextView(this, "删除");
        tvRight.setOnClickListener(this);
        setTitle("", true, tvRight);
        // setTitle(R.string.title_activity_scan_choose_pic);
        initData();
        initView();
    }

    private void initData() {
        maxCount = getIntent().getIntExtra(Constant.EXTRA_MAX_COUNT, Constant.DEFAULT_MAX_COUNT);
        bOnlyDelete = getIntent().getBooleanExtra(EXTRA_ONLY_DELETE, false);
        mCurrentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
        mListSelected = getIntent().getStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS);
        mListTotal = getIntent().getStringArrayListExtra(EXTRA_TOTAL_PHOTOS);

        setTitle(String.format("%d/%d", mCurrentItem + 1, mListTotal.size()));
        mPagerAdapter = new PhotoPagerAdapter(this, mListTotal);
    }

    private void initView() {
        mLayoutBottom = (RelativeLayout) findViewById(R.id.layout_bottom);
        tvSelect = (TextView) findViewById(R.id.tv_select);
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        tvSelect.setOnClickListener(this);
        ivSelect.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentItem);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                if (LangUtils.isNotEmpty(mListSelected) && mListSelected.contains(mListTotal.get(index))) {
                    ivSelect.setImageResource(R.drawable.pictures_selected);
                } else {
                    ivSelect.setImageResource(R.drawable.picture_unselected);
                }
                setTitle(String.format("%d/%d", index + 1, mListTotal.size()));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        if (bOnlyDelete) {
            mLayoutBottom.setVisibility(View.GONE);
        } else {
            mLayoutBottom.setVisibility(View.VISIBLE);
            updateTitleRight();
            if (LangUtils.isNotEmpty(mListSelected) && mListSelected.contains(mListTotal.get(mCurrentItem))) {
                ivSelect.setImageResource(R.drawable.pictures_selected);
            } else {
                ivSelect.setImageResource(R.drawable.picture_unselected);
            }
        }
    }

    private void updateTitleRight() {
        if (maxCount <= 1) {
            return;
        }
    }

    protected boolean onRightClick() {
        Intent i = new Intent();
        if (bOnlyDelete) {
            showDeleteConfirmDialog();
        } else {
            if (LangUtils.isEmpty(mListSelected)) {
                if (mListSelected == null)
                    mListSelected = new ArrayList<String>(1);
                mListSelected.add(mListTotal.get(mViewPager.getCurrentItem()));
            }

            i.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, (ArrayList<String>) mListSelected);
            i.putExtra(EXTRA_IS_COMPLETE_DIRECTLY, true);
            setResult(RESULT_OK, i);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, (ArrayList<String>) mListSelected);
        i.putExtra(EXTRA_IS_COMPLETE_DIRECTLY, false);
        setResult(RESULT_OK, i);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(tvSelect) || v.equals(ivSelect)) {
            String currentPhoto = mListTotal.get(mViewPager.getCurrentItem());
            if (LangUtils.isNotEmpty(mListSelected) && mListSelected.contains(currentPhoto)) {
                mListSelected.remove(currentPhoto);
                ivSelect.setImageResource(R.drawable.picture_unselected);
            } else {
                if (!LangUtils.isEmpty(mListSelected) && mListSelected.size() >= maxCount) {
                    Toast.makeText(PhotoPreviewWhenPickActivity.this,
                            getString(R.string.over_max_count_tips, maxCount), Toast.LENGTH_LONG).show();
                    return;
                }
                mListSelected.add(currentPhoto);
                ivSelect.setImageResource(R.drawable.pictures_selected);
            }
            updateTitleRight();
        } else if(v == tvRight) {
            onRightClick();
        }
    }

    private void showDeleteConfirmDialog() {
        CustomDialog.Builder b =
                new CustomDialog.Builder(this).setPositiveButton(R.string.city_no, null)
                        .setNegativeButton(R.string.city_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (mListTotal != null && mListTotal.size() <= 1) {
                                    mListSelected.clear();
                                    Intent i = new Intent();
                                    i.putStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS, (ArrayList<String>) mListSelected);
                                    i.putExtra(EXTRA_IS_COMPLETE_DIRECTLY, true);
                                    setResult(RESULT_OK, i);

                                    PhotoPreviewWhenPickActivity.this.finish();
                                    return;
                                }
                                mListSelected.remove(mViewPager.getCurrentItem());
                                mListTotal.remove(mViewPager.getCurrentItem());
                                setTitle(String.format("%d/%d", mViewPager.getCurrentItem() + 1, mListTotal.size()));
                                mPagerAdapter.notifyDataSetChanged();
                            }
                        });
        CustomDialog d = b.create();
        d.setMessage(this.getString(R.string.photo_delete_tip));
        d.show();

    }
}
