package com.hhu.imis;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Global {
    static public Map<String,String> tagLastTime = new HashMap<>();
    static public ToDB dbConn;
    static public Config globalSetting;
    static public Timer Polling;
    static public Map<String,Instance> Instances =new HashMap<>();
    static public Map<String,Timer> Timers = new HashMap<>();
}
