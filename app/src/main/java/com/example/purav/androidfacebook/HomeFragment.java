package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 7/10/17.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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


public class HomeFragment extends Fragment {
    public final ArrayList<PostStructure> temp;
    public HomeFragment(){
        temp = new ArrayList<PostStructure>();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home , container, false);
        // Creating the adapter
        final AdapterPost adapter = new AdapterPost(rootView, getActivity(),temp);
        ListView listview_home = (ListView)rootView.findViewById(R.id.listview_home);
        listview_home.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                temp.get(position).see_all_comments = !temp.get(position).see_all_comments;
                Log.e("Focused","focus");
                ((AdapterPost)parent.getAdapter()).notifyDataSetChanged();
            }
        });
        listview_home.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url = urlx + "/SeePosts";
        Log.i("urll", url);
        StringRequest str = new StringRequest(Request.Method.GET, url,
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
                                    boolean has_img = post.has("image");
                                    Log.d("Bool image", String.valueOf(has_img));
                                    if (has_img) {
                                        String encodedImage = post.getString("image");
                                        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                        Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                        temp.get(temp.size() - 1).bitmap = image;
                                    }
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
                            jsonex.printStackTrace();
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
        );
        queue.add(str);

        return rootView;
    }
}