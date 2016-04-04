package com.example.administrator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/3/26.
 */
//进行md5加密算法
public class Md5Utils {
    public static String getMd5Digest(String password){
        String afterpassword="";

        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");//得到md5的信息摘要器
            byte[] digest = md5.digest(password.getBytes());//通过摘要器将传过来的信息转化成字节数据
            StringBuffer result=new StringBuffer();
            for ( byte s:digest){
                int ret=s&0xff;//将数据与上一个2进制的数
                String s1 = Integer.toHexString(ret);
                if (s1.length()==1){
                    result.append("0");//如果长度为1就在数的前面加上一个零补齐两位  因为它里面必须都是两位的十六进制数
                }
                result.append("result");
            }
            afterpassword=result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  afterpassword;
    }
    public  static String  getAppMd5Digest(String apklocation){
        String afterencyp="";
        byte[]digest= null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            File file = new File(apklocation);
            //如果文件不存在就拷贝文件
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = fis.read(buffer, 0, 1024)) != -1) {
                    md.update(buffer, 0, len);
                }
                fis.close();
                digest = md.digest();
            }
            //如果文件存在就从病毒库中找是否有与之对应的病毒软件 有则杀之
            if (digest!=null){
                {
                    StringBuffer result= new StringBuffer();//变长的string
                    for (byte b : digest) {
                        int ret = b&0xFF;
                        String hexstring = Integer.toHexString(ret);
                        if (hexstring.length()==1) {
                            result.append("0");
                        }
                        result.append(hexstring);
                    }
                    System.out.println(result);
                    afterencyp=result.toString();
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  afterencyp;
    }
}
