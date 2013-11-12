package com.iseed.crm.android.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.SimpleCrypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;



public class UserFunctions {
    
    public static final String PREF_IS_LOGIN = "isLogin";
    public static final String PREF_UID = "uid";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_USER_NAME = "name";
    public static final String PREF_CREATED_AT = "created_at";
    public static final String PREF_TOKEN = "token";
    public static final String PREF_ROLE = "role";
    public static final String PREF_REPUTATION = "reputation";
    
    boolean isLogin;
    String name;
    String email;
    String uid;
    String createdAt;
    String token;
    String role;
    
    private Context context;
    
    public UserFunctions(Context ctx){
        context = ctx;
    }
    
    public int login(String email, String password){
    	ConnectServer connect = new ConnectServer(context);
    	JSONObject jsonResponse = connect.login(email, password);
    	
    	try {
    		String status = jsonResponse.getString("status");
    		if (status.equals("OK")){
    			// Login successful
    			setLoginState(true);
    			
    			JSONObject publicUser = jsonResponse.getJSONObject("public");
    			updateUser(publicUser.getString("name"), 
    					publicUser.getString("email"), 
    					publicUser.getString("uid"), 
    					publicUser.getString("member_since"),
    					jsonResponse.getString("role"),
    					jsonResponse.getString("token"));
    			
    			
    		} else if (status.equals("Incorrect")){
    			return Constant.INCORRECT;
    		} else {
    			// Error
    			return Constant.ERROR;
    		}
    	} catch (JSONException e){
    		e.printStackTrace();
    		return Constant.ERROR;
    	}
    	return Constant.SUCCESS;
    }
    
    public int registerCustomer(String name, String email, String password){
    	ConnectServer connect = new ConnectServer(context);
    	JSONObject jsonResponse = connect.register(name, email, password, "customer");
    	
    	try {
    		String status = jsonResponse.getString("status");
    		if (status.equals("OK")){
    			// Login successful
    			setLoginState(true);
    			
    			JSONObject publicUser = jsonResponse.getJSONObject("public");
    			updateUser(publicUser.getString("name"), 
    					publicUser.getString("email"), 
    					publicUser.getString("uid"), 
    					publicUser.getString("member_since"),
    					jsonResponse.getString("role"),
    					jsonResponse.getString("token"));
    			
    			
    		} else if (status.equals("EmailExisted")){
    			return Constant.EMAIL_EXISTED;
    		} else {
    			// Error
    			return Constant.ERROR;
    		}
    	} catch (JSONException e){
    		e.printStackTrace();
    		return Constant.ERROR;
    	}
    	return Constant.SUCCESS;
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
    
    public void updateUser (String name, String email, String uid, String createdat, String role, String token){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putString(PREF_UID, uid);
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_EMAIL, email);
        editor.putString(PREF_CREATED_AT, createdat);
        editor.putString(PREF_ROLE, role);
        editor.putString(PREF_TOKEN, token);
        
        // Commit the edits!
        editor.commit();
    }
    
    public UserFunctions getUser(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        isLogin = settings.getBoolean(PREF_IS_LOGIN, false);
        name = settings.getString(PREF_USER_NAME, null);
        email = settings.getString(PREF_EMAIL, null);
        uid = settings.getString(PREF_UID, null);
        createdAt = settings.getString(PREF_CREATED_AT, null);
        role = settings.getString(PREF_ROLE, null);
        token = settings.getString(PREF_TOKEN, null);
        return this;
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
    public String getEncryptedUid(){
    	String encrypted = "UID";
    	try {
    		String clearUid = getUid();
    		clearUid = Constant.QRCODE_UID+clearUid;
    		Log.v("User", "Encrype String" + clearUid);
    		encrypted = SimpleCrypto.encrypt(Constant.SEED_CRYPTO, clearUid);
    	} catch (Exception e){
    		e.printStackTrace();
    	}
    	return encrypted;
    }
    public String getCreatedAt(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_CREATED_AT, null);
    }
    
    public String getToken(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_TOKEN, null);
    }
    public void setToken (String token){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putString(PREF_TOKEN, token);
        
        // Commit the edits!
        editor.commit();
    }
    
    public String getRole(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getString(PREF_ROLE, null);
    }
    public void setRole (String role){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putString(PREF_ROLE, role);
        
        // Commit the edits!
        editor.commit();
    }
    
    public int getReputation(){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        return settings.getInt(PREF_REPUTATION, 0);
    }
    public void setReputation (int reputation){
        SharedPreferences settings = context.getSharedPreferences(Constant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putInt(PREF_REPUTATION, reputation);
        
        // Commit the edits!
        editor.commit();
    }
}
