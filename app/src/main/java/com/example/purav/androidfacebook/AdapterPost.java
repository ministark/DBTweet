package com.example.purav.androidfacebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sourabh on 8/10/17.
 */

public class AdapterPost extends ArrayAdapter<PostStructure> {
    private final Context context;
    private final ArrayList<PostStructure> posts;

    public AdapterPost(Context context, ArrayList<PostStructure> posts) {
        super(context, R.layout.template_post, posts);
        this.context = context;
        this.posts = posts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.template_post, parent, false);
        TextView textView1 = (TextView) convertView.findViewById(R.id.post_name);
        textView1.setText(posts.get(position).name);
        TextView textView2 = (TextView) convertView.findViewById(R.id.post_content);
        textView2.setText(posts.get(position).content);
        return convertView;
    }
}