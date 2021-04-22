package com.hhu.imis;

import java.util.Timer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        
        //程序初始化，开始读取配置文件，并将全局设置自动装载到Global类中
        //实例化Config后，Global类中的全局设置被赋值供调用
        new Config();
        //初始化数据库链接，整个程序共用一个数据库接口
        ToDB dbConn = new ToDB(Global.globalSetting.dbUrl, Global.globalSetting.username, Global.globalSetting.password);
        Global.dbConn = dbConn;

        //创建轮询Timer进程
        Timer timer = new Timer();
        //启动轮询线程
        System.out.println("5秒后启动轮询线程，每"+Global.globalSetting.pollingInterval+"轮询一次");
        timer.schedule(new Polling(), 5000, Global.globalSetting.pollingInterval*1000);
    }
}
