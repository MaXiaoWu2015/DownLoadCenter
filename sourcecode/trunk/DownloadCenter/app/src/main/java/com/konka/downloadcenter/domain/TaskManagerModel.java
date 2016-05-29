package com.konka.downloadcenter.domain;

import android.content.ContentValues;

/**
 * Created by xiaowu on 2016-5-19.
 */
public class TaskManagerModel {
    public final static String ID = "id";
    public final static String NAME = "name";
    public final static String URL = "url";
    public final static String PATH = "path";
    public final static String STATE="state";
    public final static String SOFARBYTES ="sofarbytes" ;
    public final static String TOTALBYTES="totalbytes";

     //任务状态
    public final static int STOP=1;
    public final static int WAIT=2;
    public final static int DOWNLOADING=3;
    public final static int FAILED=4;
    public final static int COMPLETED=5;

    private int id;
    private String name;
    private String url;
    private String path;
    private int state;
    private int sofarbytes;
    private int totalbytes;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSofarbytes() {
        return sofarbytes;
    }

    public void setSofarbytes(int sofarbytes) {
        this.sofarbytes = sofarbytes;
    }

    public int getTotalbytes() {
        return totalbytes;
    }

    public void setTotalbytes(int totalbytes) {
        this.totalbytes = totalbytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ContentValues toCotentValues() {
        ContentValues values=new ContentValues();

        values.put(ID,id);
        values.put(PATH,path);
        values.put(URL,url);
        values.put(NAME,name);
        values.put(STATE,state);
//        values.put(SOFARBYTES,sofarbytes);
        values.put(TOTALBYTES,totalbytes);
        return values;
    }
}
