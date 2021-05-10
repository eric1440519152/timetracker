package com.hhu.imis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Tag {
    public String deviceId;
    public String deviceNo;
    public String catagoryId;
    public String tagName;
    public String timestamp;
    public String value;
  
    public Tag(String deviceId,String tagName,String deviceNo,String catagoryId,java.util.Date timestamp,String value){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.deviceId = deviceId;
        this.tagName = tagName;
        this.timestamp = sdf.format(timestamp);
        this.value = value;
        this.catagoryId = catagoryId;
        this.deviceNo = deviceNo;
    }

    static public List<Tag> toTagList(String deviceId,JSONArray raw,String catagoryId,String deviceNo) throws Exception{
        List<Tag> list = new ArrayList<Tag>();

        for(int i=0;i<raw.size();i++){
            JSONObject unit = raw.getJSONObject(i);
            Tag _unit = new Tag(deviceId,unit.get("tagName").toString(),deviceNo,catagoryId,
            unit.getDate("timestamp"), unit.get("value").toString());
            list.add(_unit);
        }
        
        return list;
    }
    
}
