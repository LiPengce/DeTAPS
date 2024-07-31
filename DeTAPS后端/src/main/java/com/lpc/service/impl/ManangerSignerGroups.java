package com.lpc.service.impl;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManangerSignerGroups {
    private static Map<String,Integer> groupsMap = new ConcurrentHashMap<>();

    public static void record(String gid,Integer CombinerId){
        groupsMap.put(gid,CombinerId);
    }

    public static Integer getCombinerId(String gid){
        return groupsMap.get(gid);
    }

}
