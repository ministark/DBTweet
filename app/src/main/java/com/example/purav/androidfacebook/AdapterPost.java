package com.example.purav.androidfacebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.purav.androidfacebook.CommentStructure.min_comments;
import static com.example.purav.androidfacebook.Login.urlx;
import static java.lang.Math.min;

/**
 * Created by sourabh on 8/10/17.
 * This Adapter links the post structure to the list view by populating the layout with the
 * appropriate value
 */

public class AdapterPost extends ArrayAdapter<PostStructure> {
    private final Context context;
    private final ArrayList<PostStructure> posts;


    public AdapterPost(View view, Context context, ArrayList<PostStructure> posts) {
        super(context, R.layout.template_post, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int fposition = position;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view =inflater.inflate(R.layout.template_post, parent, false);
        TextView textView1 = (TextView) view.findViewById(R.id.post_name);
        textView1.setText(posts.get(position).name);
        TextView textView2 = (TextView) view.findViewById(R.id.post_content);
        textView2.setText(posts.get(position).content);

        TableLayout table = (TableLayout)view.findViewById(R.id.comment_table);
        int c_size = posts.get(position).see_all_comments ? posts.get(position).comments.size():min(min_comments,posts.get(position).comments.size());
        for(int i = 0; i < c_size; i++)
        {
            CommentStructure c = posts.get(position).comments.get(i);
            // Inflate your row "template" and fill out the fields.
            TableRow row = (TableRow)LayoutInflater.from(context).inflate(R.layout.template_comment, null);
            ((TextView)row.findViewById(R.id.comment_content)).setText(c.content);
            ((TextView)row.findViewById(R.id.comment_name)).setText(c.name);
            table.addView(row);
            table.requestLayout();
        }

        Button add_comment = (Button) view.findViewById(R.id.comment_add_button);
        add_comment.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                EditText editusername = (EditText) view.findViewById(R.id.comment_add) ;
                final String content = editusername.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = urlx+ "/NewComment";
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
                                        Toast.makeText(context, "Comment Added", Toast.LENGTH_LONG).show();
                                        posts.get(fposition).comments.add(new CommentStructure("Me", content));
                                        posts.get(fposition).see_all_comments = true;
                                        notifyDataSetChanged();
                                    }
                                    else{
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
                        params.put("postid", posts.get(fposition).postid);
                        params.put("content", content);
                        return params;
                    }
                };

                queue.add(str);

            }
        });
        final Button show_comment = (Button) view.findViewById(R.id.comment_more_button);
        if(posts.get(position).comments.size() > min_comments) {
            show_comment.setText(posts.get(position).see_all_comments ? "Less":"More");
            show_comment.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //Do stuff here
                    posts.get(fposition).see_all_comments = ! posts.get(fposition).see_all_comments;
                    notifyDataSetChanged();
                }
            });
        }
        else
            show_comment.setVisibility(View.GONE);
        return view;
    }
}