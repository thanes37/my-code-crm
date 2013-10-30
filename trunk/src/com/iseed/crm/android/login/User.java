package com.iseed.crm.android.login;

import com.iseed.crm.android.common.Constant;

import android.content.Context;
import android.content.SharedPreferences;



public class User {
    
    public static final String PREF_IS_LOGIN = "isLogin";
    public static final String PREF_UID = "uid";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_USER_NAME = "name";
    public static final String PREF_CREATED_AT = "created_at";
    
    
    String name;
    String email;
    String uid;
    String createdAt;
    
    private Context context;
    
    public User(Context ctx){
        context = ctx;
    }
    
    public void setLoginState(boolean state){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREF_IS_LOGIN, state);
        // Commit the edits!
        editor.commit();
    }
    
    public boolean isLogin(){
     // Restore preferences
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        boolean isLogin = settings.getBoolean(PREF_IS_LOGIN, false);
        return isLogin;
    }
    
    public void updateUser (String name, String email, String uid, String createdat){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putString(PREF_UID, uid);
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_CREATED_AT, createdat);
        
        // Commit the edits!
        editor.commit();
    }
    
    public void getUser(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        name = settings.getString(PREF_USER_NAME, null);
        email = settings.getString(PREF_EMAIL, null);
        uid = settings.getString(PREF_UID, null);
        createdAt = settings.getString(PREF_CREATED_AT, null);
    }
    
    public String getName(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_USER_NAME, null);
    }
    
    public String getEmail(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_EMAIL, null);
    }
    public String getUid(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_UID, null);
    }
    public String getCreatedAt(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_CREATED_AT, null);
    }
}
