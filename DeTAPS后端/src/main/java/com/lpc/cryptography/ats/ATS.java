package com.lpc.cryptography.ats;

import com.lpc.cryptography.common.PairingProvider;
import com.lpc.cryptography.base.Hash;
import com.lpc.cryptography.util.ManagerSignerGroups;
import com.lpc.cryptography.util.PropertiesUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class ATS {
    private final Field Zr= PairingProvider.Zr;
    private final Field G=PairingProvider.G;
    private final Element g=PairingProvider.g;
    private int t=5;
    //pk,sk都为16进制0x字符串
    //会把id，pk，sk写到数据库中
    /**
     *
     * @param skstr 0x字符串
     * @param m  消息m
     * @param gid
     * @return <Ri,zi>
     */
    public Map.Entry<Element,Element> Sign(String skstr, String m,String gid) {
        Element r=Zr.newRandomElement().getImmutable();
        Element Ri=g.powZn(r).getImmutable();
        if(!ManagerSignerGroups.add(gid, Ri)){
            System.out.println("add Ri fail");
            System.exit(-1);
        }else{
            System.out.println("add Ri succeed");
        }
        Element R=G.newOneElement().getImmutable();

        while (t!=ManagerSignerGroups.getRiCountbyGid(gid));

        List<Element> list = ManagerSignerGroups.getRiListbyGid(gid);
        for (Element e : list) {
            R = R.mul(e).getImmutable();
        }

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
        StringBuffer csb=new StringBuffer();
        csb.append(pk);
        csb.append(R);
        csb.append(m);

        byte[] cBytes= csb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] chash = Hash.calculateSM3Hash(cBytes);
        Element c=Zr.newElementFromHash(chash,0,chash.length).getImmutable();
        Element sk=Zr.newElementFromBytes(Hex.decode(skstr)).getImmutable();
        Element zi=r.add(sk.mul(c)).getImmutable();

        Map.Entry<Element,Element> share=new AbstractMap.SimpleEntry<>(Ri,zi);
        return share;
    }

    public List<Element>Combine(List<Map.Entry<Element,Element>> shares, int t){
        if(shares.size()!=t){
            System.out.println("shares.length!=t");
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

}
