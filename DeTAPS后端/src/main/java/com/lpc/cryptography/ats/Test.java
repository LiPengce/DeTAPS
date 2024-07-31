package com.lpc.cryptography.ats;

import com.lpc.cryptography.common.PairingProvider;
import com.lpc.cryptography.util.PropertiesUtil;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.util.encoders.Hex;

import java.sql.*;
import java.util.*;
import java.util.concurrent.FutureTask;

public class Test implements PairingProvider {

    public static void main(String[] args) throws Exception {
        ATS ats=new ATS();
//        List<Element> pk = ats.KeyGen(512, 20);
        int[]C={1,5,7,11,13};
        String gid="6a607c9ed6c6f9d2626b8902a083";
        String skstr1="6c6f9d2626b8848a221657e119172200883d50cc";
        String skstr5="409e5257a68c9acbc383a70152f522f717242e0c";
        String skstr7="622b26a607c9edb0be5364e81c1b1b18d7ded719";
        String skstr11="045dac78f15cfe14d05a70b2a81902a083965712";
        String skstr13="081bf909b16c803701fdd3466cdcb0e4b1b880cd";
        String m="lpc";
        FutureTask<Map.Entry<Element, Element>> signer1 = new FutureTask<>(new SignCallable(ats,skstr1, m ,gid));
        FutureTask<Map.Entry<Element, Element>> signer5 = new FutureTask<>(new SignCallable(ats,skstr5, m,gid));
        FutureTask<Map.Entry<Element, Element>> signer7 = new FutureTask<>(new SignCallable(ats, skstr7, m, gid));
        FutureTask<Map.Entry<Element, Element>> signer11 = new FutureTask<>(new SignCallable(ats, skstr11, m,gid));
        FutureTask<Map.Entry<Element, Element>> signer13 = new FutureTask<>(new SignCallable(ats, skstr13, m, gid));
        Thread t1=new Thread(signer1);
        Thread t5=new Thread(signer5);
        Thread t7=new Thread(signer7);
        Thread t11=new Thread(signer11);
        Thread t13=new Thread(signer13);
        t1.start();
        t5.start();
        t7.start();
        t11.start();
        t13.start();

        List<Map.Entry<Element,Element>> shares = Collections.synchronizedList(new ArrayList<>());
        shares.add(signer1.get());
        shares.add(signer5.get());
        shares.add(signer7.get());
        shares.add(signer11.get());
        shares.add(signer13.get());

        int t=5;
        List<Element> sigma = ats.Combine(shares,t);
        Properties prop = PropertiesUtil.loadProperties("database.properties");
        String url = prop.getProperty("db.url");
        String user = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");
        String quertSQL="select pk from signer order by id asc ";
        List<Element>pk=new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url,user,password);
             PreparedStatement statement = connection.prepareStatement(quertSQL)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                pk.add(G.newElementFromBytes(Hex.decode(resultSet.getString("pk"))).getImmutable());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
