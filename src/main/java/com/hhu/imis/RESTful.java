package com.hhu.imis;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RESTful {
    public static JSONObject Restful(String url,String parama) throws Exception{
        String temp = httpPost(url, parama);
        System.out.println(temp);
        return JSON.parseObject(temp); 
    }

    public static String httpPost(String url,String param) throws Exception{
        URL Url = new URL(url);
        HttpURLConnection connection = null;
        InputStream IS = null;
        OutputStream OS = null;
        BufferedReader BR = null;

        connection = (HttpURLConnection) Url.openConnection();
        connection.setRequestMethod("POST");
        connection.setReadTimeout(60000);
        connection.setConnectTimeout(15000);

        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
        //connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        // 通过连接对象获取一个输出流

        OS = connection.getOutputStream();
        OS.write(param.getBytes());

        if(connection.getResponseCode() == 200){
            //如果成功HTTP请求返回读取Response里的Body
            IS = connection.getInputStream();
            BR = new BufferedReader(new InputStreamReader(IS,"utf8"));

            StringBuffer SB = new StringBuffer();
            String temp = null;
            while((temp = BR.readLine()) != null){
                SB.append(temp);
                SB.append("\n\r");
            }
            return SB.toString();
        }
        throw new Exception("返回内容为空");
    }
}
