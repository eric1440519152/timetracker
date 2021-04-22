package com.hhu.imis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tag {
    public String deviceId;
    public String tagName;
    public String timestamp;
    public String value;
  
    public Tag(String deviceId,String tagName,java.util.Date timestamp,String value){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        this.deviceId = deviceId;
        this.tagName = tagName;
        this.timestamp = sdf.format(timestamp);
        this.value = value;
    }

    static public List<Tag> toTagList(JSONArray raw) throws Exception{
        List<Tag> list = new ArrayList<Tag>();

        for(int i=0;i<raw.size();i++){
            JSONObject unit = raw.getJSONObject(i);
            Tag _unit = new Tag(unit.get("deviceId").toString(), unit.get("tagName").toString(),
            unit.getDate("timestamp"), unit.get("value").toString());
            list.add(_unit);
        }
        
        return list;
    }
    
}
