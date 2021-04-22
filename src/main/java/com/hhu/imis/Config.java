package com.hhu.imis;

import java.io.File;
import java.io.FileInputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Config {
    public String dbUrl;
    public String username;
    public String password;
    public String ApiUrl;
    
    public Config(){
        String path = "."+ File.separator + "Config.json";
        File file = new File(path);
        try{
            FileInputStream is = new FileInputStream(file);
            byte[] bytes = new byte[is.available()];

            is.read(bytes);
            is.close();

            String s = new String(bytes);
            JSONObject setting = JSON.parseObject(s);

            dbUrl = setting.getString("dbUrl");
            username = setting.getString("username");
            password = setting.getString("password");
            ApiUrl = setting.getString("ApiUrl");

            Global.globalSetting = this;
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
