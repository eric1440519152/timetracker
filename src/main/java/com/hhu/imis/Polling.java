package com.hhu.imis;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Polling extends TimerTask{
    private Map<String,String> deviceStatus = new HashMap<>();
    public void run() {
        try {
            ResultSet set = Global.dbConn.getEvaStatus();

            //依次遍历数据库结果，并匹配和存入deviceStatus
            while(set.next()){
                String deviceId = set.getString("F_EquipmentId");
                String currStatus = set.getString("F_Status");

                Boolean changed = deviceStatus.get(deviceId) == null || !deviceStatus.get(deviceId).equals(currStatus);
                if(changed){
                    //状态改变，重建实例进程
                    System.out.println(deviceId + " 设备状态改变，开始重建进程");
                    
                    if(Global.Timers.get(deviceId) != null){
                        //原来的设备线程存在，先销毁
                        Global.Timers.get(deviceId).cancel();
                        //销毁实例
                        Global.Instances.remove(deviceId);
                        Global.Timers.remove(deviceId);

                        Timer timer = new Timer();
                        Instance instance = new Instance(deviceId);

                        switch(currStatus){
                            case "0":
                                System.out.println("创建设备关机监控线程，五秒后启动首次监控，"+Global.globalSetting.offInterval+"秒执行一次监控。");
                                //关机状态
                                timer.schedule(instance, 5*1000, Global.globalSetting.offInterval*1000);
                                break;
                            case "1":
                                System.out.println("创建设备开机监控线程，五秒后启动首次监控，"+Global.globalSetting.onInterval+"秒执行一次监控。");
                                //开机状态
                                timer.schedule(instance, 5*1000, Global.globalSetting.offInterval*1000);
                                break;
                        }
                        
                    }
                }

                //状态未改变，什么也不做，等待主线程轮询。
            }
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}
