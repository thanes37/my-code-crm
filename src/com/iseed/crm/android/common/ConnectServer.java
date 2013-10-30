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
import org.json.JSONException;
import org.json.JSONObject;

import com.iseed.crm.android.R;

import android.content.Context;
import android.util.Log;

public class ConnectServer {

    private Context context;
    public String baseUrl;
    public int resultCode;

    public ConnectServer(Context ctx){
        context = ctx;
        baseUrl = context.getString(R.string.url_base);
    }

    public Shop getShopInfo(String uid){
        Shop shop = new Shop();
        String url = baseUrl+ context.getString(R.string.url_get_shop_info);
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", "qwertyu"));
        nameValuePair.add(new BasicNameValuePair("uid", "qwertyu"));


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
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", "qwertyu"));
        nameValuePair.add(new BasicNameValuePair("uid", "qwertyu"));


        // Parsing JSON object
        try {
            JSONObject jsonObject = getJSONPost(url, nameValuePair);
            String status = jsonObject.getString("status");
            if (status.equals("OK")) {
                JSONObject jsonCustomer = jsonObject.getJSONObject("user");
                customer.uid = jsonCustomer.getString("uid");
                customer.displayName = jsonCustomer.getString("name");
                customer.photoLink = jsonCustomer.getString("photo_link");
                customer.gender = jsonCustomer.getString("gender");
                customer.profileState = jsonCustomer.getString("profile_state");
                customer.location = jsonCustomer.getString("location");
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
    
    public int addInvolvement(String uid){
        String url = baseUrl+ context.getString(R.string.url_add_my_customer);
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", "qwertyu"));
        nameValuePair.add(new BasicNameValuePair("uid", "qwertyu"));

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
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("token", "qwertyu"));
        nameValuePair.add(new BasicNameValuePair("uid", "qwertyu"));

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
        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        
        nameValuePair.add(new BasicNameValuePair("token", "qwertyu"));
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

    /**
     * @param url
     * @param nameValuePair
     * @return JSON object of the server response
     */
    private JSONObject getJSONPost(String url, List<NameValuePair> nameValuePair) {
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
