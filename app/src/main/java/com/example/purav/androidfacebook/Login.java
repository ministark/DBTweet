package com.example.purav.androidfacebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }

    private void sendJSONArrayRequest(final String username, final String password) throws JSONException
    {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="http://192.168.0.110:8080/newmessenger/Login";

        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try{
                            JSONObject jobj = new JSONObject(response);
                        }
                        catch (JSONException jsonex){
                            Log.e("Error in Json Parsing", "Shit");
                        }
                        Log.e("done with response", "yaaay");
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error In HTTP Response", "Shit");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", username);
                params.put("password", password);
                return params;
            }
        };

// Add the request to the RequestQueue.

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        queue.add(str);

    }

    public void onClickLogin(View v)
    {
        Toast.makeText(getApplicationContext(), "Clicked on Button", Toast.LENGTH_LONG).show();
        EditText editusername = (EditText) findViewById(R.id.username) ;
        String username = editusername.getText().toString();
        EditText editpassword = (EditText) findViewById(R.id.password) ;
        String password = editpassword.getText().toString();

        Log.e("The username is: ",username);
        Log.e("The password is: ",password);

        try {
            sendJSONArrayRequest(username, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
