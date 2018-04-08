package com.fengwenyi.app.webapp;

import com.fengwenyi.app.webapplib.start.StartActivity;

public class MActivity extends StartActivity {

    String url = "http://www.baidu.com";

    @Override
    public String setUrl() {
        return url;
    }
}
