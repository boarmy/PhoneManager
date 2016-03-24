package com.example.administrator.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/24.
 */

//践行httpURL通讯是的解析工具类
public class HTTPUtils {
    //解析向服务器获取版本号时的json解析
    public static String getTextFromStream(InputStream is){
        String result="";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len=-1;
        try {
            while ((len=is.read(bytes,0,1024))!=-1){
                baos.write(bytes,0,len);
            }
            result=baos.toString("GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
}
