package com.hhu.imis;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tag {
    public String deviceId;
    public String tagName;
    public int timestamp;
    public String value;

    public Tag(String deviceId,String tagName,int timestamp,String value){
        this.deviceId = deviceId;
        this.tagName = tagName;
        this.timestamp = timestamp;
        this.value = value;
    }
    
    static public List<Tag> toTagList(JSONArray raw) throws Exception{
        List<Tag> list = new ArrayList<Tag>();

        for(int i=0;i<raw.size();i++){
            JSONObject unit = raw.getJSONObject(i);
            Tag _unit = new Tag(unit.get("deviceId").toString(), unit.get("tagName").toString(), 
            unit.getInteger("timestamp"), unit.get("value").toString());
            list.add(_unit);
        }
        
        return list;
    }
    
}
