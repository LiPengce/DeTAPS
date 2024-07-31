package com.lpc.blockchain;

import org.web3j.codegen.SolidityFunctionWrapperGenerator;

import java.util.Arrays;

public class SolidityFunctionWrapperGeneratorTest {
    public static void generateClass(String abiFile,String binFile,String generateFile){
        String[] args = Arrays.asList(
                "-a",abiFile,
                "-b",binFile,
                "-p","",
                "-o",generateFile
        ).toArray(new String[0]);

        SolidityFunctionWrapperGenerator.main(args);
    }

    public static void main(String[] args) {
        generateClass("D:\\JetBrainsProjects\\JavaProjects\\contestwork-jdk11\\src\\main\\java\\com\\lpc\\blockchain\\DeTAPSContract.abi","D:\\JetBrainsProjects\\JavaProjects\\contestwork-jdk11\\src\\main\\java\\com\\lpc\\blockchain\\DeTAPSContract.bin","D:\\JetBrainsProjects\\JavaProjects\\contestwork-jdk11\\src\\main\\java\\com\\lpc\\blockchain");
    }
}
