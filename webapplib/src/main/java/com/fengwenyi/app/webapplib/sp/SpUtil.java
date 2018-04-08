package com.fengwenyi.app.webapplib.sp;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author Wenyi Feng
 */
public class SpUtil {

    /* DB */
    private static final String DB_STARTUP_MODE = "xfsy_sp_db_startup_mode";

    /* TABLE */
    private static final String KEY_STARTUP_MODE_LEVEL = "xfsy_sp_key_mode";

    /* startup mode */

    // 保存
    public static void saveStartupMode (Context context, int status) {
        SharedPreferences sp = context.getSharedPreferences(DB_STARTUP_MODE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_STARTUP_MODE_LEVEL, status);
        editor.apply();
    }

    // 读取
    public static int queryStartupMode (Context context) {
        SharedPreferences sp = context.getSharedPreferences(DB_STARTUP_MODE, MODE_PRIVATE);
        return sp.getInt(KEY_STARTUP_MODE_LEVEL, 0);
    }

    // 清除
    public static void deleteStartupMode (Context context) {
        SharedPreferences sp = context.getSharedPreferences(DB_STARTUP_MODE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
