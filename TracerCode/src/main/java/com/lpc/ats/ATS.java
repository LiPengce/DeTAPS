package com.lpc.ats;

import com.lpc.common.PairingProvider;
import com.lpc.crypto.Hash;
import com.lpc.util.JDBCUtilsByDruid;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.sql.*;
import java.util.*;

public class ATS {
    private final Field Zr= PairingProvider.Zr;
    private final Field G=PairingProvider.G;
    private final Element g=PairingProvider.g;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    //pk,sk都为16进制0x字符串
    //会把id，pk，sk写到数据库中
    public List<Element> KeyGen(int lambda,int n)  {
        List<Element> pk=new ArrayList<>();

        String SQL="insert into signer_key (sk,pk) values (?,?)";
        try (Connection conn = JDBCUtilsByDruid.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            for (int i = 0; i < n; i++) {
                Element sk = Zr.newRandomElement().getImmutable();
                Element pk_ = g.powZn(sk).getImmutable();
                pk.add(pk_);
                String skstr = Hex.toHexString(sk.toBytes());
                String pkstr = Hex.toHexString(pk_.toBytes());
                pstmt.setString(1, skstr);
                pstmt.setString(2, pkstr);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pk;
    }

    public List<Element>Combine(List<Map.Entry<Element,Element>> shares, int t){
        if(shares.size()!=t){
            System.out.println("signer count != t");
            System.exit(-1);
        }

        Element R=G.newOneElement();
        Element z=Zr.newZeroElement();
        for(Map.Entry<Element,Element> entry:shares){
            R.mul(entry.getKey());
            z.add(entry.getValue());
        }
        List<Element>sigma=new ArrayList<>();

        sigma.add(R.getImmutable());
        sigma.add(z.getImmutable());

        return sigma;
    }

//    public int[] Trace(List<Element>pk,String m,List<Object>sigma){
//        if(!Verify(pk,m,sigma)){
//            System.out.println("verify fail !");
//            System.exit(-1);
//        }
//        return (int[]) sigma.get(2);
//    }


}
