package com.witmergers.mquotes;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    public static final String URL_JSON_OBJECT = "https://andruxnet-random-famous-quotes.p.mashape.com/cat=movies";
    public static final String URL_JSON_ARRAY = "http://api.androidhive.info/volley/person_array.json";
    public static final String URL_STRING_REQ = "http://api.androidhive.info/volley/string_response.html";
    public static final String URL_IMAGE = "http://api.androidhive.info/volley/volley-image.jpg";
    public static final String TAG = AppController.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
       
    }
    
    private String author;
    private void jReq() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                URL_JSON_OBJECT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try
                        { 
                            String splittext = response.getString("quote");
                            author = response.getString("author");
                            if(!author.equalsIgnoreCase(""))
                            {
                                
                                callAuthor(author);

                            }
                            msgResponse.setText(splittext+"\t"+author);
                        }
                        catch (Exception e)
                        {
                            
                            
                        }
                       

                       
                       // hideProgressDialog();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
               // hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-Mashape-Key", "1yaC7x6PszmshfNWHx21eshMfqWgp14XMPxjsnuWW5xcvx9CLd");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq,"t");
    }

    

    private void callAuthor(String author) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

       String url = "http://www.omdbapi.com/?t="+author;
        url =url.replaceAll(" ","+");
       // String url = "http://www.omdbapi.com/?t=Dracula";

        Log.d("Chu",url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            String iUrl = response.getString("Poster").toString();
                            if(iUrl.equalsIgnoreCase("N/A"))
                            {
                                Picasso.with(getApplicationContext()).load("https://s-media-cache-ak0.pinimg.com/736x/ea/63/d5/ea63d56de1af1ae80d0a806fed661e15.jpg").into(mImageView);
                                
                            }
                            iUrl = iUrl+"&apikey=af35722e";
                            Log.d("Chu2",iUrl);
                            Picasso.with(getApplicationContext())
                                    .load(iUrl)
                                    
                                    .into(mImageView)
                            ;
                           /* Drawable image = mImageView.getDrawable();
                            linearLayout.setBackground(image);*/
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    private TextView msgResponse;
    private ImageView mImageView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        msgResponse = (TextView) findViewById(R.id.mTextView);
        mImageView = (ImageView) findViewById(R.id.iView);
        Picasso.with(this).load("https://s-media-cache-ak0.pinimg.com/736x/ea/63/d5/ea63d56de1af1ae80d0a806fed661e15.jpg").into(mImageView);
        jReq();
        linearLayout=(LinearLayout)findViewById(R.id.lBG);

    }

    
     

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AppController.getInstance().getRequestQueue().getCache().get(URL_JSON_OBJECT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
