package com.fengwenyi.app.webapplib.startup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.fengwenyi.app.applib.tool.ToastUtil;
import com.fengwenyi.app.webapplib.R;
import com.fengwenyi.app.webapplib.base.BaseActivity;
import com.fengwenyi.app.webapplib.guide.GuideActivity;
import com.fengwenyi.app.webapplib.main.MainActivity;
import com.fengwenyi.app.webapplib.tool.Constant;

/**
 * @author Wenyi Feng
 */
public class StartupActivity extends BaseActivity {

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private AlertDialog dialog;

    private int REQUEST_CODE = 180;

    private Handler mHandler = new Handler();

    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Override
    public void init() {

        // 申请权限
        sysPermission();

        imageView = findViewById(R.id.imgView);

        setXml2FrameAnim1();

        //当计时结束时，跳转至主界面
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();
                int mode = intent.getIntExtra(Constant.KEY_STARTUP_MODE, 0);
//                String url = intent.getStringExtra(Constant.KEY_INTENT_URL);

                switch (mode) {
                    case 0:
                        intent.setClass(mContext, GuideActivity.class);
                        break;
                    default:
                        intent.setClass(mContext, MainActivity.class);
                        break;
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public Integer getLayout() {
        return R.layout.activity_lib_startup;
    }

    @Override
    public void click() {

    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int x : grantResults) {
                    if (x != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        for (String permission : permissions) {
                            boolean b = shouldShowRequestPermissionRationale(permission);
                            if (!b) {
                                // 用户还是想用我的 APP 的
                                // 提示用户去应用设置界面手动开启权限
                                showDialogTipUserGoToAppSettting();
                            } else
                                finish();
                        }
                    } else {
                        ToastUtil.show(mContext, "权限获取成功");
                    }
                }
            }
        }
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, REQUEST_CODE);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, permission);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        ToastUtil.show(mContext, "权限获取成功");
                    }
                }
            }
        }
    }

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，允许使用权限")
                .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 系统权限
    private void sysPermission () {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (String permission : permissions) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permission);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    // showDialogTipUserRequestPermission();
                    startRequestPermission();
                }
            }
        }
    }

    /**
     * 通过XML添加帧动画方法一
     */
    private void setXml2FrameAnim1() {

        // 把动画资源设置为imageView的背景,也可直接在XML里面设置
//        imageView.setBackgroundResource(R.drawable.start_loading);
//        animationDrawable = (AnimationDrawable) imageView.getBackground();
//        animationDrawable.start();
    }
}
