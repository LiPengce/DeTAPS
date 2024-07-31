package com.lpc.dtpke;

import com.lpc.common.PairingProvider;
import com.lpc.crypto.AES;
//import com.lpc.crypto.SM4;
import com.lpc.crypto.Hash;
import com.lpc.util.CustomSerializer;
import com.lpc.util.JDBCUtilsByDruid;
import com.lpc.util.PropertiesUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class DTPKE implements PairingProvider{
    private final Pairing bp = PairingProvider.pairing;
    private final Field Zr=PairingProvider.Zr;
    private final Field G1=PairingProvider.G;
    private final Field G2=PairingProvider.G2;
    private final int m=12; //最大公证人数量

    public Map<String,Map> Setup(int lambda){
        Element g=G1.newRandomElement().getImmutable();
        Element h=G2.newRandomElement().getImmutable();
        Element gamma=this.Zr.newRandomElement().getImmutable();
        Element alpha=this.Zr.newRandomElement().getImmutable();

        //D是m-1个元素
        Element[]D=new Element[m-1];
        for(int i=0;i<m-1;i++){
            D[i]=this.Zr.newRandomElement().getImmutable();
        }

        //MK
        Map<String,Element>MK=new HashMap<>();
        MK.put("g",g);
        MK.put("gamma",gamma);
        MK.put("alpha",alpha);

        //EK
        Map<String,Object>EK=new HashMap<>();
        Element u=g.powZn(alpha.mul(gamma)).getImmutable();
        Element v= bp.pairing(g,h).powZn(alpha).getImmutable();
        Element h_alpha=h.powZn(alpha).getImmutable();
        //E为h^(α*γ^i)集合,i∈[1,2*m-1]
        List<Element> E=new ArrayList<>();
        for(int i=1;i<2*m;i++){
            E.add(h.powZn(alpha.mul(gamma.powZn(this.Zr.newElement(i)))).getImmutable());
        }
        EK.put("m",this.m);
        EK.put("u",u);
        EK.put("v",v);
        EK.put("h_alpha",h_alpha);
        EK.put("E",E);
        EK.put("D",D);

        //CK
        Map<String,Object>CK=new HashMap<>();
        //定义F集合为 CK中的{h.powZn(γ^i)} [1,m-2]
        List<Element>F=new ArrayList<>();
        for(int i=1;i<=m-2;i++){
            F.add(h.powZn(gamma.powZn(this.Zr.newElement(i))).getImmutable());
        }
        CK.put("m",this.m);
        CK.put("h",h);
        CK.put("F",F);
        CK.put("D",D);

        Map<String,Map>KetMap=new HashMap<>();
        KetMap.put("MK",MK);
        KetMap.put("EK",EK);
        KetMap.put("CK",CK);

        return KetMap;
    }

    public Map<String,Element> Join(Map<String,Element>MK,String pid){
        Element upk=this.Zr.newRandomElement().getImmutable();
        Element g= MK.get("g").getImmutable();
        Element gamma=  MK.get("gamma").getImmutable();
        Element usk=g.powZn(gamma.add(upk).invert()).getImmutable();
        Element uvk=this.Zr.newElement(upk).getImmutable();
        Map<String,Element>Keys=new HashMap<>();
        Keys.put("usk",usk);
        Keys.put("upk",upk);
        Keys.put("uvk",uvk);
        return Keys;
    }

    public List<Object> Encrypt(Map<String,Object>EK, Element[]S, int t, List<Element> ATSsignature) throws Exception {
        //加密结果
        Element u= ((Element) EK.get("u")).getImmutable();
        Element v= ((Element) EK.get("v")).getImmutable();
        Element k=this.Zr.newRandomElement().getImmutable();
        Element[]Hdr=new Element[2];
        Element C1=u.powZn(k).invert().getImmutable();
        Hdr[0]=C1;
        Set<String>thisS=new HashSet<>();
        List<Element>SDmts1=new ArrayList<>();
        for(Element upk:S){
            SDmts1.add(upk);
            thisS.add(Base64.getEncoder().encodeToString(upk.toBytes()));
        }
        Element C2=G2.newOneElement();

        int set_tag = thisS.hashCode();
        String selectSql = "SELECT c2org FROM dtpke_param WHERE set_tag = ?";
        String insertSql = "INSERT INTO dtpke_param (set_tag, c2org) VALUES (?, ?)"; // 插入语句

        try (Connection connection = JDBCUtilsByDruid.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setInt(1, set_tag);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 如果查询有结果
                    String c2org = resultSet.getString("c2org");
                    Element C2org = ((Element) CustomSerializer.deserialize(Hex.decode(c2org))).getImmutable();
                    C2.mul(C2org.powZn(k)).getImmutable();
                } else {
                    System.out.println("没有找到与set_tag对应的结果，进行插入操作");
                    Element[] D = (Element[]) EK.get("D");
                    List<Element>E= (List<Element>) EK.get("E");
                    for (int i = 0; i < m + t - S.length - 1; i++) {
                        SDmts1.add(D[i]);
                    }
                    Element h_alpha = ((Element) EK.get("h_alpha")).getImmutable();
                    Element nogamma = Zr.newOneElement();
                    for (Element element : SDmts1) {
                        nogamma.mul(element);
                    }
                    C2.mul(h_alpha.powZn(nogamma.duplicate()));
                    for (int gammaPower = 1; gammaPower <= m + t - 2; gammaPower++) {
                        C2.mul(E.get(gammaPower - 1).powZn(calculateCombinationsSum(SDmts1, m + t - 1 - gammaPower)));
                    }
                    C2.mul(E.get(m + t - 2));

                    String c2orgToInsert = Hex.toHexString(CustomSerializer.serialize(C2.getImmutable()));

                    C2.powZn(k).getImmutable();
                    System.out.println("c2orgToInsert.length() = " + c2orgToInsert.length());
                    // 插入操作
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        insertStatement.setInt(1, set_tag);
                        insertStatement.setString(2, c2orgToInsert);
                        insertStatement.executeUpdate();
                        System.out.println("插入成功");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Hdr[1] = C2;
        List<Object> Encresult = new ArrayList<>();
        //把公证人的upk都添加进去
        for (Element element : S) {
            Encresult.add(element);
        }
        //t值
        Encresult.add(t); //-5
        //添加k
        Encresult.add(k); //-4
        //添加C1
        Encresult.add(C1); //-3
        //添加C2
        Encresult.add(C2);  //-2
        Element K = v.powZn(k).getImmutable();
        System.out.println("K = " + K);
        byte[]AESkeyBytes=new byte[16];
        byte[] bytes = Hash.calculateSM3Hash(K.toBytes());

        System.arraycopy(bytes,0,AESkeyBytes,0,16);
        SecretKey AESkey = new SecretKeySpec(AESkeyBytes, "AES");
        String encryptedATSsign = AES.encrypt(ATSsignature, AESkey);
//        添加加密的ATSsign
        Encresult.add(encryptedATSsign); //-1

        return Encresult;
    }

    public boolean ValidateCT(Map<String,Object>EK,Element[]S,int t,Element[]Hdr){
        Element u= ((Element) EK.get("u")).getImmutable();
        Element C1plus=u.invert().getImmutable();
        List<Element>SDmts1=new ArrayList<>();
        Set<String>SDmts1str=new HashSet<>();
        for(Element upk:S){
            SDmts1.add(upk);
            SDmts1str.add(Base64.getEncoder().encodeToString(upk.toBytes()));
        }

        int set_tag = SDmts1str.hashCode();
        System.out.println("set_tag = " + set_tag);
        String selectSql = "select c2org from dtpke_param where set_tag = ?";

        try (Connection connection = JDBCUtilsByDruid.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {

            selectStatement.setInt(1, set_tag);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 如果查询有结果
                    String c2org = resultSet.getString("c2org");
                    Element C2plus = (Element) CustomSerializer.deserialize(Hex.decode(c2org));
                    Element C1=Hdr[0].getImmutable();
                    Element C2=Hdr[1].getImmutable();


                    return bp.pairing(C1,C2plus).equals(bp.pairing(C1plus,C2));
                } else {
                    System.out.println("没有找到与set_tag对应的结果，进行插入操作");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Element> ShareDecrypt(Element usk,Element[]Hdr){
        Element share=bp.pairing(usk,Hdr[1]).getImmutable();
        Element deta=Zr.newRandomElement().getImmutable();
        Element usk2=usk.powZn(deta).getImmutable();
        List<Element>threeTriple=new ArrayList<>();
        threeTriple.add(share);
        threeTriple.add(usk2);
        threeTriple.add(deta);
        return threeTriple;
    }

    public boolean ShareVerify(Map<String,Object>EK,Object[]fullHdr,List<Element>threeTriple,Element uvk){
        System.out.println("uvk = " + uvk);
        Element[]Hdr= (Element[]) fullHdr[2];
        Element C2=Hdr[1].getImmutable();

        Element share=threeTriple.get(0).getImmutable();
        Element usk2=threeTriple.get(1).getImmutable();
        Element delta=threeTriple.get(2).getImmutable();
        Element hPowAlphaGamma=((List<Element>)EK.get("E")).get(0).getImmutable();

        Element v= ((Element) EK.get("v")).getImmutable();
        Element h_alpha= ((Element) EK.get("h_alpha")).getImmutable();

        boolean part1=bp.pairing(usk2,hPowAlphaGamma.mul(h_alpha.powZn(uvk))).isEqual(v.powZn(delta));
        boolean part2=bp.pairing(usk2,C2).isEqual(share.powZn(delta));

        return  part1 && part2;
    }

    //fullHdr包括S,t,Hdr=(C1,C2)
    public List<Object> ShareCombine(Map<String,Object>CK,Object[]fullHdr,Element[]T,List<Element>[]shares,String encryptedATSsignature) throws Exception {
        Element[]S= (Element[]) fullHdr[0];
        int t= (int) fullHdr[1];
        if(T.length!=t){
            System.exit(-1);
        }
        Element[]Hdr= (Element[]) fullHdr[2];
        Element C1=Hdr[0].getImmutable();

        Element[]D= (Element[]) CK.get("D");
        Element h= ((Element) CK.get("h")).getImmutable();
        int m= (int) CK.get("m");
        int s=S.length;
        //SDminusTstr为S∪D（m+t-s-1）-T的Base64编码后的结果
        Set<String>SDminusTstr=new HashSet<>();
        for(Element e:S){
            SDminusTstr.add(Base64.getEncoder().encodeToString(e.toBytes()));
        }
        //D的前m+t-s-1个元素
        for(int i=0;i<m+t-s-1;i++){
            SDminusTstr.add(Base64.getEncoder().encodeToString(D[i].toBytes()));
        }
        for(Element element:T){
            SDminusTstr.remove(Base64.getEncoder().encodeToString(element.toBytes()));
        }

        List<Element>SDminusT=new ArrayList<>();
        for(String element:SDminusTstr){
            SDminusT.add(Zr.newElementFromBytes(Base64.getDecoder().decode(element)).getImmutable());
        }

        List<Element>F= (List<Element>) CK.get("F");
        Element hpts=G.newOneElement();
        //gammaPower是从0到m-2,m-2次另分
        hpts.mul(h.powZn(calculateCombinationsSum(SDminusT,SDminusT.size()-1)));
        for(int gammaPower=1;gammaPower<SDminusT.size()-1;gammaPower++){
            hpts.mul(F.get(gammaPower-1).powZn(calculateCombinationsSum(SDminusT,m-2-gammaPower)));
        }
        hpts.mul(F.get(F.size()-1)).getImmutable();

        Element cts=this.Zr.newOneElement().getImmutable();
        for(String element:SDminusTstr){
            cts=cts.mul(Zr.newElementFromBytes(Base64.getDecoder().decode(element))).getImmutable();
        }

        Element Aggr=Aggregate(t-1,t,shares,T).getImmutable();
        Element K=bp.pairing(C1,hpts).mul(Aggr).powZn(cts.invert()).getImmutable();
        byte[]AESkeyBytes=new byte[16];  //SM4密钥长度为128bit
        byte[] bytes = Hash.calculateSM3Hash(K.toBytes());
        System.out.println("K = " + K);
        System.arraycopy(bytes,0,AESkeyBytes,0,16);
        SecretKey AESkey = new SecretKeySpec(AESkeyBytes, "AES");
        List<Object> ATSsignature = AES.decrypt(encryptedATSsignature, AESkey);

        return ATSsignature;
    }

    public Element Aggregate(int j,int l,List<Element>[]shares,Element[]T){
        if(j==0){
            return shares[l-1].get(0).getImmutable();
        }else {
            return Aggregate(j-1,j,shares,T).div(Aggregate(j-1,l,shares,T)).powZn(T[l-1].sub(T[j-1]).invert()).getImmutable();
        }
    }

    // 计算组合的乘积之和
    private static Element calculateCombinationsSum(List<Element> elements, int chooseCount) {
        if (elements.isEmpty() || chooseCount <= 0) {
            throw new IllegalArgumentException("元素列表不能为空且 chooseCount 必须大于 0");
        }

        Element sum = elements.get(0).getField().newZeroElement();
        int n = elements.size();
        int[] indices = new int[chooseCount];
        for (int i = 0; i < chooseCount; i++) {
            indices[i] = i;
        }

        while (indices[0] < n - chooseCount + 1) {
            sum.add(getProduct(elements, indices));
            int t = chooseCount - 1;
            while (t != 0 && indices[t] == n - chooseCount + t) {
                t--;
            }
            indices[t]++;
            for (int i = t + 1; i < chooseCount; i++) {
                indices[i] = indices[i - 1] + 1;
            }
        }

        return sum.getImmutable();
    }

    // 根据索引获取乘积
    private static Element getProduct(List<Element> elements, int[] indices) {
        Element product = elements.get(indices[0]).duplicate();
        for (int i = 1; i < indices.length; i++) {
            product.mul(elements.get(indices[i]));
        }
        return product.getImmutable();
    }
}
