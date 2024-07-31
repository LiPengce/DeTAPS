package com.lpc.cryptography.util;

import com.lpc.cryptography.common.PairingProvider;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ManagerSignerGroups implements PairingProvider {
    private static Map<String, List<Element>> groupsRiMap = new ConcurrentHashMap<>();

    public static boolean add(String gid,Element Ri){
        List<Element> list = groupsRiMap.computeIfAbsent(gid, k -> Collections.synchronizedList(new ArrayList<>()));
        return list.add(Ri);
    }

    public static int getRiCountbyGid(String gid){
        List<Element> list = groupsRiMap.get(gid);
        return list==null? 0:list.size();
    }

    public static List<Element>getRiListbyGid(String gid){
        return groupsRiMap.get(gid);
    }
}
