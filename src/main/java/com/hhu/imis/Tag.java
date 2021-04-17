package com.hhu.imis;

import java.util.List;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;

public class Tag {
    public String deviceId;
    public String tagName;
    public int timestamp;
    public String value;
    static private List<Tag> list;

    public Tag(String deviceId,String tagName,int timestamp,String value){
        this.deviceId = deviceId;
        this.tagName = tagName;
        this.timestamp = timestamp;
        this.value = value;
    }

    /*
    static public List<Tag> toTagList(JsonArray raw) throws Exception{
        raw.forEach(unit -> tagForEach(unit));
    }

    static private void tagForEach(JSONObject unit) throws Exception{
        Tag _unit = new Tag(unit.get("deviceId").toString(), unit.get("tagName").toString(), 
        unit.getInteger("timestamp"), unit.get("value").toString());

        list.add(_unit);
    }
    */
}
