package com.hhu.imis;

import java.util.List;
import java.util.TimerTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

//线程实例类
//该线程用来操作获取数据及写入到数据库的实例

public class Instance extends TimerTask {
    private String deviceId;
    private String deviceNo;
    private String deviceCatagry;
    public Instance(String deviceId,String deviceNo,String deviceCatagry){
        this.deviceId = deviceId;
        this.deviceNo = deviceNo;
        this.deviceCatagry = deviceCatagry;
    }
    public void run(){
        try{
            System.out.println("正在向"+Global.globalSetting.apiUrl+"发起连接，deviceNo="+deviceNo + "deviceId="+deviceId);
            JSONObject obj = RESTful.Restful(Global.globalSetting.apiUrl, "deviceNo="+deviceNo);
            //JSONObject obj =JSON.parseObject("{\"mesId\":\"mesId1\",\"timestamp\":\"11111111\",\"tagsCount\":\"4\",\"tags\":[{\"deviceId\":\"M1\",\"tagName\":\"A1\",\"timestamp\":\"11111111\",\"value\":\"12\"},{\"deviceId\":\"M1\",\"tagName\":\"A2\",\"timestamp\":\"11111112\",\"value\":\"13\"},{\"deviceId\":\"M1\",\"tagName\":\"A3\",\"timestamp\":\"11111113\",\"value\":\"14\"},{\"deviceId\":\"M1\",\"tagName\":\"A4\",\"timestamp\":\"11111114\",\"value\":\"15\"}]}");
            List<Tag> tags = Tag.toTagList(deviceId,obj.getJSONArray("tags"),deviceCatagry,deviceNo);
            //System.out.println(tags);
            Global.dbConn.addRecord(tags);
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
