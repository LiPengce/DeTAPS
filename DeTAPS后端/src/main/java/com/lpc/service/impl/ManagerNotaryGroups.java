package com.lpc.service.impl;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerNotaryGroups {
    private static Map<String, Integer> groupsMap = new ConcurrentHashMap<>();

    public static void record(String DeTAPS_sign,int TracerId){
        groupsMap.put(DeTAPS_sign,TracerId);
    }

    public static Integer getTracerId(String DeTAPS_sign){
        return groupsMap.get(DeTAPS_sign);
    }
}
