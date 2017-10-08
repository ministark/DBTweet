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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home , container, false);
        // Temporary post
        ArrayList<PostStructure> temp = new ArrayList<PostStructure>(2);
        temp.add(new PostStructure("Me","First Post"));
        temp.add(new PostStructure("Also Me","Second Post"));
        // Creating the adapter
        AdapterPost adapter = new AdapterPost(getActivity(),temp);
        ListView listview_home = (ListView)rootView.findViewById(R.id.listview_home);
        Log.e("Shit",Boolean.toString(listview_home == null) );
        listview_home.setAdapter(adapter);
        return rootView;
    }
}