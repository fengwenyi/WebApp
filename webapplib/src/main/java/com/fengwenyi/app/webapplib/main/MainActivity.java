package com.fengwenyi.app.webapplib.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengwenyi.app.applib.dialog.LoadingDialog;
import com.fengwenyi.app.applib.tool.ToastUtil;
import com.fengwenyi.app.webapplib.R;
import com.fengwenyi.app.webapplib.base.BaseActivity;
import com.fengwenyi.app.webapplib.tool.Constant;
import com.fengwenyi.app.webapplib.tool.WebCameraHelper;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * @author Wenyi Feng
 */
public class MainActivity extends BaseActivity {

    private BridgeWebView mWebView;
    private LoadingDialog loadingDialog;
    private RefreshLayout refreshLayout;
    private TextView tvFail;
    private ImageView imgBack;

    private long exitTime = 0;
    private String URL;
    private boolean isSuccess = false;
    private boolean isError = false;

    @Override
    public void init() {

        initView();

        //显示H5页面
        // mWebView.loadUrl(URL);
        loadWebView();

        dropDownRefresh ();
    }

    @Override
    public Integer getLayout() {
        return R.layout.activity_lib_main;
    }

    @Override
    public void click() {
        back ();
    }

    // initView
    private void initView () {

        loading();

        //初始化BridgeWebView
        mWebView = findViewById(R.id.webView);

        refreshLayout = findViewById(R.id.refreshLayout);
        tvFail = findViewById(R.id.tvFail);

        imgBack = findViewById(R.id.appBack);

        Intent intent = getIntent();
        URL = intent.getStringExtra(Constant.KEY_INTENT_URL);
    }

    // loading show
    private void loading () {
        LoadingDialog.Builder loadBuilder = new LoadingDialog.Builder(mContext)
                .setShowMsg(true)
                .setMsg("正在载入");
        loadingDialog = loadBuilder.create();
        loadingDialog.show();
    }

    // loading dismiss
    private void loadingDismiss () {
        loadingDialog.dismiss();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadWebView () {

        mWebView.loadUrl(URL);

        mWebView.setDefaultHandler(new DefaultHandler());
        mWebView.setWebViewClient(new BridgeWebViewClient(mWebView) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                assert url != null;
                if (url.startsWith("mailto:")
                        || url.startsWith("geo:")
                        || url.startsWith("tel:")
                        || url.startsWith("weixin://wap/pay?")
                        || url.startsWith("alipays://platformapi/startApp?")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(url));
                    mActivity.startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }

                if (!isError) {
                    isSuccess = true;
                    // LogUtil.d("Success");
                    //回调成功后的相关操作
                    mWebView.setVisibility(View.VISIBLE);
                    tvFail.setVisibility(View.GONE);
                    tvFail.setText(mContext.getResources().getText(R.string.app_lib_loading));
                }
                isError = false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                isError = true;
                isSuccess = false;
               //  LogUtil.d("fail");
                //回调失败的相关操作
                mWebView.setVisibility(View.GONE);
                tvFail.setVisibility(View.VISIBLE);
                tvFail.setText(mContext.getResources().getText(R.string.app_lib_error_net));
            }

            /*@Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                isError = true;
                isSuccess = false;
                // LogUtil.d("fail");
                //回调失败的相关操作
                mWebView.setVisibility(View.GONE);
                tvFail.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }*/
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
                WebCameraHelper.getInstance().showOptions(mActivity);
            }

            // For Android > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
                WebCameraHelper.getInstance().showOptions(mActivity);
            }

            // For Android > 5.0支持多张上传
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
                WebCameraHelper.getInstance().mUploadCallbackAboveL = uploadMsg;
                WebCameraHelper.getInstance().showOptions(mActivity);
                return true;
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 95) {
                    loadingDismiss();
                }
                if (mWebView.canGoBack()) {
                    imgBack.setVisibility(View.VISIBLE);
                } else {
                    imgBack.setVisibility(View.GONE);
                }
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("utf-8");
    }

    // 下拉刷新
    public void dropDownRefresh () {

        refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                // refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败

                URL = mWebView.getUrl();

                loadWebView();

                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(mContext, "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        }

        return false;
    }

    public void back () {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });
    }
}
