package com.hhu.imis;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        Config config = new Config();
        System.out.println(config.dbUrl);
        ToDB db = new ToDB("mysql://www.hzeen.cn:3306/project", "project", "projectproject123");
        
        db.addRecord(new Tag("111", "A1", new Date(), "13"));
        try{
            JSONObject obj = RESTful.Restful("https://www.hzeen.cn/DemoData.txt", "city=1&key=8c17a52abbb71895e1efa8c1b3893573");
            System.out.println(obj.get("reason"));
        }catch(Exception e){
            System.out.println(e);
        }
        //mvn assemblySystem.out.println( "Hello World!" );
    }
}
