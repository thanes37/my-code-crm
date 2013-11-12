/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.iseed.crm.android.login;

import com.iseed.crm.android.MainActivity;
import com.iseed.crm.android.R;
import com.iseed.crm.android.common.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	TextView registerErrorMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions(RegisterActivity.this);
                int status = userFunction.login(email, password);
                if (status == Constant.SUCCESS){
                	if (userFunction.getRole().equals("customer")){
	                	// Launch Home Screen
	                    Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
	                    
	                    // Close all views before launching Dashboard
	                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(dashboard);
	                     
	                    // Close register Screen
	                    finish();
                	} else {
                		// TODO : other role: SHOP
                	}
                } else if (status == Constant.EMAIL_EXISTED){
                	registerErrorMsg.setText(getApplicationContext().getString(R.string.msg_register_existed));
                } else {
                	registerErrorMsg.setText(getApplicationContext().getString(R.string.msg_register_error));
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
}
