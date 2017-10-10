package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 7/10/17.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.purav.androidfacebook.Login.urlx;

public class PostFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_post , container, false);
        Button post_add = (Button) view.findViewById(R.id.post_add_button);
        post_add.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                final EditText editusername = view.findViewById(R.id.post_content);
                final String content = editusername.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = urlx+ "/CreatePost";
                StringRequest str = new StringRequest(Request.Method.POST,  url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("The response is ", response);
                                // Display the first 500 characters of the response string.
                                try{
                                    JSONObject jobj = new JSONObject(response);
                                    Boolean successlogin = jobj.getBoolean("status");
                                    if(successlogin.equals(true)){
                                        String userid = jobj.getString("data");
                                        Toast.makeText(getActivity(), "Post Added", Toast.LENGTH_LONG).show();
                                        editusername.setText("");
                                    }
                                    else{
                                        Toast.makeText(getActivity(), "Could't add Post", Toast.LENGTH_LONG).show();
                                    }
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
                        params.put("content", content);
                        return params;
                    }
                };
                queue.add(str);
            }
        });
        return view;
    }
}