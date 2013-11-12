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

public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();
                UserFunctions userFunction = new UserFunctions(LoginActivity.this);
                int status = userFunction.login(email, password);
                if (status == Constant.SUCCESS){
                	if (userFunction.getRole().equals("customer")){
	                	// Launch Home Screen
	                    Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
	                    
	                    // Close all views before launching Dashboard
	                    dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                    startActivity(dashboard);
	                     
	                    // Close Login Screen
	                    finish();
                	} else {
                		// TODO : SHOP
                	}
                } else if (status == Constant.INCORRECT){
                	loginErrorMsg.setText(getApplicationContext().getString(R.string.msg_login_incorrect));
                } else {
                	loginErrorMsg.setText(getApplicationContext().getString(R.string.msg_login_error));
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
}
