/**
 * 
 */
package com.iseed.crm.android;

import com.iseed.crm.android.login.User;
import com.iseed.crm.android.qrcode.EncoderActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.WindowManager;

/**
 * Main Model class to manage data
 * @author MrHung
 *
 */
public class MainModel {
    
    private Context context;
    private User user;
    
    public MainModel(Context ctx){
        context = ctx;
        user = new User(context);
    }
    
    @SuppressWarnings("deprecation")
    public Bitmap getUserQRcode(){
        int smallerDimension;
     // This assumes the view is full screen, which is a good assumption
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 7 / 8;
        
        return EncoderActivity.getEncode(user.getEmail(), smallerDimension);
    }
}
