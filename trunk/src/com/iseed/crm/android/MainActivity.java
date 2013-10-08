
package com.iseed.crm.android;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.Constant;
import com.jwetherell.quick_response_code.CaptureActivity;
import com.jwetherell.quick_response_code.EncoderActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    public Context context;
    
    public static TextView txtUserName;
    public static TextView txtIdentifier;
    public static TextView txtEmail;
    public static TextView txtLink;
    public static ImageView imgvUserQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtIdentifier = (TextView) findViewById(R.id.txtIdentifier);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtLink = (TextView) findViewById(R.id.txtLink);
        
        imgvUserQR = (ImageView) findViewById(R.id.imgvUserQR);
        
        // Setting view content
        setImgUserQR();
        setUserInfor();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                
                return true;
            case R.id.menu_scan:
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_generate:
                Intent encodeIntent = new Intent(this, EncoderActivity.class);
                startActivity(encodeIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void setImgUserQR(){
        if (isLogin()){
            
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_anonymous);
            imgvUserQR.setImageBitmap(bitmap);
        }
        
//        String name = c.getString(str_url);
//        URL url_value = new URL(name);
//        ImageView profile = (ImageView)v.findViewById(R.id.vdo_icon);
//        if (profile != null) {
//            Bitmap mIcon1 =
//                BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
//            profile.setImageBitmap(mIcon1);
//        }
    }
    
    public void setUserInfor(){
        
        
        if (isLogin()){
            
        } else {
            txtUserName.setText(getResources().getString(R.string.msg_please_login));
        }
    }
    
    private boolean isLogin(){
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Constant.PREFS_NAME, 0);
        boolean isLogin = settings.getBoolean("isLogin", false);
        return isLogin;
    }

}
