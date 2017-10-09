package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 7/10/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.purav.androidfacebook.Login.urlx;

public class SearchFragment extends Fragment {

    private static final String[] COUNTRIES = new String[] {
            "Belgium, Brussels", "00128, Purav, ppuravgandhi@gmail.com", "00235, Sourabh, sourabh@gmail.com", "00435, Purva, purav@gmail.com", "Spain"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search , container, false);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        final AutoCompleteTextView textView = view.findViewById(R.id.user_input_autocomplete);
        textView.setAdapter(adapter);

        Log.e("OnclickListener", "setting");

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String x = textView.getText().toString();
                Log.e("String in inputbox", x);
                if (x.length() >= 3){
                    textView.dismissDropDown();
                    Integer index = adapter.getPosition(x);
                    String indx = index.toString();
                    if(index==-1){
                        Log.e("Started fuct", "Autocomplete");
                        UpdateAutocompleteOptions(view, adapter);
                        Log.e("printing index", indx);
                        Log.e("Ended fuct", "Autocomplete");
                        LinearLayout mybuttons = (LinearLayout) view.findViewById(R.id.user_finalized);
                        mybuttons.setVisibility(View.GONE);
                    }
                }
            }
        });

        Log.e("OnclickListener", "has been set");

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View arg1, int position, long arg3) {

                String textbox = textView.getText().toString();

                Toast.makeText(getActivity(), "You request is being sent: " + textbox.split(",")[0], Toast.LENGTH_SHORT).show();

                LinearLayout mybutton = (LinearLayout) view.findViewById(R.id.user_finalized);
                mybutton.setVisibility(View.VISIBLE);
            }
        });

        Button cancelbutton = (Button) view.findViewById(R.id.cancel_button);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelsearch(view);
            }
        });

        final Button followuidbutton = (Button) view.findViewById(R.id.follow_button);

        followuidbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text_in_box = textView.getText().toString();
                followuid(text_in_box);
            }
        });


        // COde for see user
        final Button showpostsbutton = (Button) view.findViewById(R.id.showposts_button);
        showpostsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text_in_box = textView.getText().toString();
                showposts(text_in_box,view);
            }
        });
        return view;
    }

    private void UpdateAutocompleteOptions(final View view, final ArrayAdapter<String> adapter){

        AutoCompleteTextView the_input = (AutoCompleteTextView) view.findViewById(R.id.user_input_autocomplete);

        final String input = the_input.getText().toString().split(",")[0];

        String url = urlx + "/SearchUser";

        Log.e("In updatlist function"," Creating Request");

        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("The response is ", response);
                        try{
                            JSONObject jobj = new JSONObject(response);
                            Boolean successlogin = jobj.getBoolean("status");
                            Log.e("testing 0", "parsed boolean");
                            if(successlogin.equals(true)){
//                                JSONObject jtemp = jobj.getJSONObject("data");
//                                Log.e("testing1", jtemp.toString());
                                JSONArray j_temp = jobj.getJSONArray("data");
                                JSONArray User_list = j_temp.getJSONArray(0);
                                Log.e("testing2", User_list.toString());
                                ArrayList<String> user_list = new ArrayList<String>();
                                String uid, name, email;
                                String entry;
                                for (int i = 0; i < User_list.length(); i++) {
                                    JSONObject row = User_list.getJSONObject(i);
                                    uid = row.getString("uid");
                                    name = row.getString("name");
                                    email = row.getString("email");
                                    entry = uid + ", " + name + ", " + email ;
                                    Log.e("AutoComplete- entries", entry);
                                    user_list.add(entry);
                                }
                                AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.user_input_autocomplete);
                                adapter.clear();
                                adapter.addAll(user_list);

//Force the adapter to filter itself, necessary to show new data.
//Filter based on the current text because api call is asynchronous.
                                adapter.getFilter().filter(textView.getText(), textView);
                                Log.e("List Strings", user_list.toString());
                                textView.showDropDown();
                                Log.e("AutoComplete", "Added new suggestions..! yaaay");

                            }
                            else{
                                Toast.makeText(getActivity(), "Couldn't Do It :(", Toast.LENGTH_LONG).show();
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
                params.put("uid", input);
                Log.e("params", "put and request sent");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        queue.add(str);
        Log.e("Volley", "request sent");

    }



    public void cancelsearch(View view){
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.user_input_autocomplete);
        textView.setText("");
        textView.setSelected(false);
        LinearLayout mybuttons = (LinearLayout) view.findViewById(R.id.user_finalized);
        mybuttons.setVisibility(View.GONE);
    }

    public void followuid(String uidx){

        final String uid = uidx.split(",")[0];

        String url = urlx + "/Follow";
        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Follow", "The response is " + response);
                        try{
                            JSONObject jobj = new JSONObject(response);
                            Boolean successlogin = jobj.getBoolean("status");
                            if(successlogin.equals(true)){
                                Toast.makeText(getActivity(), jobj.getString("data"), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uid", uid);
                Log.e("params", "put and request sent");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(str);
        Log.e("Volley", "Follow request sent");

    }

    public void showposts(String uid1, View view){
        // Creating the adapter
        final String uid = uid1.split(",")[0];
        final ArrayList<PostStructure> temp = new ArrayList<>();
        final AdapterPost adapter = new AdapterPost(view, getActivity(),temp);
        ListView listview_home = (ListView)view.findViewById(R.id.listview_user);
        listview_home.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = urlx + "/SeeUserPosts";
        Log.i("urll", url);
        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("The response is ", response);
                        try{
                            JSONObject jobj = new JSONObject(response);
                            Boolean successlogin = jobj.getBoolean("status");
                            if(successlogin.equals(true)){
                                JSONArray post_list = jobj.getJSONArray("data");
                                for (int i = 0; i < post_list.length(); i++) {
                                    JSONObject post = post_list.getJSONObject(i);
                                    temp.add(new PostStructure(post.getString("uid"),post.getString("text")) );
                                    temp.get(temp.size()-1).postid = post.getString("postid");
                                    JSONArray comment_list = post.getJSONArray("Comment");
                                    for (int j = 0; j < comment_list.length(); j++){
                                        JSONObject comm = comment_list.getJSONObject(j);
                                        temp.get(temp.size()-1).comments.add(new CommentStructure(comm.getString("name"), comm.getString("text")) );
                                    }

                                }
                                adapter.notifyDataSetChanged();
                                Log.e("AutoComplete", "Added new suggestions..! yaaay");
                            }
                            else{
                                Toast.makeText(getActivity(), "Couldn't Do It :(", Toast.LENGTH_LONG).show();
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
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                Log.e("params", uid);
                return params;
            }
        };
        queue.add(str);
    }
}


