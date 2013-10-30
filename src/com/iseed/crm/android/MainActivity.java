package com.iseed.crm.android;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.login.LoginActivity;
import com.iseed.crm.android.login.RegisterActivity;
import com.iseed.crm.android.login.User;
import com.iseed.crm.android.qrcode.EncoderActivity;
import com.iseed.crm.android.shop.AddCustomerPointActivity;
import com.iseed.crm.android.shop.CustomerInfoActivity;
import com.jwetherell.quick_response_code.CaptureActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
    
    public Context context;
    private MainModel mModel;
    private User user;
    
    public static TextView txtUserName;
    public static TextView txtIdentifier;
    public static TextView txtEmail;
    public static TextView txtLink;
    public static ImageView imgvUserQR;
    public static Button btnLogin;
    public static Button btnRegister;
    public static Button btnTest;
    
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        mModel = new MainModel(context);
        user = new User(context);
        isLogin = user.isLogin();
        
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtIdentifier = (TextView) findViewById(R.id.txtIdentifier);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtLink = (TextView) findViewById(R.id.txtLink);
        
        btnLogin = (Button) findViewById(R.id.btnMainLogin);
        btnLogin.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnMainRegister);
        btnRegister.setOnClickListener(this);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);
        imgvUserQR = (ImageView) findViewById(R.id.imgvUserQR);
        
        // Setting view content
        setImgUserQR();
        setUserInfor();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem loginMenuItem = menu.findItem(R.id.menu_login);
        if (isLogin){
            loginMenuItem.setTitle(R.string.menu_logout);
        } else {
            loginMenuItem.setTitle(R.string.menu_login);
        }
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
            case R.id.menu_login:
                if (isLogin){
                    user.setLoginState(false);
                } else {
                }
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void setImgUserQR(){
        if (user.isLogin()){
            imgvUserQR.setImageBitmap(mModel.getUserQRcode());
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
        } else {
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_anonymous);
//            imgvUserQR.setImageBitmap(bitmap);
            
            
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
        
        
        if (user.isLogin()){
            txtUserName.setText(user.getName());
            txtEmail.setText(user.getEmail());
        } else {
            txtUserName.setText(getResources().getString(R.string.msg_please_login));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnMainLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMainRegister:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            // XXX 
            case R.id.btnTest:
                intent = new Intent(this, AddCustomerPointActivity.class);
                intent.putExtra(Constant.UID, "qwertyui");
                startActivity(intent);
                break;
            default :
                break;
        }
    }

}
