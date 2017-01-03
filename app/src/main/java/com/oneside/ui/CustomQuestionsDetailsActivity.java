package com.oneside.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oneside.base.BaseActivity;
import com.oneside.utils.Constant;
import com.oneside.utils.SysUtils;
import com.oneside.utils.ViewUtils;
import com.oneside.R;

public class CustomQuestionsDetailsActivity extends BaseActivity {

    private LinearLayout mQALayout_1, mQALayout_2, mQALayout_3, mQALayout_4, mQALayout_5,
            mQALayout_6;
    private LinearLayout mQLayout1, mQLayout2, mQLayout3, mQLayout4;
    private TextView t1_1, t1_2, t2_1, t2_2, t3_1, t3_2, t4_1, t4_2, t5_1, t5_2, t6_1, t6_2;
    private ImageView img_1, img_2;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_questions_details);
        Intent intent = getIntent();
        int a = intent.getIntExtra(Constant.ACTIVITY_QUESTIONS_CHOSE_POSITION, 3);
        mQLayout1 = (LinearLayout) findViewById(R.id.questions_layout_1);
        mQLayout2 = (LinearLayout) findViewById(R.id.questions_layout_2);
        mQLayout3 = (LinearLayout) findViewById(R.id.questions_layout_3);
        mQLayout4 = (LinearLayout) findViewById(R.id.questions_layout_4);
        setTitle("", true);
        initQuestionsViews(a);
    }

    private void initQuestionsViews(int a) {
        switch (a) {
            case 0:
                setTitle(getString(R.string.customer_question_answer));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three);
                mQALayout_4 = ViewUtils.find(this, R.id.quetions_four);
                mQALayout_5 = ViewUtils.find(this, R.id.quetions_five);
                mQALayout_6 = ViewUtils.find(this, R.id.quetions_six);
                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                t2_1.setText(getString(R.string.customer_question_two_title));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(getString(R.string.customer_question_two_answer));
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer));
                t4_1 = (TextView) mQALayout_4.findViewById(R.id.questions_title);
                t4_1.setText(getString(R.string.customer_question_four_title));
                t4_2 = (TextView) mQALayout_4.findViewById(R.id.questions_answer);
                t4_2.setText(getString(R.string.customer_question_four_answer));
                t5_1 = (TextView) mQALayout_5.findViewById(R.id.questions_title);
                t5_1.setText(getString(R.string.customer_question_five_title));
                t5_2 = (TextView) mQALayout_5.findViewById(R.id.questions_answer);
                t5_2.setText(getString(R.string.customer_question_five_answer));
                t6_1 = (TextView) mQALayout_6.findViewById(R.id.questions_title);
                t6_1.setText(getString(R.string.customer_question_six_title));
                t6_2 = (TextView) mQALayout_6.findViewById(R.id.questions_answer);
                t6_2.setText(getString(R.string.customer_question_six_answer));
                mQLayout1.setVisibility(View.VISIBLE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.GONE);
                break;
            case 1:
                setTitle(getString(R.string.customer_question_counpon));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one1);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two1);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three1);
                mQALayout_4 = ViewUtils.find(this, R.id.quetions_four1);
                mQALayout_5 = ViewUtils.find(this, R.id.quetions_five1);
                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title1));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer1));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                t2_1.setText(getString(R.string.customer_question_two_title1));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(getString(R.string.customer_question_two_answer1));
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title1));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer1));
                t4_1 = (TextView) mQALayout_4.findViewById(R.id.questions_title);
                t4_1.setText(getString(R.string.customer_question_four_title1));
                t4_2 = (TextView) mQALayout_4.findViewById(R.id.questions_answer);
                t4_2.setText(getString(R.string.customer_question_four_answer1));
                t5_1 = (TextView) mQALayout_5.findViewById(R.id.questions_title);
                t5_1.setText(getString(R.string.customer_question_five_title1));
                t5_2 = (TextView) mQALayout_5.findViewById(R.id.questions_answer);
                t5_2.setText(getString(R.string.customer_question_five_answer1));
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.VISIBLE);
                mQLayout3.setVisibility(View.GONE);
                break;
            case 2:
                setTitle(getString(R.string.customer_question_account_balance));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one2);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two2);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three2);
                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title2));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer2));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                t2_1.setText(getString(R.string.customer_question_two_title2));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(getString(R.string.customer_question_two_answer2));
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title2));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer2));
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.VISIBLE);
                break;
            case 3:
                setTitle(getString(R.string.customer_question_xxer_circle));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one3);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two3);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three3);
                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title3));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer3));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                String str = getString(R.string.customer_question_two_answer3);
                String inc = getString(R.string.customer_questions_two_span);
                SpannableString span = new SpannableString(str);
                int imgStart = str.indexOf("2-4") - 1;
                Drawable d = getResources().getDrawable(R.drawable.ic_account_explain);
                d.setBounds(0, 0, SysUtils.WIDTH, 100);
                int start = str.indexOf(inc);
                int end = start + inc.length();
                span.setSpan(new ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ImageSpan(this, R.drawable.ic_account_explain), imgStart, imgStart + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                img_1 = (ImageView) mQALayout_2.findViewById(R.id.questions_img);
                img_1.setVisibility(View.VISIBLE);
                img_1.setImageResource(R.drawable.ic_account_explain);
                t2_1.setText(getString(R.string.customer_question_two_title3));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(span);
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title3));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer3));
                img_2 = (ImageView) mQALayout_3.findViewById(R.id.questions_img);
                img_2.setImageResource(R.drawable.ic_account_explain);
                img_2.setVisibility(View.VISIBLE);
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.GONE);
                mQLayout4.setVisibility(View.VISIBLE);
                break;
            case 4:
                setTitle(getString(R.string.customer_question_title4));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one3);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two3);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three3);

                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title4));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer4));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                t2_1.setText(getString(R.string.customer_question_two_title4));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(getString(R.string.customer_question_two_answer4));
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title4));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer4));
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.GONE);
                mQLayout4.setVisibility(View.VISIBLE);
                break;
            case 5:
                setTitle(getString(R.string.customer_question_title5));
                mQALayout_1 = ViewUtils.find(this, R.id.quetions_one3);
                mQALayout_2 = ViewUtils.find(this, R.id.quetions_two3);
                mQALayout_3 = ViewUtils.find(this, R.id.quetions_three3);

                t1_1 = (TextView) mQALayout_1.findViewById(R.id.questions_title);
                t1_1.setText(getString(R.string.customer_question_one_title5));
                t1_2 = (TextView) mQALayout_1.findViewById(R.id.questions_answer);
                t1_2.setText(getString(R.string.customer_question_one_answer5));
                t2_1 = (TextView) mQALayout_2.findViewById(R.id.questions_title);
                t2_1.setText(getString(R.string.customer_question_two_title5));
                t2_2 = (TextView) mQALayout_2.findViewById(R.id.questions_answer);
                t2_2.setText(getString(R.string.customer_question_two_answer5));
                t3_1 = (TextView) mQALayout_3.findViewById(R.id.questions_title);
                t3_1.setText(getString(R.string.customer_question_three_title5));
                t3_2 = (TextView) mQALayout_3.findViewById(R.id.questions_answer);
                t3_2.setText(getString(R.string.customer_question_three_answer5));
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.GONE);
                mQLayout4.setVisibility(View.VISIBLE);
                break;
            default:
                mQLayout1.setVisibility(View.GONE);
                mQLayout2.setVisibility(View.GONE);
                mQLayout3.setVisibility(View.GONE);
                break;
        }
    }

}
