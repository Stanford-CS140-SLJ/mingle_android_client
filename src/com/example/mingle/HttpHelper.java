package com.example.mingle;
//package com.hmkcode.android;

        
        import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.net.*;

        import io.socket.*;

import java.util.ArrayList;

        import com.example.mingle.MingleUser;
        









import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
//import com.hmkcode.android.vo.Person;
        import org.json.*;

import java.io.IOException;
import java.lang.String;

/**
 * Created by Tempnote on 2014-06-02.
 */
public class HttpHelper extends AsyncTask<String, MingleUser, Integer>  {

    private SocketIO socket = null;
    private Context currContext = null;

    public HttpHelper(String url, Context context){
        try {
           //SocketIO.setDefaultSSLSocketFactory(SSLContext.getDefault());
            socket = new SocketIO(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } /*catch (NoSuchAlgorithmException f) {
            f.printStackTrace();
        }*/

        System.out.println("kill me now");
        if (socket == null) {
            System.out.println("Socket is not availiable");
            return;
        }
        //String cook  = CookieManager.;
        System.out.println(socket.toString());
        socket.addHeader("Cookie", "cookie");
        currContext = context;

        IOCallback sth  = new IOCallback() {

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                System.out.println("an Error occured");

                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                System.out.println("Server triggered event '" + event + "'");
                
                if(event.equals("user_create_return")){
                    ((MainActivity)currContext).createUser((JSONObject) args[0]);

                    Intent i = new Intent(currContext, HuntActivity.class);
                    currContext.startActivity(i);
                } 

                if(event.equals("get_list_return")){
                    ((HuntActivity)currContext).showList((JSONArray)args[0]);
                }
            }

            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    System.out.println("Server said:" + json.toString(2));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        socket.connect( sth);
    }
    
    
    

    //what's this? -SK
    public void send(ArrayList<Bitmap> photos, String comment, String sex, int number, float longitude, float latitude)  {
       
    	
    	String baseURL = "http://ec2-54-178-214-176.ap-northeast-1.compute.amazonaws.com:8080/";
    	baseURL += "user_create?";
    	baseURL += "comm=" +comment + "&";
    	baseURL += "sex=" + sex + "&";
    	baseURL += "num=" + (new Integer(number)).toString() + "&";
    	baseURL += "loc_long=" + (new Float(longitude)).toString() + "&";
    	baseURL += "loc_lat=" + (new Float(latitude)).toString();
    	System.out.println(baseURL);
    	//initInfoObject.put("photo_count", photos.size());
        
    	/*
    	for (int i = 0; i < 3; i++) {
            Integer x = i;
            if( i < photos.size())
            	baseURL += "pic" + Integer.toString(x + 1) photos.get(i);
                initInfoObject.put("pic" + Integer.toString(x + 1), photos.get(i));
            else
                initInfoObject.put("pic" + Integer.toString(x + 1),"");
        }*/
    	final String cpy = baseURL;
       
    	   
    	   new Thread(new Runnable() {
    	        public void run() {
    	        	HttpClient client = new DefaultHttpClient();
    	        	HttpGet poster = new HttpGet(cpy);
    	        	 HttpResponse response = null;
					try {
						response = client.execute(poster);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	        	 HttpResponseBody(response);
    	        }
    	    }).start();
    	  
    	   System.out.println("Got here!!");
    	  
       
    }
    
    public void HttpResponseBody(HttpResponse response) { 
    	System.out.println(Integer.valueOf(response.getStatusLine().getStatusCode()).toString());
    	if(response.getStatusLine().getStatusCode() == 200)
        {
			HttpEntity entity = response.getEntity();
			assert (entity != null);
			System.out.println("Entity:"+entity);
			if (entity != null) {
				String responseBody = "";
				try {
					responseBody = EntityUtils.toString(entity);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("finalResult"+responseBody.toString());
            }
        }
    	
    }
    
    /* Method Name: changeContext
     * Should be called whenever the context changes.
     */
    public void changeContext (Context context){
        this.currContext = context;
    }

    /*
    * Sends login info along to the server, and hopefully what will be returned
    * is the unique id of the user as well as some other useful information
    */
    public void sendInitialInfo(ArrayList<Bitmap> photos, String comment, String sex, int number, float longitude, float latitude) {
    	
    	boolean fat = true;
    	this.send(photos, comment, sex, number, longitude, latitude);
    	if (fat) return; 
        if (photos == null) photos = new ArrayList<Bitmap>();
        JSONObject initInfoObject = new JSONObject();
        //JSONObject photo_arr = new JSONObject();
        try {
            initInfoObject.put("comm",comment);
            initInfoObject.put("sex",sex);
            initInfoObject.put("num",number);
            initInfoObject.put("loc_long",longitude);
            initInfoObject.put("loc_lat",latitude);

            //initInfoObject.put("photo_count", photos.size());
            for (int i = 0; i < 3; i++) {
                Integer x = i;
                if( i < photos.size())
                    initInfoObject.put("pic" + Integer.toString(x + 1), photos.get(i));
                else
                    initInfoObject.put("pic" + Integer.toString(x + 1),"");
            }
            //initInfoObject.put("photos", photo_arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("user_create",initInfoObject);
    }

    public void requestUserList(String uid, String sex, float loc_lat, float loc_long, int dist_lim, int num_of_users) {
        JSONObject userListRequestObject = new JSONObject();
        try {
            userListRequestObject.put("uid", uid);
            userListRequestObject.put("sex", sex);
            userListRequestObject.put("loc_lat", loc_lat);
            userListRequestObject.put("loc_long", loc_long);
            userListRequestObject.put("dist_lim", dist_lim);
            userListRequestObject.put("list_num", num_of_users);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("get_list", userListRequestObject);
    }

    //@Override
    protected Integer doInBackground(String... urls) {
        return 0;
    }

    //@Override
    protected void onProgressUpdate(Integer... progress) {
       // setProgressPercent(progress[0]);
    }

    //Override
    protected void onPostExecute(Long result) {
        //showDialog("Downloaded " + result + " bytes");
    }
}