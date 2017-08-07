package com.oneside.ui.study;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.oneside.R;
import com.oneside.base.BaseActivity;
import com.oneside.base.inject.From;
import com.oneside.utils.LogUtils;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class RxStudyActivity extends BaseActivity {
    @From(R.id.tv_temp)
    private TextView textView;

    @From(R.id.btn_rx)
    private Button btnRx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_study);

        btnRx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        if (v == btnRx) {
            doSomeWork();
        }
    }

    /**
     * simple example using Flowable
     */
    private void doSomeWork() {

        Flowable<Integer> observable = Flowable.just(1, 2, 3, 4);
        observable.reduce(50, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer t1, Integer t2) {
                return t1 + t2;
            }
        }).subscribe(getObserver());

    }

    private SingleObserver<Integer> getObserver() {

        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtils.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onSuccess(Integer value) {
                textView.append(" onSuccess : value : " + value);
                textView.append("\r\n");
                LogUtils.d(TAG, " onSuccess : value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append("\n");
                LogUtils.d(TAG, " onError : " + e.getMessage());
            }
        };
    }
}
