package com.fengwenyi.app.webapplib.guide;

import android.content.Intent;
import android.os.Handler;

import com.fengwenyi.app.webapplib.R;
import com.fengwenyi.app.webapplib.base.BaseActivity;
import com.fengwenyi.app.webapplib.main.MainActivity;

/**
 * @author Wenyi Feng
 */
public class GuideActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    public void init() {

        //当计时结束时，跳转至主界面
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                intent.setClass(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public Integer getLayout() {
        return R.layout.activity_lib_guide;
    }

    @Override
    public void click() {

    }
}
