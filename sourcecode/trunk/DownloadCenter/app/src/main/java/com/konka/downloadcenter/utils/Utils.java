package com.konka.downloadcenter.utils;

import android.content.Context;

import java.text.DecimalFormat;

/**
 * Created by xiaowu on 2016-5-20.
 */
public class Utils {

    public static String getDataSize(long size) {

        DecimalFormat formater = new DecimalFormat("#.#");
        DecimalFormat formater1=new DecimalFormat("#");
        if (size<1024)
        {
            return 1 + "K";
        }else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater1.format(kbsize) + "K";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "M";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "G";
        } else {
            return "未知大小";
        }
    }

    public static String getSpeedStr(int speed)
    {
        DecimalFormat formater = new DecimalFormat("#.#");
        if(speed>=1024)
        {
            float temp=speed*1.0f/1024;
            return "("+formater.format(temp)+"M/s"+")";
        }
        return "("+speed+"KB/s"+")";
    }
    /**
     * 从资源文件获取字符串
     * */
    public static String getStringFromRes(Context context,int strId)
    {
        return context.getResources().getString(strId);
    }


}