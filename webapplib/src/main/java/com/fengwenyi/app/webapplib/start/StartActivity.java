package com.fengwenyi.app.webapplib.start;

import android.content.Intent;

import com.fengwenyi.app.webapplib.R;
import com.fengwenyi.app.webapplib.base.BaseActivity;
import com.fengwenyi.app.webapplib.sp.SpUtil;
import com.fengwenyi.app.webapplib.startup.StartupActivity;
import com.fengwenyi.app.webapplib.tool.Constant;


/**
 * App 入口
 * 两种方案:
 *    App入口  启动页  引导页  主页
 * 1、start -> Startup->[]->main
 * 2、start     ->      []->main
 * @author Wenyi Feng
 */
public abstract class StartActivity extends BaseActivity {

    @Override
    public void init() {

        // 判断是否进入启动页
//        int openMode = startupMode();
        int openMode = 1;
        Intent intent;
        switch (openMode) {
            case 0:
                // 0 : 安装
                //     start->startup->guide->main
                intent = new Intent(mContext, StartupActivity.class);
                intent.putExtra(Constant.KEY_STARTUP_MODE, 0);
                break;

            case 1:
                // 1 : 打开
                //     start->startup->main
                intent = new Intent(mContext, StartupActivity.class);
                intent.putExtra(Constant.KEY_STARTUP_MODE, 1);
                break;

            case 2:
                // 2 : 禁用启动页
                //     start->main
                intent = new Intent(mContext, StartupActivity.class);
                intent.putExtra(Constant.KEY_STARTUP_MODE, 2);
                break;

            default:
                // 1 : 打开
                //     start->startup->main
                intent = new Intent(mContext, StartupActivity.class);
                intent.putExtra(Constant.KEY_STARTUP_MODE, 3);
                break;
        }
        intent.putExtra(Constant.KEY_INTENT_URL, setUrl());
        startActivity(intent);
        finish();
    }

    @Override
    public Integer getLayout() {
        return R.layout.activity_lib_start;
    }

    @Override
    public void click() {

    }

    /**
     * 获取启动方式
     * @return
     */
    private int startupMode () {

        int openMode = SpUtil.queryStartupMode(mContext);

        if (openMode == 0) {
            SpUtil.saveStartupMode(mContext, 1);
        }

        // 0 : 安装
        //     start->startup->guide->main

        // 1 : 打开
        //     start->startup->main

        // 2 : 禁用启动页
        //     start->main

        return openMode;
    }

    //
    public abstract String setUrl ();
}
