package com.example.purav.androidfacebook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.min;

/**
 * Created by sourabh on 8/10/17.
 * This Adapter links the post structure to the list view by populating the layout with the
 * appropriate value
 */

public class AdapterPost extends ArrayAdapter<PostStructure> {
    private final Context context;
    private final ArrayList<PostStructure> posts;
    private final View view;

    public AdapterPost(View view, Context context, ArrayList<PostStructure> posts) {
        super(context, R.layout.template_post, posts);
        this.context = context;
        this.posts = posts;
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.template_post, parent, false);
        TextView textView1 = (TextView) convertView.findViewById(R.id.post_name);
        textView1.setText(posts.get(position).name);
        TextView textView2 = (TextView) convertView.findViewById(R.id.post_content);
        textView2.setText(posts.get(position).content);

        TableLayout table = (TableLayout)convertView.findViewById(R.id.comment_table);
        int c_size = posts.get(position).see_all_comments ? posts.get(position).comments.size():min(1,posts.get(position).comments.size());
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


        return convertView;
    }

}