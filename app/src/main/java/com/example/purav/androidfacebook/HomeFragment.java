package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 7/10/17.
 */

import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;


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
        // Temporary post
        temp.add(new PostStructure("Me","First Post"));
        temp.get(0).comments.add(new CommentStructure("By Me", "My comment"));
        temp.get(0).comments.add(new CommentStructure("By Me", "The Hidden Comment"));
        temp.add(new PostStructure("Also Me","Second Post"));
        temp.get(1).comments.add(new CommentStructure("By Me again", "My comment again"));
//        for (PostStructure s: temp) {
//            Log.i("name", s.name);
//            Log.i("content", s.content);
//            for (CommentStructure c : s.comments) {
//                Log.i("comment", c.content + c.name);
//            }
//        }
        // Creating the adapter
        AdapterPost adapter = new AdapterPost(rootView, getActivity(),temp);
        ListView listview_home = (ListView)rootView.findViewById(R.id.listview_home);
        listview_home.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp.get(position).see_all_comments = !temp.get(position).see_all_comments;
                ((AdapterPost)parent.getAdapter()).notifyDataSetChanged();
            }
        });
        listview_home.setAdapter(adapter);
        return rootView;
    }
}