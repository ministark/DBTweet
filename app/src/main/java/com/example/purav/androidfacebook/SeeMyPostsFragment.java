package com.example.purav.androidfacebook;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class SeeMyPostsFragment extends Fragment {

    static Integer the_limit = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_see_my_posts, container, false);
        showmyposts(view);

        Button more_my_post_btn = (Button) view.findViewById(R.id.more_my_posts);

        more_my_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmyposts(view);
            }
        });

        return view;
    }

    private void showmyposts(final View view){
        // Creating the adapter

        final ListView listview_home = (ListView)view.findViewById(R.id.listview_myposts);

        final ArrayList<PostStructure> temp = new ArrayList<>();
        final AdapterPost adapter = new AdapterPost(view, getActivity(), temp);
        if(listview_home.getAdapter() != null) {
            Integer count = listview_home.getAdapter().getCount();
            Log.e("Maybe Error", "Starting");
            for (int i = 0; i < count; i++) {
                temp.add((PostStructure) listview_home.getAdapter().getItem(i));
            }
        }
        listview_home.setAdapter(adapter);
        Log.e("Maybe Error", "Done yaaay");

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = urlx + "/SeeMyPosts";
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
                                if(post_list.length() < the_limit){
                                    Toast.makeText(getActivity(), "Showing all Posts Possible", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                                listview_home.setSelection(temp.size() - post_list.length());
                                Log.e("SeeMyPosts", "Added new suggestions..! yaaay");
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

                Integer the_offset = 0;
                if(listview_home.getAdapter() != null);
                    the_offset = listview_home.getAdapter().getCount();
                params.put("offset", the_offset.toString());
                params.put("limit", the_limit.toString());
                return params;
            }
        };

        queue.add(str);
    }
}
