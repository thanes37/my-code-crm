/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.iseed.crm.android.login;


import com.iseed.crm.android.MainActivity;
import com.iseed.crm.android.R;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.CustomerMainActivity;
import com.iseed.crm.android.gymclub.GymMainActivity;
import com.iseed.crm.android.shop.CustomerInfoActivity;
import com.iseed.crm.android.shop.ShopMainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";
	
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
		
		progressBar = (ProgressBar) findViewById(R.id.prgbLogin);
		progressBar.setVisibility(View.INVISIBLE);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isOnline()){
					String email = inputEmail.getText().toString();
	                String password = inputPassword.getText().toString();
	                new LoginTask().execute(email, password);
				} else {
					Toast.makeText(
							LoginActivity.this, 
							R.string.msg_no_network_common, 
							Toast.LENGTH_LONG).show();
				}
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	private class LoginTask extends AsyncTask<String, Void, Integer> {

		UserFunctions userFunction;

		protected void onPreExecute (){
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        }
        
        protected Integer doInBackground(String... params) {
        	userFunction = new UserFunctions(LoginActivity.this);
            int status = userFunction.login(params[0], params[1]);
            return status;
        }

        protected void onPostExecute(Integer status) {
        	progressBar.setVisibility(View.INVISIBLE);
        	btnLogin.setEnabled(true);
        	if (status == Constant.SUCCESS){
            	if (userFunction.getRole().equals(Constant.ROLE_CUSTOMER)){
                	// Launch Home Screen
                    Intent dashboard = new Intent(getApplicationContext(), CustomerMainActivity.class);
                    
                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                     
                    // Close Login Screen
                    finish();
            	} else if (userFunction.getRole().equals(Constant.ROLE_SHOP)){
                	// Launch Home Screen
                    Intent dashboard = new Intent(getApplicationContext(), ShopMainActivity.class);
                    
                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                     
                    // Close Login Screen
                    finish();
            	} else if (userFunction.getRole().equals(Constant.ROLE_CLUB)){
            		// Launch Home Screen
                    Intent dashboard = new Intent(getApplicationContext(), GymMainActivity.class);
                    
                    // Close all views before launching Dashboard
                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(dashboard);
                     
                    // Close Login Screen
                    finish();
            	}
            } else if (status == Constant.INCORRECT){
            	loginErrorMsg.setText(getApplicationContext().getString(R.string.msg_login_incorrect));
            } else {
            	loginErrorMsg.setText(getApplicationContext().getString(R.string.msg_login_error));
            }
            
        }
    }
	
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
}
