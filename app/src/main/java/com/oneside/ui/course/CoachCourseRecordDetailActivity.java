package com.oneside.ui.course;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.base.model.BasePageParam;
import com.oneside.base.net.IService;
import com.oneside.base.net.RequestDelegate;
import com.oneside.base.net.UrlRequest;
import com.oneside.base.net.model.BaseResult;
import com.oneside.base.net.model.NetworkParam;
import com.oneside.manager.CardManager;
import com.oneside.manager.CardSessionManager;
import com.oneside.R;
import com.oneside.model.beans.XAction;
import com.oneside.model.beans.XActionItem;
import com.oneside.model.beans.XActionNote;
import com.oneside.model.beans.XUser;
import com.oneside.model.response.CoachCourseDetailResponse;
import com.oneside.ui.adapter.PublishPicAdapter;
import com.oneside.ui.CustomDialog;
import com.oneside.ui.photopicker.ImageCaptureManager;
import com.oneside.ui.photopicker.PhotoPickerIntent;
import com.oneside.ui.photopicker.PhotoPreviewWhenPickIntent;
import com.oneside.ui.view.ActionItemView;
import com.oneside.ui.view.NoScrollGridView;
import com.oneside.utils.Constant;
import com.oneside.utils.IOUtils;
import com.oneside.utils.LangUtils;
import com.oneside.utils.LogUtils;
import com.oneside.utils.UploadPicFileUtils;
import com.oneside.utils.ViewUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by MVEN on 16/9/19.
 * <p>
 * email: magiwen@126.com.
 */
public class CoachCourseRecordDetailActivity extends BaseActivity implements ActionItemView.OnActionClickListener {
    private static final int REQUEST_CODE_CHOOSE_PICS_MODIFY = 101;
    @From(R.id.ll_body)
    private LinearLayout llBody;

    @From(R.id.tv_body)
    private TextView tvBody;

    @From(R.id.ll_action_container)
    private LinearLayout llActionContainer;

    @From(R.id.ll_support_type)
    private LinearLayout llSupportType;

    @From(R.id.tv_sport_type)
    private TextView tvSportType;

    @From(R.id.edt_sport_weight)
    private EditText edtSportWeight;

    @From(R.id.edt_oxygen_sport)
    private EditText edtOxygenSport;

    @From(R.id.ll_time)
    private LinearLayout llTime;

    @From(R.id.edit_time)
    private EditText editTime;

    @From(R.id.edt_distance)
    private EditText edtDistance;

    @From(R.id.edt_space)
    private EditText edtSpace;

    @From(R.id.edt_calorie)
    private EditText edtCalorie;

    @From(R.id.edt_comment)
    private EditText descEdit;

    @From(R.id.gv_items)
    private NoScrollGridView gvItems;

    @From(R.id.add_course_commit_btn)
    private Button publishButton;


    @From(R.id.course_detail_parts_text)
    private TextView partTextView;

    @From(R.id.course_detail_train_assess_title)
    private TextView commentTextView;

    private PublishPicAdapter mAdapter;

    private String sportType = "";
    private long arrangementId = 0;

//    private JSONObject trainingPartJson;
//    private List<String> mPics;

    private CoachCourseDetailResponse courseDetailModel;
    private boolean isModifyMode = false;
    private boolean isFromDraft = false;

    private List<String> strengthItems;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_course_detail);
        setTitle(getString(R.string.course_detail_title), true);
        arrangementId = getIntent().getLongExtra("arrangement_id", 0);
        isFromDraft = getIntent().getBooleanExtra("from_draft", false);
        courseDetailModel = (CoachCourseDetailResponse) getIntent().getSerializableExtra("course_detail");
        if (courseDetailModel == null) {
            isModifyMode = false;
            courseDetailModel = new CoachCourseDetailResponse();
        } else {
            if (isFromDraft) {
                isModifyMode = courseDetailModel.note.id > 0;// 0, is saved new course; 1, is saved updated failed course;
            } else {
                isModifyMode = true;
            }
//            isModifyMode = !isFromDraft;
            if (arrangementId == 0) {
                arrangementId = courseDetailModel.id;
            }
        }

        initUI();
        updateUI();
    }

    private void initUI() {
        strengthItems = new ArrayList<>(3);
        strengthItems.add(getString(R.string.strength_type_1));
        strengthItems.add(getString(R.string.strength_type_2));
        strengthItems.add(getString(R.string.strength_type_3));

        SpannableString ss = new SpannableString(getString(R.string.coach_course_parts_toast));
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.papaya_primary_color)), ss.length() - 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        partTextView.setText(ss);
        ss = new SpannableString(getString(R.string.coach_course_comment_toast));
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.papaya_primary_color)), ss.length() - 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        commentTextView.setText(ss);
        //动作Item
        llActionContainer.addView(createActionItemView());
        llSupportType.setOnClickListener(this);
        llBody.setOnClickListener(this);
        tvSportType.setTag(1);

        mAdapter = new PublishPicAdapter(this);
        mAdapter.setData(null);
        gvItems.setAdapter(mAdapter);

        gvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinishing()) {
                    return;
                }

                if (PublishPicAdapter.ADD_TAG.equals(mAdapter.getItem(position))) {
                    int alreadySelectedPicNum = 0;
                    if (courseDetailModel != null && courseDetailModel.note != null && courseDetailModel.note.imageUrls != null) {
                        alreadySelectedPicNum = courseDetailModel.note.imageUrls.size();
                    }
                    if (alreadySelectedPicNum >= Constant.DEFAULT_MAX_COUNT) {
                        Toast.makeText(CoachCourseRecordDetailActivity.this,
                                getString(R.string.over_max_count_tips, Constant.DEFAULT_MAX_COUNT),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    showTakePicDialog();
                } else {
                    PhotoPreviewWhenPickIntent intent =
                            new PhotoPreviewWhenPickIntent(CoachCourseRecordDetailActivity.this);
                    intent.setCurrentIndex(position);
                    intent.setPhotoMaxCount(Constant.DEFAULT_MAX_COUNT);
                    ArrayList<String> selectedItems = new ArrayList<String>();
                    for (int i = 0; i < mAdapter.getData().size(); i++) {
                        if (!PublishPicAdapter.ADD_TAG.equals(mAdapter.getItem(i))) {
                            selectedItems.add(mAdapter.getItem(i));
                        }
                    }

                    intent.setSelectedPhotoes(selectedItems);
                    intent.setTotalPhotoes(selectedItems);
                    intent.setOnlyShowDelete(true);
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_PICS_MODIFY);
                }
            }
        });
        publishButton.setOnClickListener(this);

        edtSportWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!LangUtils.isEmpty(s)) {
                    int i = LangUtils.parseInt(s, 0);
                    if (i > 100) {
                        ViewUtils.showToast("请填写100以内数字", Toast.LENGTH_LONG);
                        CharSequence s1 = s.subSequence(0, s.length() - 1);
                        edtSportWeight.setText(s1);
                        edtSportWeight.setSelection(s1.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void updateUI() {
        if (courseDetailModel == null) {
            sportType = "";
            tvSportType.setText("请选择运动方式");
            tvSportType.setTextColor(getResources().getColor(R.color.dark_shadow));
            return;
        }
        updatePartText();

        addActions(courseDetailModel.note.actions);

        int method = courseDetailModel.note.method;
        if (method <= 0) {
            tvSportType.setText("请选择运动方式");
            tvSportType.setTextColor(getResources().getColor(R.color.dark_shadow));
            sportType = "";
            method = 1;
        } else {
            tvSportType.setText(strengthItems.get(method - 1));
            sportType = strengthItems.get(method - 1);
            tvSportType.setTextColor(getResources().getColor(R.color.text_color_gray_66));
        }
        tvSportType.setTag(method);

        if(courseDetailModel.note.strength > 0 && courseDetailModel.note.strength <=100) {
            edtSportWeight.setText(String.valueOf(courseDetailModel.note.strength));
        } else {
            edtSportWeight.setText("");
            edtSportWeight.setHint("请填写100以内数字");
        }

        edtOxygenSport.setText(courseDetailModel.note.aerobicTraining);

        if(courseDetailModel.note.remainMinutes > 0) {
            editTime.setText(String.valueOf(courseDetailModel.note.remainMinutes));
        } else {
            editTime.setText("");
            editTime.setHint("请填写持续时间");
        }

        if(courseDetailModel.note.distance > 0) {
            edtDistance.setText(String.valueOf(courseDetailModel.note.distance));
        } else {
            edtDistance.setText("");
            edtDistance.setHint("请填写距离");
        }

        if(courseDetailModel.note.speed > 0) {
            edtSpace.setText(String.valueOf(courseDetailModel.note.speed));
        } else {
            edtSpace.setText("");
            edtSpace.setHint("请填写速度／阻尼");
        }

        if(courseDetailModel.note.calories > 0) {
            edtCalorie.setText(String.valueOf(courseDetailModel.note.calories));
        } else {
            edtCalorie.setText("");
            edtCalorie.setHint("请填写卡路里");
        }

        descEdit.setText(courseDetailModel.note.desc);

        mAdapter.setData(courseDetailModel.note.imageUrls);
        publishButton.setText(isModifyMode ? R.string.coach_course_modify : R.string.coach_course_upload);
    }

    private void showTakePicDialog() {
        final CustomDialog.Builder b =
                new CustomDialog.Builder(this).setTitle(R.string.comment_upload_pic1).setNegativeButton(
                        R.string.no, null);
        LinearLayout l = (LinearLayout) View.inflate(this, R.layout.dialog_two_img_layout, null);
        LinearLayout l1 = (LinearLayout) l.findViewById(R.id.dialog_img_1_layout);
        LinearLayout l2 = (LinearLayout) l.findViewById(R.id.dialog_img_2_layout);
        ImageView i1 = (ImageView) l.findViewById(R.id.dialog_img_1);
        ImageView i2 = (ImageView) l.findViewById(R.id.dialog_img_2);
        TextView t1 = (TextView) l.findViewById(R.id.dialog_img_1_tv);
        TextView t2 = (TextView) l.findViewById(R.id.dialog_img_2_tv);
        t1.setText(R.string.comment_album);
        t1.setTextColor(getResources().getColor(R.color.text_color_gray));
        t2.setText(R.string.comment_take_photo);
        t2.setTextColor(getResources().getColor(R.color.text_color_gray));
        i1.setImageResource(R.drawable.ic_photo_album);
        i2.setImageResource(R.drawable.ic_photo_graph);
        l1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                b.dismiss();
                PhotoPickerIntent intent = new PhotoPickerIntent(CoachCourseRecordDetailActivity.this);
                intent.setShowCamera(true);
                intent.setPhotoMaxCount(Constant.DEFAULT_MAX_COUNT);
                startActivityForResult(intent, 100);
            }

        });
        l2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                b.dismiss();

                takePhoto();
            }

        });
        b.setView(l);
        b.show();
    }

    private ImageCaptureManager mCaptureManager;

    private void takePhoto() {
        try {
            if (mCaptureManager == null) {
                mCaptureManager = new ImageCaptureManager(this);
            }
            Intent intent = mCaptureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                if (data != null) {
                    List<String> images = data.getStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS);
                    LogUtils.d("images %s", images);
                    if (courseDetailModel.note.imageUrls == null) {
                        courseDetailModel.note.imageUrls = new ArrayList<>();
                    }
                    courseDetailModel.note.imageUrls.addAll(images);
                    mAdapter.setData(courseDetailModel.note.imageUrls);

                }
            } else if (requestCode == 1) {
                if (data != null) {
                    String jsonString = data.getStringExtra("json");
                    if (LangUtils.isNotEmpty(jsonString)) {
                        JSONObject json = JSON.parseObject(jsonString);
                        courseDetailModel.note.setBodyPartsByJson(json);
                    }
                    updatePartText();

                }
            } else if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO) {
                if (mCaptureManager == null || LangUtils.isEmpty(mCaptureManager.getCurrentPhotoPath())) {
                    ViewUtils.showToast(getResources().getString(R.string.take_photo_failed), Toast.LENGTH_SHORT);
                    return;
                }

                String path = mCaptureManager.getCurrentPhotoPath();
                courseDetailModel.note.imageUrls.add(path);
                mAdapter.setData(courseDetailModel.note.imageUrls);
            } else if (requestCode == REQUEST_CODE_CHOOSE_PICS_MODIFY) {
                if (isFinishing() || data == null) {
                    return;
                }

                List<String> selectedPhotos = data.getStringArrayListExtra(Constant.EXTRA_SELECTED_PHOTOS);
                if(selectedPhotos == null) {
                    courseDetailModel.note.imageUrls.clear();
                    mAdapter.setData(new ArrayList<String>());
                    return;
                }

                List<String> currentPhotos = mAdapter.getData();
                List<String> resultPhotos = new ArrayList<>();

                if(currentPhotos != null) {
                    for(int i = 0; i < currentPhotos.size(); i++) {
                        if(selectedPhotos.contains(currentPhotos.get(i))) {
                            resultPhotos.add(currentPhotos.get(i));
                        }
                    }
                }
                courseDetailModel.note.imageUrls.clear();
                courseDetailModel.note.imageUrls.addAll(selectedPhotos);
                mAdapter.setData(resultPhotos);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == llBody) {
            Bundle bundle = new Bundle();
            if (courseDetailModel.note.bodyParts != null) {
                bundle.putString("json", String.valueOf(JSON.toJSON(courseDetailModel.note.bodyParts)));
            }
            xStartActivity(CourseTrainPartsOfBodyActivity.class, bundle, 1);
        } else if (v == llSupportType) {
            showDialog(strengthItems);
        } else if (v == publishButton) {
            if (setCourseDetailModelData()) {
                uploadImagesAndCommit(courseDetailModel);
            }
        }
    }

    @Override
    public void addAction() {
        addOneAction(null);
    }

    @Override
    public void deleteAction(ActionItemView actionItemView) {
        if (llActionContainer.getChildCount() == 1) {
            return;
        }
        llActionContainer.removeView(actionItemView);
        int count = llActionContainer.getChildCount();
        if (count >= 1) {
            ((ActionItemView) llActionContainer.getChildAt(count - 1)).setAddActionVisible(true);
        }
    }

    private ActionItemView createActionItemView() {
        ActionItemView actionItemView = new ActionItemView(this);
        actionItemView.setActionClickListener(this);
        actionItemView.addChildView(null);

        return actionItemView;
    }

    private void uploadImagesAndCommit(final CoachCourseDetailResponse model) {

        showLoadingDialog();

        final List<String> imageUrls = model.note.imageUrls;
        if (LangUtils.isNotEmpty(imageUrls)) {
//            final List<String> imageUrls = new ArrayList<>(imagePaths);
            final AtomicInteger succCount = new AtomicInteger();
            final AtomicInteger failCount = new AtomicInteger();

            for (int j = 0; j < imageUrls.size(); j++) {
                String path = imageUrls.get(j);
                if (path.startsWith("//")) {
                    caculateUploadImageSucc(model, null, j, succCount, failCount, true);
                    continue;
                }
                final int positon = j;
                UploadPicFileUtils.uploadXXAssistantImage(path, new UploadPicFileUtils.OnUploadProcessListener() {
                    @Override
                    public void onUploadDone(int responseCode, String message) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = JSON.parseObject(message);
                        } catch (JSONException e) {

                        }
                        String url = jsonObject == null ? null : jsonObject.getString("url");

                        caculateUploadImageSucc(model, url, positon, succCount, failCount, false);
                    }

                    @Override
                    public void onUploadProcess(int uploadSize) {
//                    LogUtils.d("onUploadProcess code %s", uploadSize);
                    }

                    @Override
                    public void initUpload(int fileSize) {

                    }
                });
            }
        } else {
            commitData(model);
        }
    }

    private synchronized void caculateUploadImageSucc(CoachCourseDetailResponse model, String url, int positon, AtomicInteger succCount, AtomicInteger failCount, boolean uploaded) {
        if (uploaded) {
            succCount.addAndGet(1);
        } else {
            boolean succ = LangUtils.isNotEmpty(url);
            if (succ) {
                model.note.imageUrls.set(positon, url);
                succCount.addAndGet(1);
            } else {
                failCount.addAndGet(1);
            }
        }
        if (succCount.get() + failCount.get() >= model.note.imageUrls.size()) {
            if (failCount.get() > 0) {
                ViewUtils.showToast(getString(R.string.no_network_warn), Toast.LENGTH_SHORT);
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                    }
                });
                CardManager.saveFailedDetail(model);
                finish();
            } else {
                commitData(model);
            }
        }
    }


    private void commitData(final CoachCourseDetailResponse model) {

        List<String> imageUrls = model.note.imageUrls;
        UrlRequest r = new UrlRequest("/ic-arrangement/note/" + (isModifyMode ? model.note.id : ""));
        r.addPostParam("coach_id", CardSessionManager.getInstance().getUser().id);
        r.addPostParam("arrangement_id", arrangementId);

        XActionNote note = model.note;

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < note.actions.size(); i++) {

            XAction action = note.actions.get(i);
            JSONObject actionJson = new JSONObject();
            actionJson.put("name", action.name);

            List<XActionItem> items = action.items;
            if (LangUtils.isNotEmpty(items)) {
                JSONArray dataArray = new JSONArray();
                for (XActionItem item : items) {
                    if (item.weight == 0 && item.times == 0 && item.group == 0) {
                        continue;
                    }
                    JSONObject itemJson = new JSONObject();
                    itemJson.put("weight", item.weight);
                    itemJson.put("frequency", item.times);
                    itemJson.put("group", item.group);
                    dataArray.add(itemJson);
                }
                if (dataArray.size() > 0)
                    actionJson.put("data", dataArray);
            }
            if (LangUtils.isNotEmpty(action.name) && actionJson.containsKey("data")) {
                jsonArray.add(actionJson);
            }

        }
        if (jsonArray.size() > 0) {
            r.addPostParam("actions", jsonArray.toString());//
        }

        if (note.bodyParts != null && note.bodyParts.size() > 0)
            r.addPostParam("body_parts", JSON.toJSON(note.bodyParts));

        if (LangUtils.isNotEmpty(imageUrls)) {
            StringBuilder sb = new StringBuilder();
            for (String img : imageUrls) {
                sb.append(img);
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            r.addPostParam("imgs", sb.toString());
        }
        if (LangUtils.isNotEmpty(note.desc)) {
            r.addPostParam("desc", note.desc);
        }

        r.addPostParam("strength", note.strength);
        if (note.method > 0) {
            r.addPostParam("method", note.method);
        }

        if (LangUtils.isNotEmpty(note.aerobicTraining)) {
            r.addPostParam("aerobic_exercise", note.aerobicTraining);
        }

        r.addPostParam("remain_minutes", note.remainMinutes);


        r.addPostParam("distance", note.distance);


        r.addPostParam("speed_resistance", note.speed);

        r.addPostParam("calories", note.calories);
        r.setDelegate(new RequestDelegate() {
            @Override
            public void requestFailed(UrlRequest request, int statusCode, String errorString) {
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        ViewUtils.showToast(getString(isModifyMode ? R.string.coach_course_modify_fail : R.string.coach_course_upload_fail), Toast.LENGTH_SHORT);
                        CardManager.saveFailedDetail(model);
                        finish();
                    }
                });
            }

            @Override
            public void requestFinished(UrlRequest request) {
                JSONObject json = request.getResponseJsonObject();
                int code = json.getIntValue("errcode");
                if (code == 0) {
                    ViewUtils.showToast(getString(isModifyMode ? R.string.coach_course_modify_success : R.string.coach_course_upload_success), Toast.LENGTH_SHORT);
                    if (isFromDraft) {
                        CardManager.clearFailedDetail();
                    }
                    setResult(RESULT_OK);
                } else {
                    ViewUtils.showToast(getString(isModifyMode ? R.string.coach_course_modify_fail : R.string.coach_course_upload_fail), Toast.LENGTH_SHORT);
                    CardManager.saveFailedDetail(model);
                }
                ViewUtils.runInHandlerThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissLoadingDialog();
                        finish();

                    }
                });
            }
        });

        NetworkParam param = new NetworkParam();
        param.service = new IService() {
            @Override
            public String getUrl() {
                return null;
            }

            @Override
            public int getRequestType() {
                return isModifyMode ? Request.Method.PUT : Request.Method.POST;
            }

            @Override
            public Class<? extends BaseResult> getResultClazz() {
                return null;
            }
        };
        r.setNetworkParam(param);
        r.start();
    }

    private static final int MAX_ITEM_SIZE = 7;
    private static final int ITEM_HEIGHT = ViewUtils.rp(40);

    private void showDialog(List<String> items) {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.DialogTheme).create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.shareDiaLogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        dialog.setContentView(R.layout.ui_choose_gym);
        dialogWindow.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ListView llContent = (ListView) dialog.findViewById(R.id.ll_content);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        addItems(dialog, items, llContent);
    }

    private void addItems(final Dialog dialog, final List<String> items, ListView llContent) {
        if (LangUtils.isEmpty(items)) {
            return;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llContent.getLayoutParams();
        if (items.size() > MAX_ITEM_SIZE) {
            params.height = MAX_ITEM_SIZE * ITEM_HEIGHT + ViewUtils.rp(MAX_ITEM_SIZE / 2);
        }
        llContent.setLayoutParams(params);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_gym_name, items);
        llContent.setAdapter(adapter);
        llContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinishing()) {
                    return;
                }

                dialog.dismiss();
                tvSportType.setText(items.get(position));
                sportType = items.get(position);
                tvSportType.setTag(position + 1);
                tvSportType.setTextColor(getResources().getColor(R.color.text_color_gray_66));
            }
        });
    }

    private void updatePartText() {
        StringBuilder sb = new StringBuilder();
        HashMap<String, List<String>> parts = courseDetailModel.note.bodyParts;
        if (parts != null && parts.size() > 0) {

            Set<String> keys = parts.keySet();
            for (String key : keys) {
                List<String> array = parts.get(key);
                if (array != null && array.size() > 0) {
                    sb.append(key);
                    sb.append("-");
                    for (int j = 0; j < array.size(); j++) {
                        String value = array.get(j);
                        sb.append(value);
                        sb.append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(";");
                }

            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        tvBody.setText(sb.toString());
    }

    private boolean setCourseDetailModelData() {
        XActionNote note = courseDetailModel.note;
        if (courseDetailModel.note.bodyParts == null || courseDetailModel.note.bodyParts.size() <= 0 || LangUtils.isEmpty(descEdit.getText())) {
//            ViewUtils.showToast(getString(R.string.coach_course_parts_empty), Toast.LENGTH_SHORT);
            showEmptyAlertDialog(R.string.coach_course_parts_empty);
            return false;
        }
        if (note.actions == null) {
            note.actions = new ArrayList<>();
        } else {
            note.actions.clear();
        }
        for (int i = 0; i < llActionContainer.getChildCount(); i++) {
            ActionItemView view = (ActionItemView) llActionContainer.getChildAt(i);
            XAction action = view.getData();
            if (LangUtils.isEmpty(action.items)) {
                continue;
            }
            note.actions.add(action);
        }
        if (LangUtils.isEmpty(note.actions)) {
//            ViewUtils.showToast(getString(R.string.coach_course_action_empty), Toast.LENGTH_SHORT);
            showEmptyAlertDialog(R.string.coach_course_action_empty);
            return false;
        }

        if (!LangUtils.isEmpty(sportType)) {
            note.method = (int) tvSportType.getTag();
        }

        String strength = edtSportWeight.getEditableText().toString();
        note.strength = LangUtils.parseInt(strength, 0);

        note.aerobicTraining = edtOxygenSport.getEditableText().toString();
        note.desc = descEdit.getEditableText().toString();

        String timeString = editTime.getEditableText().toString();
        note.remainMinutes = LangUtils.parseInt(timeString, 0);

        String disString = edtDistance.getEditableText().toString();
        note.distance = LangUtils.parseInt(disString, 0);

        String speedString = edtSpace.getEditableText().toString();
        note.speed = LangUtils.parseInt(speedString, 0);

        String calString = edtCalorie.getEditableText().toString();
        note.calories = LangUtils.parseInt(calString, 0);

        courseDetailModel.id = arrangementId;
        return true;
    }

    private void showEmptyAlertDialog(int rid) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(R.string.coach_course_alert_title).setMessage(rid).setPositiveButton(R.string.yes, null).show();
    }

    private void addActions(List<XAction> actions) {
        if (LangUtils.isNotEmpty(actions)) {
            //草稿箱中必然有action，否则没法存储
            for (int i = 0; i < actions.size(); i++) {
                XAction action = actions.get(i);
                if (i == 0) {
                    ActionItemView view = (ActionItemView) llActionContainer.getChildAt(0);
                    view.setData(action);
                } else {
                    addOneAction(action);
                }
            }
        } else {
            //不是草稿箱，新页面
            if (llActionContainer.getChildCount() > 0 && llActionContainer.getChildAt(0) instanceof ActionItemView) {
                ActionItemView itemView = (ActionItemView) llActionContainer.getChildAt(0);
                if(itemView.getActionItemViewCount() == 0) {
                    itemView.addChildView(null);
                }
            }
        }
    }

    private void addOneAction(XAction action) {
        ActionItemView actionItemView = createActionItemView();
        if (action != null)
            actionItemView.setData(action);
        int count = llActionContainer.getChildCount();
        if (count >= 1) {
            ((ActionItemView) llActionContainer.getChildAt(count - 1)).setAddActionVisible(false);
        }
        llActionContainer.addView(actionItemView);
    }
}
