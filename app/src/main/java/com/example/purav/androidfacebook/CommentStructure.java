package com.example.purav.androidfacebook;

/**
 * Created by sourabh on 8/10/17.
 * This stores the structure of the comment
 */

public class CommentStructure {
    public static int min_comments = 1;
    public String name;
    public String content;
    public CommentStructure (String name, String content){
        this.name = name;
        this.content = content;

    }
}
