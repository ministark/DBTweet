package com.example.purav.androidfacebook;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by sourabh on 8/10/17.
 * Stores the structure of the Post
 */

public class PostStructure {
    public String content;
    public String postid;
    public String name;
    public Bitmap bitmap;
    public boolean see_all_comments;
    public ArrayList<CommentStructure> comments;
    public PostStructure(String name, String content){
        this.name = name;
        this.content = content;
        comments = new ArrayList<CommentStructure>();
        see_all_comments = false;
        bitmap = null;
    }
}
