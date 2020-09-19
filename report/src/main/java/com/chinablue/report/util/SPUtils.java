package com.chinablue.report.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * SharedPreferences工具类
 */
public final class SPUtils {
    private static String SPAESKEY = "chinabluetv35111";
    private static Map<String, SPUtils> sSPMap = new HashMap<>();
    private SharedPreferences sp;

    /**
     * 获取SP实例
     */
    public static SPUtils getInstance() {
        return getInstance("chinaReport");
    }

    /**
     * 获取SP实例
     */
    public static SPUtils getInstance(String spName) {
        if (isSpace(spName)) spName = "chinaReport";
        SPUtils sp = sSPMap.get(spName);
        if (sp == null) {
            sp = new SPUtils(spName);
            sSPMap.put(spName, sp);
        }
        return sp;
    }

    private SPUtils(final String spName) {
        sp = Utils.getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * SP中写入String
     */
    public void put( final String key, final String value) {
        try {
            if (!TextUtils.isEmpty(value))
                sp.edit().putString(key, AESUtil.encryptString(value, SPAESKEY)).apply();
            else
                sp.edit().putString(key, "").apply();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * SP中读取String
     */
    public String getString( final String key) {
        return getString(key, "");
    }

    /**
     * SP中读取String
     */
    public String getString( final String key, final String defaultValue) {
        try {
            if (!TextUtils.isEmpty(sp.getString(key, "")))
                return AESUtil.decryptString(sp.getString(key, defaultValue), SPAESKEY);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        return !TextUtils.isEmpty(defaultValue) ? defaultValue : "";

//        return sp.getString(key, defaultValue);
    }

    /**
     * SP中写入int
     */
    public void put( final String key, final int value) {
        sp.edit().putInt(key, value).apply();
    }

    /**
     * SP中读取int
     */
    public int getInt( final String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     */
    public int getInt( final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * SP中写入long
     */
    public void put( final String key, final long value) {
        sp.edit().putLong(key, value).apply();
    }

    /**
     * SP中读取long
     */
    public long getLong( final String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     */
    public long getLong( final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * SP中写入float
     */
    public void put( final String key, final float value) {
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * SP中读取float
     */
    public float getFloat( final String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     */
    public float getFloat( final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * SP中写入boolean
     */
    public void put( final String key, final boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * SP中读取boolean
     */
    public boolean getBoolean( final String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     */
    public boolean getBoolean( final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * SP中写入String集合
     */
    public void put( final String key,  final Set<String> values) {
        sp.edit().putStringSet(key, values).apply();
    }

    /**
     * SP中读取StringSet
     */
    public Set<String> getStringSet( final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * SP中读取StringSet
     */
    public Set<String> getStringSet( final String key,  final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * SP中获取所有键值对
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * SP中是否存在该key
     */
    public boolean contains( final String key) {
        return sp.contains(key);
    }

    /**
     * SP中移除该key
     */
    public void remove( final String key) {
        sp.edit().remove(key).apply();
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        sp.edit().clear().apply();
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
