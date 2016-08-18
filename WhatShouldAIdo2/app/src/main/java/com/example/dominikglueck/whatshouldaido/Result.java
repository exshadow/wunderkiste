package com.example.dominikglueck.whatshouldaido;

/**
 * Created by D3v1lishGrin on 18.08.2016.
 */

public class Result {
    private String id;
    private String content;

    public Result(){

    }

    public String getId(){
        return this.id;
    }


    public String getContent(){

        return this.content;
    }


    public void setId(String id){
        this.id = id;
    }


    public void setContent(String content){

        this.content = content;
    }

    public String toString() {
        return this.id+" "+this.content;
    }
}
