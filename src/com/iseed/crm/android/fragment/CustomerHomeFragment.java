package com.iseed.crm.android.fragment;

import com.iseed.crm.android.MainModel;
import com.iseed.crm.android.R;
import com.iseed.crm.android.login.UserFunctions;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerHomeFragment extends Fragment{
	
	
	public Context context;
    private MainModel mModel;
    private UserFunctions user;
    
    public static TextView txtUserName;
    public static TextView txtReputation;
    public static ImageView imgvUserAvatar;
    public static ImageView imgvUserQR;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customer_home, container, false);
        
        
        txtUserName = (TextView) rootView.findViewById(R.id.txtCustomerName);
        txtReputation = (TextView) rootView.findViewById(R.id.txtCustomerReputation);
        imgvUserQR = (ImageView) rootView.findViewById(R.id.imgvCutomerHomeQR);
        imgvUserAvatar = (ImageView) rootView.findViewById(R.id.imgvCustomerAvatar);
        
        // Setting view content
        setImgUserQR();
        setUserInfor();
        
        return rootView;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getActivity();
        mModel = new MainModel(context);
        user = new UserFunctions(context);
    }
    
	@Override
	public void onResume(){
		super.onResume();
		
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		
		
	}
	
    public void setImgUserQR(){
        imgvUserQR.setImageBitmap(mModel.getUserQRcode());
    }
    
    public void setUserInfor(){
    	// TODO: set avatar
    	
    	txtReputation.setText(Integer.toString(user.getReputation()));
        txtUserName.setText(user.getName());
        
    }
}