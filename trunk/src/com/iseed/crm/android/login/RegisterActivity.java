/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.iseed.crm.android.login;

import com.iseed.crm.android.R;
import com.iseed.crm.android.common.ConnectServer;
import com.iseed.crm.android.common.Constant;
import com.iseed.crm.android.customer.CustomerMainActivity;
import com.iseed.crm.android.shop.ShopMainActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText edtName;
	EditText edtEmail;
	EditText edtPass;
	EditText edtPassConfirm;
	TextView txtErrorMessage;
	ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Importing all assets like buttons, text fields
		edtName = (EditText) findViewById(R.id.registerName);
		edtEmail = (EditText) findViewById(R.id.registerEmail);
		edtPass = (EditText) findViewById(R.id.registerPassword);
		edtPassConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		txtErrorMessage = (TextView) findViewById(R.id.register_error);
		
		progressBar = (ProgressBar) findViewById(R.id.prgbRegister);
		progressBar.setVisibility(View.INVISIBLE);
		
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				
				if (ConnectServer.isOnline(RegisterActivity.this)){
					String name = edtName.getText().toString();
					String email = edtEmail.getText().toString();
	                String password = edtPass.getText().toString();
	                String error = validate();
	                if (error.length()!= 0){
	                	// Show error message
	                	txtErrorMessage.setText(error);
	                } else {
	                	// Input fine, let register
	                	new RegisterTask().execute(name, email, password);
	                }
				} else {
					Toast.makeText(
							RegisterActivity.this, 
							R.string.msg_no_network_common, 
							Toast.LENGTH_LONG).show();
				}
				
				
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}
	
	/**
	 * Parameter: name, email, password
	 * @author MrHung
	 */
	private class RegisterTask extends AsyncTask<String, Void, Integer> {

		UserFunctions userFunction;

		protected void onPreExecute (){
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        }
        
        protected Integer doInBackground(String... params) {
        	userFunction = new UserFunctions(RegisterActivity.this);
            int status = userFunction.registerCustomer(params[0], params[1], params[2]);
            return status;
        }

        protected void onPostExecute(Integer status) {
        	progressBar.setVisibility(View.INVISIBLE);
        	btnRegister.setEnabled(true);
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
            	} 
            } else if (status == Constant.EMAIL_EXISTED){
            	txtErrorMessage.setText(getApplicationContext().getString(R.string.msg_register_existed));
            } else {
            	txtErrorMessage.setText(getApplicationContext().getString(R.string.msg_register_error));
            }
            
        }
    }
	
	/**
	 * Validate user input. Error will return to string to display for user.
	 * @return
	 */
	public String validate() {
		String name = edtName.getText().toString();
		String address = edtEmail.getText().toString();
        String password = edtPass.getText().toString();
        String passwordConfirm = edtPassConfirm.getText().toString();
        if(name.length()==0){ 
        	return this.getString(R.string.msg_register_name_invalid);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(address).matches()) {
			return this.getString(R.string.msg_register_not_address) + ": "
					+ address;
		} else if (!password.equals(passwordConfirm)){
			return this.getString(R.string.msg_register_password_not_match);
		} else if (password.length()<6){
			return this.getString(R.string.msg_register_password_invalid);
		}
		
		return "";
	}
}
