package com.example.gcls.prj_demo;

/**
 * Created by samue_000 on 12/24/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil
{

    /**
     * 通用分享文件名
     */
    private final String commonShareName = "SmartCard";

    private Context ctx;

    private SharedPreferences sharedPreferences;

    public interface Key{
        public final static String GESTURE_PWD_TAG = "gesture_pwd_tag";
    }
    /**
     * <默认构造函数>
     */
    public SharedPreferencesUtil(final Context ctx)
    {
        this.ctx = ctx;
        sharedPreferences = this.ctx.getSharedPreferences(commonShareName, Activity.MODE_PRIVATE);
    }

    /**
     * 保存键值 包含事务，如果一次要保存多值不建议使用，性能有损耗
     *
     * @param key 保存的键
     * @param value 保存的值
     * @see [类、类#方法、类#成员]
     */
    public void saveStringValue(String key, String value)
    {
        if (sharedPreferences == null)
        {
            return;
        }

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 获取String值
     *
     * @param key 保存的键
     * @param defaultValue 默认的值
     * @return 键对应的值
     */
    public String readStringValue(String key, String defaultValue)
    {
        if (sharedPreferences == null)
        {
            return defaultValue;
        }

        return sharedPreferences.getString(key, defaultValue);
    }
}
