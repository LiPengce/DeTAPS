package com.lpc.contestworkspringboot;

import com.lpc.cryptography.base.Hash;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author lpc
 * @version 1.0
 * @since 2024-05-12
 */
public class Test {

    @org.junit.Test
    public void test() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sk.detaps"));
        String s = Hash.calculateSM3Hash("123");
        bufferedWriter.write(s,0,s.length());
        bufferedWriter.close();
    }
}
