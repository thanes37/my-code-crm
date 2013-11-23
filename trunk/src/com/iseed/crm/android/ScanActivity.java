
package com.iseed.crm.android;

import com.google.zxing.Result;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.SimpleCrypto;
import com.iseed.crm.android.customer.ShopInfoActivity;
import com.iseed.crm.android.shop.CustomerInfoActivity;
import com.jwetherell.quick_response_code.CaptureActivity;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Extended from Decoder activity. Will return its scan result back to Activity
 * invoke it.
 * @author MrHung
 *
 */
public class ScanActivity extends CaptureActivity {
	
    private static final String TAG = "ScanActivity";

	@Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        CharSequence displayContents = resultHandler.getDisplayContents();
        
        // Decrypt scan result to real UID
        String decrypted = null;
        try {
			decrypted = SimpleCrypto.decrypt(Constant.SEED_CRYPTO, (String) displayContents);
			Log.v(TAG, "UID = "+decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        // TODO : block result when scanning just for product result only. Ignore others.
        
        if (decrypted != null){
        	String code = decrypted.substring(0, 4);
        	String uid = decrypted.substring(4);
        	Log.v(TAG, "Code = "+ code +"; UID = " + uid);
        	if (code.equals(Constant.QRCODE_UID_CUSTOMER)){
        		Intent i = new Intent(getApplicationContext(), CustomerInfoActivity.class);
        		i.putExtra(Constant.UID,uid);
        		startActivity(i);
        		finish();
        	} else if(code.equals(Constant.QRCODE_UID_SHOP)){
        		Intent i = new Intent(getApplicationContext(), ShopInfoActivity.class);
        		i.putExtra(Constant.UID,uid);
        		startActivity(i);
        		finish();
        	} else if(code.equals(Constant.QRCODE_UID_PRODUCT)){
        		Intent i = new Intent();
        		i.putExtra(Constant.UID,uid);
        		setResult(Constant.SCAN_FOR_PRODUCT_UID, i);
        		finish();
        	} else {
        		super.handleDecode(rawResult, barcode);
        	}
        } else {
        	super.handleDecode(rawResult, barcode);
        }
    }

}
