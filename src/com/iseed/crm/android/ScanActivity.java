
package com.iseed.crm.android;

import com.google.zxing.Result;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.ShopInfoActivity;
import com.jwetherell.quick_response_code.CaptureActivity;
import com.jwetherell.quick_response_code.DecoderActivity;
import com.jwetherell.quick_response_code.result.ResultHandler;
import com.jwetherell.quick_response_code.result.ResultHandlerFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;

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
        
        Intent intent = new Intent();
        intent.putExtra(Constant.SCAN_STRING_RESULT, displayContents);
        setResult(RESULT_OK, intent);
        finish();
    }

}
