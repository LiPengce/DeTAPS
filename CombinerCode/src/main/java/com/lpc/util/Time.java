package com.lpc.util;

import com.lpc.common.PairingProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Time  {
    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        Random random = new Random();
        return "lpc"+sdf.format(date)+ random.nextInt();
    }

}
