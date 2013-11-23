package com.iseed.crm.android.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.iseed.crm.android.R;
import com.iseed.crm.android.adapter.CustomerInvolve;
import com.iseed.crm.android.adapter.PointParent;
import com.iseed.crm.android.login.UserFunctions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
 * TODO: return json object may contain null value. Filter it for each value.
 * Using json.isNull();
 */

public class ConnectServer {

    private static final String TAG = "ConnectServer";
	private Context context;
    public String baseUrl;
    public int resultCode;

    public ConnectServer(Context ctx){
        context = ctx;
        baseUrl = context.getString(R.string.url_base);
    }
    
    public static boolean isOnline(Context ctx) {
	    ConnectivityManager cm =
	        (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}

    public Shop getShopInfo(String uid){
        Shop shop = new Shop();
        String url = baseUrl+ context.getString(R.string.url_get_shop_info);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", uid));


        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONObject jsonShop = jsonObject.getJSONObject("shop");
                shop.uid = jsonShop.getString("uid");
                shop.displayName = jsonShop.getString("name");
                shop.photoLink = jsonShop.getString("photo_link");
                shop.address = jsonShop.getString("address");
                shop.phoneNumber = jsonShop.getString("phone_number");
                shop.contactEmail = jsonShop.getString("contact_email");
                shop.website = jsonShop.getString("website");
                shop.description = jsonShop.getString("description");
                shop.reputation = jsonShop.getInt("reputation");

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return shop;
    }
    
    public Customer getCustomerInfo(String uid){
        Customer customer = new Customer();
        String url = baseUrl+ context.getString(R.string.url_get_customer_infor);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", uid));


        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONObject jsonCustomer = jsonObject.getJSONObject("user");
                customer.uid = jsonCustomer.getString("uid");
                customer.displayName = jsonCustomer.getString("name");
                customer.photoLink = jsonCustomer.getString("photo_link");
                if (!jsonCustomer.isNull("gender")){
                	customer.gender = jsonCustomer.getString("gender");
                } else {
                	customer.gender = "";
                }
                customer.profileState = jsonCustomer.getString("profile_state");
                if (!jsonCustomer.isNull("location")){
                	customer.location = jsonCustomer.getString("location");
                } else {
                	customer.location = "";
                }
                
                customer.reputation = jsonCustomer.getInt("reputation");
                customer.memberSince = jsonCustomer.getString("member_since");

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return customer;
    }
    
    public Involvement getInvolvementCustomer(String customerUid){
        Involvement involvement = new Involvement();
        String url = baseUrl+ context.getString(R.string.url_get_involvement_customer);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", customerUid));


        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONObject jsonCustomer = jsonObject.getJSONObject("involvement");
                involvement.id = jsonCustomer.getInt("id");
                involvement.type = jsonCustomer.getString("type");
                involvement.pointSum = jsonCustomer.getInt("point");
                involvement.state = jsonCustomer.getString("state");
                involvement.timestamp = jsonCustomer.getString("timestamp");

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("NeedInvolved")){
            	involvement = null;
            	resultCode = Constant.NEED_INVOLVED;
            } else {
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return involvement;
    }
    
    public Involvement getInvolvementShop(String shopUid){
        Involvement involvement = new Involvement();
        String url = baseUrl+ context.getString(R.string.url_get_involvement_shop);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", shopUid));


        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONObject jsonCustomer = jsonObject.getJSONObject("involvement");
                involvement.id = jsonCustomer.getInt("id");
                involvement.type = jsonCustomer.getString("type");
                involvement.pointSum = jsonCustomer.getInt("point");
                involvement.state = jsonCustomer.getString("state");
                involvement.timestamp = jsonCustomer.getString("timestamp");

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("NeedInvolved")){
            	involvement = null;
            	resultCode = Constant.NEED_INVOLVED;
            } else {
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return involvement;
    }
    
    public int addInvolvement(String uid){
        String url = baseUrl+ context.getString(R.string.url_add_my_customer);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", uid));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
            	
                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("CustomerAlreadyInvolved")) {
                resultCode = Constant.CUSTOMER_INVOLVED;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return resultCode;
    }
    
    public int removeInvolvement(String uid){
        
        // TODO : code for remove
        String url = baseUrl+ context.getString(R.string.url_add_my_customer);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", uid));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("CustomerAlreadyInvolved")) {
                resultCode = Constant.CUSTOMER_INVOLVED;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return resultCode;
    }
    
    public int addPoint(String uid, String type, String detail, int point){
        String url = baseUrl+ context.getString(R.string.url_add_customer_point);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("uid", uid));
        nameValuePair.add(new BasicNameValuePair("type", type));
        nameValuePair.add(new BasicNameValuePair("detail", detail));
        nameValuePair.add(new BasicNameValuePair("point", Integer.toString(point)));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("CustomerAlreadyInvolved")) {
                resultCode = Constant.CUSTOMER_INVOLVED;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return resultCode;
    }
    
    public List<PointTrack> getShopCustomerPoints(int involvementId){
    	List<PointTrack> trackList = new ArrayList<PointTrack>();
        String url = baseUrl+ context.getString(R.string.url_get_shop_customer_points);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("involvement_id", Integer.toString(involvementId)));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray history = jsonObject.getJSONArray("history");
                for (int i = 0; i< history.length(); i++){
                	JSONObject jsonTrack = history.getJSONObject(i);
                	//Create parent class object
                	PointTrack track = new PointTrack();
                	track.type = jsonTrack.getString("type");
        			track.id = Integer.parseInt(jsonTrack.getString("id"));
        			if (!jsonTrack.isNull("detail")){
        				track.detail = jsonTrack.getString("detail");
        			} else {
        				track.detail = "";
        			}
        			track.timestamp = jsonTrack.getString("timestamp");
        			track.point = Integer.parseInt(jsonTrack.getString("point"));
        			
        			trackList.add(track);
                }
                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("NotFound")) {
                resultCode = Constant.NOT_FOUND;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return trackList;
    }
    
    public List<PointParent> getPointHistory(int page_number){
    	List<PointParent> parents = new ArrayList<PointParent>();
        String url = baseUrl+ context.getString(R.string.url_get_point_history);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("page_number", Integer.toString(page_number)));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray history = jsonObject.getJSONArray("history");
                for (int i = 0; i< history.length(); i++){
                	JSONObject shopJson = history.getJSONObject(i);
                	//Create parent class object
                	PointParent parent = new PointParent();
        			
        			parent.shopUid = shopJson.getString("shop_uid");
        			parent.shopName = shopJson.getString("shop_name");
        			parent.involvementId = Integer.parseInt(shopJson.getString("involvement_id"));
        			parent.pointSum = Integer.parseInt(shopJson.getString("point_sum"));
        			
        			parent.children = new ArrayList<PointTrack>();
        			
        			JSONArray pointTrack = shopJson.getJSONArray("point_track");
        			for (int j = 0; j<pointTrack.length(); j++){
        				JSONObject track = pointTrack.getJSONObject(j);
        				PointTrack child = new PointTrack();
        				
        				child.id = Integer.parseInt(track.getString("id"));
        				child.detail = track.getString("detail");
        				child.type = track.getString("type");
        				child.point = Integer.parseInt(track.getString("point"));
        				child.timestamp = track.getString("timestamp");
        				
        				parent.children.add(child);
        			}
        			
        			parents.add(parent);
                }

                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("CustomerAlreadyInvolved")) {
                resultCode = Constant.CUSTOMER_INVOLVED;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return parents;
    }
    
    public List<CustomerInvolve> getCustomerList(int page_number){
    	List<CustomerInvolve> customerList = new ArrayList<CustomerInvolve>();
        String url = baseUrl+ context.getString(R.string.url_get_customer_history);
        UserFunctions user = new UserFunctions(context);
        String token = user.getToken();
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", token));
        nameValuePair.add(new BasicNameValuePair("page_number", Integer.toString(page_number)));

        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONArray customers = jsonObject.getJSONArray("customer");
                for (int i = 0; i< customers.length(); i++){
                	JSONObject customerJson = customers.getJSONObject(i);
                	
                	CustomerInvolve customer = new CustomerInvolve();
                	customer.customer.uid = customerJson.getString("uid");
                	customer.customer.displayName = customerJson.getString("customer_name");
                	customer.customer.photoLink = customerJson.getString("photo_link");
                	
                	JSONObject involvement = customerJson.getJSONObject("involvement");
                	customer.involvement.id = Integer.parseInt(involvement.getString("id"));
                	customer.involvement.type = involvement.getString("type");
                	customer.involvement.pointSum = Integer.parseInt(involvement.getString("point"));
                	customer.involvement.state = involvement.getString("state");
                	customer.involvement.timestamp = involvement.getString("timestamp");
        			
        			customerList.add(customer);
                }
                resultCode = Constant.SUCCESS;
            } else if (status.equals("RequestLogin")) {
                resultCode = Constant.REQUEST_LOGIN;
            } else if (status.equals("CustomerAlreadyInvolved")) {
                resultCode = Constant.CUSTOMER_INVOLVED;
            } else { // Error
                resultCode = Constant.ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resultCode = Constant.ERROR;
        }
        return customerList;
    }
    
    public JSONObject login(String email, String password){
    	String url = baseUrl+ context.getString(R.string.url_login);
    	// key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("password", password));
    	
    	return getJSONPost(url, nameValuePair);
    }
    
    public JSONObject register(String name, String email, String password, String role){
    	String url = baseUrl+ context.getString(R.string.url_register);
    	// key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("password", password));
        nameValuePair.add(new BasicNameValuePair("name", name));
        nameValuePair.add(new BasicNameValuePair("role", role));
    	
    	return getJSONPost(url, nameValuePair);
    }

    /**
     * @param url
     * @param nameValuePair
     * @return JSON object of the server response
     */
    private JSONObject getJSONPost(String url, List<NameValuePair> nameValuePair) {
    	Log.v("URL", url);
    	
        JSONObject jsonResult = null;
        String resultString = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    Constant.TIME_OUT_CONNECTION);
            HttpConnectionParams.setSoTimeout(httpParameters, Constant.TIME_OUT_SOCKET);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse httpresponse = httpclient.execute(httppost);
            HttpEntity resultentity = httpresponse.getEntity();

            if (resultentity != null) {
                InputStream inputstream = resultentity.getContent();
                Header contentencoding = httpresponse
                        .getFirstHeader("Content-Encoding");
                if (contentencoding != null
                        && contentencoding.getValue().equalsIgnoreCase("gzip")) {
                    inputstream = new GZIPInputStream(inputstream);
                }

                resultString = convertStreamToString(inputstream);
                inputstream.close();
                Log.v("Get back", resultString);
                try {
                    jsonResult = new JSONObject(resultString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("Exception", "Timeout");
        }
        if (jsonResult == null) {
            jsonResult = new JSONObject();
            try {
                // adding some keys
                jsonResult.put("Status", "Error");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * Method to send request with jsonObj to url, then return a JSONObject of
     * the response.
     * 
     * @param url
     * @param jsonobj
     * @return
     */
    private JSONObject getJSONPost(String url, JSONObject jsonobj) {

        JSONObject jsonResult = null;
        // HTTP post
        //Log.v("Send J Obj", jsonobj.toString());
        //Log.v("URL", url);

        String resultString = "";
        try {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    Constant.TIME_OUT_CONNECTION);
            HttpConnectionParams.setSoTimeout(httpParameters, Constant.TIME_OUT_SOCKET);
            DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new ByteArrayEntity(jsonobj.toString().getBytes(
                    "UTF8")));
            httppost.setHeader("json", jsonobj.toString());
            HttpResponse httpresponse = httpclient.execute(httppost);
            HttpEntity resultentity = httpresponse.getEntity();

            if (resultentity != null) {
                InputStream inputstream = resultentity.getContent();
                Header contentencoding = httpresponse
                        .getFirstHeader("Content-Encoding");
                if (contentencoding != null
                        && contentencoding.getValue().equalsIgnoreCase("gzip")) {
                    inputstream = new GZIPInputStream(inputstream);
                }

                resultString = convertStreamToString(inputstream);
                inputstream.close();
                //Log.v("Get back", resultString);
                try {
                    jsonResult = new JSONObject(resultString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.v("Exception", "Timeout");
        }
        if (jsonResult == null) {
            jsonResult = new JSONObject();
            try {
                // adding some keys
                jsonResult.put("Status", "Error");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * Method send a GET request to address url and get back a string
     * 
     * @param url
     * @return the String result, null otherwise.
     */
    private JSONObject getJSONGet(String url) {
        JSONObject jsonResult = null;
        //Log.v("URL", url);
        // Create an intermediate to connect with the Internet
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                Constant.TIME_OUT_CONNECTION);
        HttpConnectionParams.setSoTimeout(httpParameters, Constant.TIME_OUT_SOCKET);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        HttpGet httpGet = new HttpGet(url);
        try {

            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity resultentity = httpResponse.getEntity();

            if (resultentity != null) {
                InputStream inputstream = resultentity.getContent();
                Header contentencoding = httpResponse
                        .getFirstHeader("Content-Encoding");
                if (contentencoding != null
                        && contentencoding.getValue().equalsIgnoreCase("gzip")) {
                    inputstream = new GZIPInputStream(inputstream);
                }
                String resultString = convertStreamToString(inputstream);
                //Log.v("Get back", resultString);
                inputstream.close();
                try {
                    jsonResult = new JSONObject(resultString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException cpe) {
            System.out.println("Exception generates caz of httpResponse :"
                    + cpe);
            cpe.printStackTrace();
        } catch (IOException ioe) {
            System.out
            .println("Second exception generates caz of httpResponse :"
                    + ioe);
            ioe.printStackTrace();
        }
        if (jsonResult == null) {
            jsonResult = new JSONObject();
            try {
                // adding some keys
                jsonResult.put("Status", "Error");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * Private method convert stream from HTTP response to string
     * 
     * @param is
     * @return Result string
     */
    private String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}
