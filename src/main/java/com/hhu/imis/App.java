package com.hhu.imis;

import com.alibaba.fastjson.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Config config = new Config();
        System.out.println(config.dbUrl);

        try{
            JSONObject obj = RESTful.Restful("https://www.hzeen.cn/DemoData.txt", "city=1&key=8c17a52abbb71895e1efa8c1b3893573");
            System.out.println(obj.get("reason"));
        }catch(Exception e){
            System.out.println(e);
        }
        //mvn assemblySystem.out.println( "Hello World!" );
    }
}
