package com.example.purav.androidfacebook;

import java.util.ArrayList;

/**
 * Created by sourabh on 8/10/17.
 * Stores the structure of the Post
 */

public class PostStructure {
    public String content;
    public String name;
    public boolean see_all_comments;
    public ArrayList<CommentStructure> comments;
    public PostStructure(String name, String content){
        this.name = name;
        this.content = content;
        comments = new ArrayList<CommentStructure>();
        see_all_comments = false;
    }
}
