
package com.iseed.crm.android;

import com.google.zxing.Result;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.common.SimpleCrypto;
import com.jwetherell.quick_response_code.CaptureActivity;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;

import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Extended from Decoder activity. Will return its scan result back to Activity
 * invoke it.
 * @author MrHung
 *
 */
public class ScanActivity extends CaptureActivity {
    @Override
    public void handleDecode(Result rawResult, Bitmap barcode) {
        ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
        CharSequence displayContents = resultHandler.getDisplayContents();
        
        // Decrypt scan result to real UID
        String decrypted = null;
        try {
			decrypted = SimpleCrypto.decrypt(Constant.SEED_CRYPTO, (String) displayContents);
		} catch (Exception e) {
			e.printStackTrace();
		}
        // TODO : Check if valid code or not
        Intent intent = new Intent();
        intent.putExtra(Constant.SCAN_STRING_RESULT, decrypted);
        setResult(RESULT_OK, intent);
        finish();
    }

}
