package com.kuaipao.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.kuaipao.base.BaseActivity;
import com.kuaipao.base.inject.From;
import com.kuaipao.manager.R;
import com.kuaipao.ui.customer.CustomerTypeAdapter;
import com.kuaipao.ui.view.NoScrollGridView;
import com.kuaipao.utils.LangUtils;
import com.kuaipao.utils.LogUtils;
import com.kuaipao.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MVEN on 16/9/21.
 * <p/>
 * email: magiwen@126.com.
 */
public class CourseTrainPartsOfBodyActivity extends BaseActivity {

//    public static final String[] PECTORALIS_ITEMS = {"上束", "中束", "下束"};
//    public static final String[] BACK_ITEMS = {"背阔肌", "大圆肌", "上束斜方肌", "中束斜方肌", "下束斜方肌", "大菱形肌", "小菱形肌"};
//    public static final String[] ARM_ITEMS = {"肱肌", "肱桡肌", "肱屈肌", "肱伸肌", "肱二头肌", "肱三头肌"};
//    public static final String[] DELTOID_ITEMS = {"前束", "中束", "后束"};
//    public static final String[] ABDOMINAL_ITEMS = {"腹直肌", "腹外斜肌"};
//    public static final String[] LEG_ITEMS = {"股四头肌", "蝈绳肌", "比目鱼肌", "腓肠肌"};
//    public static final String[] HIP_ITEMS = {"臂中肌", "臂小肌", "臂大肌"};


    private static final int[] PART_ITEM_IDS = {R.string.training_part_pectoralis, R.string.training_part_back, R.string.training_part_arm,
            R.string.training_part_deltoid, R.string.training_part_abdominal, R.string.training_part_leg, R.string.training_part_hip};

    private TextView tvSubmit;

    @From(R.id.gv_ectopectoralis)
    private NoScrollGridView gvEctopectoralis;

    @From(R.id.gv_back)
    private NoScrollGridView gvBack;

    @From(R.id.gv_arm)
    private NoScrollGridView gvArm;

    @From(R.id.gv_deltoid)
    private NoScrollGridView gvDeltoid;

    @From(R.id.gv_belly)
    private NoScrollGridView gvBelly;

    @From(R.id.gv_leg)
    private NoScrollGridView gvLeg;

    @From(R.id.gv_buttocks)
    private NoScrollGridView gvButtocks;

    private ArrayList<String> ectopectoraliesItems;
    private TrainPartsAdapter ectopectoraliesAdapter;
    private List<String> ectopectoralies = new ArrayList<>();

    private ArrayList<String> backItems;
    private TrainPartsAdapter backAdapter;
    private List<String> back = new ArrayList<>();

    private TrainPartsAdapter armAdapter;
    private ArrayList<String> armItems;
    private List<String> arm = new ArrayList<>();

    private TrainPartsAdapter deltoidAdapter;
    private ArrayList<String> deltoidItems;
    private List<String> deltoid = new ArrayList<>();

    private TrainPartsAdapter bellyAdapter;
    private ArrayList<String> bellyItems;
    private List<String> belly = new ArrayList<>();

    private TrainPartsAdapter legAdapter;
    private ArrayList<String> legItems;
    private List<String> leg = new ArrayList<>();

    private TrainPartsAdapter buttocksAdapter;
    private ArrayList<String> buttocksItems;
    private List<String> buttocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_course_train_part);
        tvSubmit = ViewUtils.createTitleBarRightTextView(this, "确定");
        setTitle("训练部位", true, tvSubmit);

        initUI();
    }

    @Override
    public void onClick(View v) {
        if (v == tvSubmit) {
            Intent intent = new Intent();
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < PART_ITEM_IDS.length; i++) {
                String key = getString(PART_ITEM_IDS[i]);
                switch (i) {
                    case 0:
                        if (LangUtils.isNotEmpty(ectopectoralies))
                            jsonObject.put(key, JSON.toJSON(ectopectoralies));
                        break;
                    case 1:
                        if (LangUtils.isNotEmpty(back))
                            jsonObject.put(key, JSON.toJSON(back));
                        break;
                    case 2:
                        if (LangUtils.isNotEmpty(arm))
                            jsonObject.put(key, JSON.toJSON(arm));
                        break;
                    case 3:
                        if (LangUtils.isNotEmpty(deltoid))
                            jsonObject.put(key, JSON.toJSON(deltoid));
                        break;
                    case 4:
                        if (LangUtils.isNotEmpty(belly))
                            jsonObject.put(key, JSON.toJSON(belly));
                        break;
                    case 5:
                        if (LangUtils.isNotEmpty(leg))
                            jsonObject.put(key, JSON.toJSON(leg));
                        break;
                    case 6:
                        if (LangUtils.isNotEmpty(buttocks))
                            jsonObject.put(key, JSON.toJSON(buttocks));
                        break;
                    default:
                        break;
                }
            }
            intent.putExtra("json", jsonObject.toString());
            setResult(RESULT_OK, intent);
            finish();


        }
    }

    private void initUI() {
        tvSubmit.setOnClickListener(this);

        ectopectoraliesItems = new ArrayList<>();
        ectopectoraliesItems.add("上束");
        ectopectoraliesItems.add("中束");
        ectopectoraliesItems.add("下束");
        ectopectoraliesAdapter = new TrainPartsAdapter(this, ectopectoraliesItems);
        gvEctopectoralis.setAdapter(ectopectoraliesAdapter);
        gvEctopectoralis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ectopectoraliesAdapter.setSelectedPosition(position);
                String selected = ectopectoraliesItems.get(position);
                if (ectopectoralies.contains(selected)) {
                    ectopectoralies.remove(selected);
                } else {
                    ectopectoralies.add(selected);
                }
                ectopectoraliesAdapter.notifyDataSetChanged();
            }
        });

        backItems = new ArrayList<>();
        backItems.add("背阔肌");
        backItems.add("大圆肌");
        backItems.add("上束斜方肌");
        backItems.add("中束斜方肌");
        backItems.add("下束斜方肌");
        backItems.add("大菱形肌");
        backItems.add("小菱形肌");
        backAdapter = new TrainPartsAdapter(this, backItems);
        gvBack.setAdapter(backAdapter);
        gvBack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backAdapter.setSelectedPosition(position);
                String selected = backItems.get(position);
                if (back.contains(selected)) {
                    back.remove(selected);
                } else {
                    back.add(selected);
                }
                bellyAdapter.notifyDataSetChanged();
            }
        });

        armItems = new ArrayList<>();
        armItems.add("肱肌");
        armItems.add("肱桡肌");
        armItems.add("肱屈肌");
        armItems.add("肱伸肌");
        armItems.add("肱二头肌");
        armItems.add("肱三头肌");
        armAdapter = new TrainPartsAdapter(this, armItems);
        gvArm.setAdapter(armAdapter);
        gvArm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                armAdapter.setSelectedPosition(position);
                String selected = armItems.get(position);
                if (arm.contains(selected)) {
                    arm.remove(selected);
                } else {
                    arm.add(selected);
                }
                armAdapter.notifyDataSetChanged();
            }
        });

        deltoidItems = new ArrayList<>();
        deltoidItems.add("前束");
        deltoidItems.add("中束");
        deltoidItems.add("后束");
        deltoidAdapter = new TrainPartsAdapter(this, deltoidItems);
        gvDeltoid.setAdapter(deltoidAdapter);
        gvDeltoid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deltoidAdapter.setSelectedPosition(position);

                String selected = deltoidItems.get(position);
                if (deltoid.contains(selected)) {
                    deltoid.remove(selected);
                } else {
                    deltoid.add(selected);
                }
                deltoidAdapter.notifyDataSetChanged();
            }
        });

        bellyItems = new ArrayList<>();
        bellyItems.add("腹直肌");
        bellyItems.add("腹外斜肌");
        bellyAdapter = new TrainPartsAdapter(this, bellyItems);
        gvBelly.setAdapter(bellyAdapter);
        gvBelly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bellyAdapter.setSelectedPosition(position);
                String selected = bellyItems.get(position);
                if (belly.contains(selected)) {
                    belly.remove(selected);
                } else {
                    belly.add(selected);
                }
                bellyAdapter.notifyDataSetChanged();
            }
        });

        legItems = new ArrayList<>();
        legItems.add("股四头肌");
        legItems.add("蝈绳肌");
        legItems.add("比目鱼肌");
        legItems.add("腓肠肌");
        legAdapter = new TrainPartsAdapter(this, legItems);
        gvLeg.setAdapter(legAdapter);
        gvLeg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                legAdapter.setSelectedPosition(position);
                String selected = legItems.get(position);
                if (leg.contains(selected)) {
                    leg.remove(selected);
                } else {
                    leg.add(selected);
                }
                legAdapter.notifyDataSetChanged();
            }
        });

        buttocksItems = new ArrayList<>();
        buttocksItems.add("臂中肌");
        buttocksItems.add("臂小肌");
        buttocksItems.add("臂大肌");
        buttocksAdapter = new TrainPartsAdapter(this, buttocksItems);
        gvButtocks.setAdapter(buttocksAdapter);
        gvButtocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buttocksAdapter.setSelectedPosition(position);
                String selected = buttocksItems.get(position);
                if (buttocks.contains(selected)) {
                    buttocks.remove(selected);
                } else {
                    buttocks.add(selected);
                }
                buttocksAdapter.notifyDataSetChanged();
            }
        });

        String jsonString = getIntent().getStringExtra("json");
        JSONObject json = null;

        try {
            json = JSON.parseObject(jsonString);
        } catch (JSONException e) {

        }

        if (json != null && json.size() > 0) {
            for (int i = 0; i < PART_ITEM_IDS.length; i++) {
                String key = getString(PART_ITEM_IDS[i]);
                if (json.containsKey(key)) {
                    String value = json.getString(key);
                    switch (i) {
                        case 0:
                            ectopectoralies = JSON.parseArray(value, String.class);
                            for (String s : ectopectoralies) {
                                ectopectoraliesAdapter.setSelectedPosition(ectopectoraliesItems.indexOf(s));
                            }
                            break;
                        case 1:
                            back = JSON.parseArray(value, String.class);
                            for (String s : back) {
                                backAdapter.setSelectedPosition(backItems.indexOf(s));
                            }
                            break;
                        case 2:
                            arm = JSON.parseArray(value, String.class);
                            for (String s : arm) {
                                armAdapter.setSelectedPosition(armItems.indexOf(s));
                            }
                            break;
                        case 3:
                            deltoid = JSON.parseArray(value, String.class);
                            for (String s : deltoid) {
                                deltoidAdapter.setSelectedPosition(deltoidItems.indexOf(s));
                            }
                            break;
                        case 4:
                            belly = JSON.parseArray(value, String.class);
                            for (String s : belly) {
                                bellyAdapter.setSelectedPosition(bellyItems.indexOf(s));
                            }
                            break;
                        case 5:
                            leg = JSON.parseArray(value, String.class);
                            for (String s : leg) {
                                legAdapter.setSelectedPosition(legItems.indexOf(s));
                            }
                            break;
                        case 6:
                            buttocks = JSON.parseArray(value, String.class);
                            for (String s : buttocks) {
                                buttocksAdapter.setSelectedPosition(buttocksItems.indexOf(s));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

}
