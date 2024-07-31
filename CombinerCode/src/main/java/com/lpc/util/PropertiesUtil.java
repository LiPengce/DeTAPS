package com.lpc.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadProperties(String filePath) {

        Properties prop=new Properties();

        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath)){
            if(inputStream==null){
                System.out.println("unable to find "+filePath);
                return null;
            }
            prop.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    // 将属性保存到指定路径的文件
    public static void saveProperties(Properties prop, String filePath) {

        try (OutputStream outputStream=new FileOutputStream(filePath)){
            prop.store(outputStream,null);
        }catch (IOException io){
            io.printStackTrace();
        }
    }
}

