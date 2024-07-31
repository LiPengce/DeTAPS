package com.lpc.net;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.util.encoders.Hex;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SocketTCPServer extends Thread{
    private int t ;
    private static final Map<String, List<Element>> groupSignatures = new HashMap<>();
    private Field G;
    private static final Map<String, ReentrantLock> locks = new HashMap<>();
    private static final Map<String, Condition> conditions = new HashMap<>();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private int port;

    public SocketTCPServer(int port,int t,Field G) {
        this.port = port;
        this.t = t;
        this.G=G;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("accept one successfully");
                executorService.submit(() -> handleConnection(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            System.out.println("start to handle");
            byte[] buff = new byte[32 + 128]; // 假定gid为32字节的16进制字符串
            int readlen;
            while ((readlen = inputStream.read(buff)) != -1) {
                if (readlen == 160) { // gid + Ri
                    byte[] gidBytes = new byte[32];
                    System.arraycopy(buff, 0, gidBytes, 0, 32);
                    String gid = Hex.toHexString(gidBytes);
                    Element Ri = G.newElementFromBytes(Arrays.copyOfRange(buff, 32, 160)).getImmutable();
                    System.out.println("gid = " + gid);
                    System.out.println("Ri = " + Ri);

                    ReentrantLock lock = locks.computeIfAbsent(gid, k -> new ReentrantLock());
                    Condition condition = conditions.computeIfAbsent(gid, k -> lock.newCondition());
                    List<Element> list;

                    lock.lock();
                    try {
                        list = groupSignatures.computeIfAbsent(gid, k -> new ArrayList<>());
                        list.add(Ri);
                        if (list.size() == this.t) {
                            System.out.println("list.size() = " + list.size());
                            condition.signalAll(); // 唤醒等待这个gid的所有线程
                        } else {
                            while (list.size() < this.t) {
                                System.out.println("list.size() = " + list.size());
                                condition.await(); // 等待直到list大小达到t
                            }
                        }
                    } finally {
                        lock.unlock();
                    }

                    Element R = G.newOneElement();
                    lock.lock();
                    try {
                        if (list.size() == t) {
                            for (Element el : list) {
                                R.mul(el);
                            }
                            outputStream.write(R.toBytes()); // 发送合并后的R
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


