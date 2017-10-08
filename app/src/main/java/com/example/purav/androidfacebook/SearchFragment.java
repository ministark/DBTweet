package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 7/10/17.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, COUNTRIES);
        final AutoCompleteTextView textView = view.findViewById(R.id.user_input_autocomplete);
        textView.setAdapter(adapter);

        textView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.e("Autocomplete", "Entered function OnKeyListener");

                UpdateAutocompleteOptions(view, adapter);

                return true;
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View arg1, int position, long arg3) {
                //... your stuff
                String textbox = textView.getText().toString();

                Toast.makeText(getActivity(), "You request is being sent: " + textbox, Toast.LENGTH_LONG).show();

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


        return view;
    }

    private void UpdateAutocompleteOptions(View view, final ArrayAdapter<String> adapter){

        AutoCompleteTextView the_input = (AutoCompleteTextView) view.findViewById(R.id.user_input_autocomplete);

        final String input = the_input.getText().toString().split(",")[0];

        String url = urlx + "/SearchUser";

        StringRequest str = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("The response is ", response);
                        try{
                            JSONObject jobj = new JSONObject(response);
                            Boolean successlogin = jobj.getBoolean("status");
                            if(successlogin.equals(true)){
                                JSONArray User_list = jobj.getJSONArray("data");
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
                                adapter.clear();
                                adapter.addAll(user_list);
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", input);
                return params;
            }
        };
    }



    public void cancelsearch(View view){
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.user_input_autocomplete);
        textView.setText("");
        textView.setSelected(false);
        LinearLayout mybuttons = (LinearLayout) view.findViewById(R.id.user_finalized);
        mybuttons.setVisibility(View.GONE);
    }

}


