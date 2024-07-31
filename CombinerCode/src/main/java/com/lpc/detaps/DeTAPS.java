package com.lpc.detaps;

import com.lpc.ats.ATS;
import com.lpc.blockchain.CustomGasProvider;
import com.lpc.blockchain.DeTAPSContract;
import com.lpc.common.PairingProvider;
import com.lpc.crypto.COM;
import com.lpc.crypto.Hash;
import com.lpc.crypto.PKE;
import com.lpc.crypto.SIG;
import com.lpc.dtpke.DTPKE;
import com.lpc.kase.KASE;
import com.lpc.net.SocketTCPServer;
import com.lpc.pojo.NotarialRecord;
import com.lpc.util.Time;
import com.lpc.util.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings({"all"})
public class DeTAPS implements PairingProvider{

    private static final Field Zr=PairingProvider.Zr;
    private static final Field G=PairingProvider.G;
    private static final Element g=PairingProvider.g;
    private static final Element h=PairingProvider.h;

    private ATS ats=new ATS();
    private DTPKE dtpke=new DTPKE();
    private KASE kase=new KASE();

    private int n;
    private int t;
    private int n1;
    private int n2;
    private int n3;
    private int tpie;
    private static final int MAXGroups=100; //KASE

    private static int[]SignerGroups;

    static {
        SignerGroups= IntStream.rangeClosed(1,MAXGroups).toArray();
    }

    public List<String> pids=new ArrayList<>();

    public DeTAPS(){}

    public DeTAPS(int n, int t, int n1, int n2, int n3, int tpie) {
        this.n = n;
        this.t = t;
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
        this.tpie = tpie;
    }

    public void Setup(int lambda, int n, int n1, int n2, int n3, int t, int tpie) throws Exception {
        System.out.println("Setup start !");
        //pk不包括t
        List<Element> pk = ats.KeyGen(lambda, n);

        Map<String,Object>pkPlus=new HashMap<>();
        pkPlus.put("pk",pk);
        Element psi1=Zr.newRandomElement().getImmutable();
        Element V0=g.powZn(psi1).getImmutable();
        Element V1=g.powZn(Zr.newElement(tpie)).mul(h.powZn(psi1)).getImmutable();
        pkPlus.put("V0",V0);
        pkPlus.put("V1",V1);

        Element psi2=Zr.newRandomElement().getImmutable();
        Element T0=g.powZn(psi2).getImmutable();
        Element T1=g.powZn(Zr.newElement(t)).mul(h.powZn(psi2)).getImmutable();
        pkPlus.put("T0",T0);
        pkPlus.put("T1",T1);
        List<Element>tauList=new ArrayList<>();
        for(int i=0;i<n;i++){
            tauList.add(Zr.newRandomElement().getImmutable());
        }
        List<Element>hList=new ArrayList<>();
        for(int i=0;i<n;i++){
            hList.add(g.powZn(tauList.get(i)).getImmutable());
        }
        pkPlus.put("hList",hList);

        //承诺方案COM,pk_t是含有t的：{t,pk1-pkn}
        List<Element>pk_t=new ArrayList<>();
        pk_t.addAll(pk);
        pk_t.add(0,Zr.newElement(t).getImmutable());
        byte[] pkbytes = Serialize.serialize(pk_t);

        Element x = Zr.newElementFromBytes(pkbytes).getImmutable();
        Element rpk=Zr.newRandomElement().getImmutable();

        Map<String, Element>publictriple  = COM.Setup();
        Element compk = COM.Commit(x, rpk,publictriple).getImmutable();

        //DTPKE
        Map<String, Map> dtpkekeys = dtpke.Setup(lambda);

        Map<String,Element>mk=dtpkekeys.get("MK");
        Map<String,Object>ck =dtpkekeys.get("CK");
        Map<String,Object>ek= dtpkekeys.get("EK");

        //Combiner的签名密钥对，pks为签名公钥，sks为签名私钥
        ECPublicKey[]pks_set=new ECPublicKey[n1];
        ECPrivateKey[]sks_set=new ECPrivateKey[n1];
        KeyPair[]SIGkeys=new KeyPair[n1];
        for(int j=0;j<n1;j++) {
            SIGkeys[j]= SIG.generateKeyPair();
            pks_set[j]= (ECPublicKey) SIGkeys[j].getPublic();
            sks_set[j]= (ECPrivateKey) SIGkeys[j].getPrivate();
        }

        //Combiner的加密密钥
        AsymmetricCipherKeyPair[]CombinerPKEkeypairs=new AsymmetricCipherKeyPair[n1];
        String[]pkec_set= new String[n1];
        String[]skec_set= new String[n1];
        String sql="insert into pkec (pke) values (?)";

        //Tracer的加密密钥,pket_set和sket_set分别为每个Tracer的公钥和私钥
        AsymmetricCipherKeyPair[] TracerPKEkeypairs = new AsymmetricCipherKeyPair[n2];
        String[] pket_set = new String[n2];
        String[] sket_set = new String[n2];
        String sql2 = "insert into pket (pke) values (?)";
        try (Connection conn = JDBCUtilsByDruid.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int j = 0; j < n1; j++) {
                CombinerPKEkeypairs[j] = PKE.generateKeyPair();
                ECPublicKeyParameters aPublic = (ECPublicKeyParameters) CombinerPKEkeypairs[j].getPublic();
                pkec_set[j] = Hex.toHexString(SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(aPublic).getEncoded());
                statement.setString(1, pkec_set[j]);
                statement.executeUpdate();
                ECPrivateKeyParameters aPrivate = (ECPrivateKeyParameters) CombinerPKEkeypairs[j].getPrivate();
                skec_set[j] = Hex.toHexString(PrivateKeyInfoFactory.createPrivateKeyInfo(aPrivate).getEncoded());
            }

            PreparedStatement statement2 = conn.prepareStatement(sql2);
            for (int j = 0; j < n2; j++) {
                TracerPKEkeypairs[j] = PKE.generateKeyPair();
                ECPublicKeyParameters aPublic = (ECPublicKeyParameters) TracerPKEkeypairs[j].getPublic();
                pket_set[j] = Hex.toHexString(SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(aPublic).getEncoded());
                statement2.setString(1,pket_set[j]);
                statement2.executeUpdate();
                ECPrivateKeyParameters aPrivate = (ECPrivateKeyParameters) TracerPKEkeypairs[j].getPrivate();
                sket_set[j] = Hex.toHexString(PrivateKeyInfoFactory.createPrivateKeyInfo(aPrivate).getEncoded());
            }
        }

        //key为pid,值为upk,并保存到数据库当中
        Map<String,Element>upk_set=new HashMap<>();
        Map<String,Element>usk_set=new HashMap<>();
        Map<String,Element>uvk_set=new HashMap<>();
        String pid;

        String insertSQL="insert into notary_key (pid,upk,usk) values (?,?,?)";
        List<Element>AllNotaries=new ArrayList<>();
        try (Connection connection = JDBCUtilsByDruid.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {
             for (int j = 0; j < n3; j++) {
                 pid = Hash.calculateSM3Hash(Time.getCurrentTimestamp());
                 this.pids.add(pid);
                 Map<String, Element> notarieskeypair = dtpke.Join(mk, pid);
                 upk_set.put(pid, notarieskeypair.get("upk"));
                 usk_set.put(pid, notarieskeypair.get("usk"));
                 uvk_set.put(pid, notarieskeypair.get("uvk"));
                 AllNotaries.add(notarieskeypair.get("upk"));
                 statement.setString(1,pid);
                 statement.setString(2,Hex.toHexString(notarieskeypair.get("upk").toBytes()));
                 statement.setString(3,Hex.toHexString(notarieskeypair.get("usk").toBytes()));
                 statement.executeUpdate();
             }
        }catch (Exception e){
            e.printStackTrace();
        }

        //kase Setup
        Map<String, Object> kaseparam = kase.Setup(lambda);
        Element[]PubK= (Element[]) kaseparam.get("PubK");
        Element g=PubK[0].getImmutable();

        //kase KeyGen
        Map<String, Element> keypair = kase.KeyGen(lambda, g);
        Element msk=keypair.get("msk").getImmutable();
        Element mpk=keypair.get("pk").getImmutable();

        //Extract产生聚合密钥
        Element kagg = kase.Extract(msk,SignerGroups, PubK).getImmutable();
        Properties kaggProp=new Properties();
        kaggProp.setProperty("kagg",Hex.toHexString(kagg.toBytes()));
        PropertiesUtil.saveProperties(kaggProp,"kagg.properties");

        List<Object>skc_set[]=new ArrayList[n1];
        for (int i = 0; i < n1; i++) {
            skc_set[i] = new ArrayList<>();
        }

        for(int j=0;j<skc_set.length;j++) {
            skc_set[j].add(pkPlus);  //0
            skc_set[j].add(skec_set[j]); //1
            skc_set[j].add(t); //2
            skc_set[j].add(tpie); //3
            skc_set[j].add(ek); //4
            skc_set[j].add(rpk); //5
            skc_set[j].add(psi1); //6
            skc_set[j].add(psi2); //7
            skc_set[j].add(tpie); //8
            //保存每个skc到文件
            Properties skcProp=new Properties();
            skcProp.setProperty("skc",Hex.toHexString(Serialize.serialize(skc_set[j])));
            skcProp.setProperty("sks",Hex.toHexString(sks_set[j].getEncoded()));
            PropertiesUtil.saveProperties(skcProp,"Combiner"+(j+1)+".properties");
        }

        List<Object>skt_set[]=new ArrayList[n2];
        for (int i = 0; i < n2; i++) {
            skt_set[i] = new ArrayList<>();
        }

        //每个skt_set的顺序为ske,ck,pk
        for(int j=0;j<skt_set.length;j++){
            skt_set[j].add(sket_set[j]); //0
            skt_set[j].add(ck); //1
            skt_set[j].add(pk); //2
            skt_set[j].add(t); //3
            skt_set[j].add(tauList); //4
            Properties sktProp=new Properties();
            sktProp.setProperty("skt",Hex.toHexString(Serialize.serialize(skt_set[j])));
            PropertiesUtil.saveProperties(sktProp,"Tracer"+(j+1)+".properties");
        }

        Map<String,Object>PK=new HashMap<>();
        PK.put("compk",compk);
        PK.put("ek",ek);
        PK.put("upkSet",AllNotaries);
        PK.put("pks_set",pks_set);
        PK.put("pkec_set",pkec_set);
        PK.put("pket_set",pket_set);
        PK.put("mpk",mpk);
        PK.put("pkPlus",pkPlus);
        PK.put("PubK",PubK);

        Properties PKprop=new Properties();
        PKprop.setProperty("PK",Hex.toHexString(Serialize.serialize(PK)));
        PropertiesUtil.saveProperties(PKprop,"PK.properties");
    }

    public void Combine() throws Exception {
        System.out.println("Combine start!");

        //PK
        Properties PKprop = PropertiesUtil.loadProperties("PK.properties");
        String PKstr = PKprop.getProperty("PK");
        Map<String, Object> PK = (Map<String, Object>) Serialize.deserialize(Hex.decode(PKstr));
        //upkSet
        List<Element> AllNotaries = (List<Element>) PK.get("upkSet");
        int n3=AllNotaries.size();
        //skc
        int CombinerIndex=1;
        Properties CombinerProp = PropertiesUtil.loadProperties("Combiner" + CombinerIndex + ".properties");
        String skcstr = CombinerProp.getProperty("skc");
        List<Object> skc = (List<Object>) Serialize.deserialize(Hex.decode(skcstr));
        int t= (int) skc.get(2);

        System.out.println();
        System.out.println("t = " + t);
        System.out.println();

        int port=9999;
        //开始与通信线程

        new SocketTCPServer(port,t,G).start();

        String sksStr = CombinerProp.getProperty("sks");
        ECPrivateKey sks = SIG.recoverPrivateKey(Hex.decode(sksStr));
        Element[] PubK = (Element[]) PK.get("PubK");

        AsymmetricKeyParameter key = PrivateKeyFactory.createKey(Hex.decode((String) skc.get(1)));
        ECPrivateKeyParameters ske = (ECPrivateKeyParameters) key;

        Map<String, Object> pkPlus = (Map<String, Object>) skc.get(0);
        List<Element>hList= (List<Element>) pkPlus.get("hList");

        //区块链
        String rpcUrl = "http://47.96.177.120:8545";
        String contractAddress="0x6f08ceb8c1356f71c7e150d4b0e889c3c098f82b";
        final int chainID=12345;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("UTC--2024-05-27T13-37-46.855227516Z--241f39599c888107485bef04f141e17a31ad77ec");
        if (inputStream == null) {
            throw new RuntimeException("Resource file not found");
        }

        // 将输入流写入临时文件
        File tempFile = File.createTempFile("keystore", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        String password_="123";
        Web3j web3j= Web3j.build(new HttpService(rpcUrl));
        Credentials credentials = WalletUtils.loadCredentials(password_,tempFile.getAbsolutePath());
        RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);

        BigInteger gasPrice = BigInteger.valueOf(5000000000L);
        BigInteger gasLimit = BigInteger.valueOf(18000000L); // 设置合适的Gas限制
        CustomGasProvider customGasProvider=new CustomGasProvider(gasPrice,gasLimit);
        DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j,rawTransactionManager,customGasProvider);
        System.out.println("load complete");

        // 设置事件监听器
        BigInteger combinerIDValue = BigInteger.valueOf(CombinerIndex);
        String combinerIDTopic = "0x" + String.format("%064x", combinerIDValue);
        String encodedEventSignature = EventEncoder.encode(DeTAPSContract.SIGNSHARESUBMITTED_EVENT);

        EthBlockNumber result = web3j.ethBlockNumber().send();
        BigInteger currentBlockNumber = result.getBlockNumber();
        System.out.println("当前区块号: " + currentBlockNumber);

        // 计算从当前最新的区块向前的10个区块
        DefaultBlockParameter startBlock = new DefaultBlockParameterNumber(currentBlockNumber.subtract(BigInteger.TEN.multiply(new BigInteger("5"))));

        // 创建 EthFilter 实例
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contract.getContractAddress())
                .addSingleTopic(encodedEventSignature)
                .addSingleTopic(combinerIDTopic);

//        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contract.getContractAddress())
//                .addSingleTopic(encodedEventSignature)
//                .addSingleTopic(combinerIDTopic);

        web3j.ethLogFlowable(filter).subscribe(log -> {
            Event event = DeTAPSContract.SIGNSHARESUBMITTED_EVENT;
            // event参数为(uint combinerId, string gid, uint shareCount)
            List<Type> Values = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
            BigInteger combinerId= (BigInteger) Values.get(0).getValue();
            String Gid= (String) Values.get(1).getValue();
            BigInteger shareCount = (BigInteger) Values.get(2).getValue();
            System.out.println("combinerId = " + combinerId);
            System.out.println("Gid = " + Gid);
            System.out.println("shareCount = " + shareCount);
            System.out.println("get one signShare");
            System.out.println("*****************");

            int count = shareCount.intValue();
            List<String>encryptedshares = contract.getSignShares(combinerId, Gid).send();
            System.out.println("encryptedshares.size() = " + encryptedshares.size());
            // 检查是否达到阈值并执行相应操作
            if (count == t && encryptedshares.size()==t) {
                System.out.println("get shares Success");
                List<Element> Notries = null;
                String m = "";
                String gid="";
                List<Element>group_pkSet=new ArrayList<>();
                //首先进行解密
                List<Entry<Element, Element>> shares = new ArrayList<>();
                for (String encShare : encryptedshares) {
                    byte[] decdata=null;
                    try {
                        decdata = PKE.Dec(ske, encShare);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    List<Object> plainText = (List<Object>) CustomSerializer.deserialize(decdata);
                    group_pkSet.add(G.newElementFromBytes(Hex.decode((String) plainText.get(0))).getImmutable());
                    List<Element>share= (List<Element>) plainText.get(2);
                    shares.add(new AbstractMap.SimpleEntry<>(share.get(0),share.get(1))); //把每个share添加进shares
                    if ("".equals(m)) {
                        m = (String) plainText.get(1);
                        Notries = (List<Element>) plainText.get(3);
                    }
                    if("".equals(gid)){
                        gid= (String) plainText.get(4);
                    }
                }

                List<Element> pk = (List<Element>) pkPlus.get("pk");
                List<String> collect = pk.stream().map(k -> Hex.toHexString(k.toBytes())).collect(Collectors.toList());
                int n=pk.size();
                int[]Signers=new int[t];
                int index=0;
                for(Element element:group_pkSet){
                    Signers[index++]=pk.indexOf(element)+1;
                }
                //从小大小顺序排序
                Arrays.sort(Signers);
                List<Element> sigma = ats.Combine(shares,t);
                Map<String, Object> ek = (Map<String, Object>) skc.get(4);
                int tpie = (int) skc.get(3);
                List<Object> Encresult = dtpke.Encrypt(ek, Notries.toArray(new Element[0]), tpie, sigma);
                Element mpk = (Element) PK.get("mpk");
                Set<Element> N = new HashSet<>(Notries);
                //包括c1,c2,{ind}集合
                List<String> Nstr = N.stream().map(element -> Base64.getEncoder().encodeToString(element.toBytes())).collect(Collectors.toList());
                Collections.sort(Nstr);
                List<byte[]> keywords = N.stream().map(element -> element.toBytes()).collect(Collectors.toList());

                try (Connection connection = JDBCUtilsByDruid.getConnection()) {
                    String insertSQL = "insert into detaps_sign (gid,m,signature) values (?,?,null)";
                    try (PreparedStatement statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                        statement.setString(1, gid);
                        statement.setString(2, m);
                        //插入数据
                        statement.executeUpdate();

                        // 获取自增主键 id
                        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                long id = generatedKeys.getLong(1);
                                Map<String, Object> kase_enc = kase.Encrypt(keywords, (int) id, g, mpk, PubK);
                                Element c1 = (Element) kase_enc.get("c1");
                                Element c2 = (Element) kase_enc.get("c2");
                                List<Element> inds = (List<Element>) kase_enc.get("cws");

                                List<Object> pi = new ArrayList<>();
                                Element V0 = ((Element) pkPlus.get("V0")).getImmutable();
                                Element V1 = ((Element) pkPlus.get("V1")).getImmutable();
                                Element psi1 = ((Element) skc.get(6)).getImmutable();
                                List<Element> proof1_2_1 = ZeroKnowledgeProof.Proof1_2_1(V0, psi1, g);
                                int[] bSet = new int[AllNotaries.size()];
                                for (Element upk : Notries) {
                                    if (AllNotaries.contains(upk)) {
                                        bSet[AllNotaries.indexOf(upk)] = 1;
                                    }
                                }
                                List<Element> proof1_2_2 = ZeroKnowledgeProof.Proof1_2_2(V1, g, h, bSet, psi1);
                                Element[] hNSet = new Element[AllNotaries.size()];
                                for (int i = 0; i < hNSet.length; i++) {
                                    hNSet[i] = G.newRandomElement().getImmutable();
                                }
                                Element R = ((Element) sigma.get(0)).getImmutable();
                                StringBuilder cSB = new StringBuilder();
                                cSB.append(pk);
                                cSB.append(R);
                                cSB.append(m);
                                //拼接得到c的byte数组
                                byte[] cBytes = cSB.toString().getBytes(StandardCharsets.UTF_8);
                                byte[] chash = Hash.calculateSM3Hash(cBytes);
                                Element c = Zr.newElementFromHash(chash, 0, chash.length).getImmutable();

                                List<Element> proof1_3 = ZeroKnowledgeProof.Proof1_3(g, h, bSet,hList);
                                Element z = ((Element) sigma.get(1)).getImmutable();

                                int[] bSet2 = new int[n];
                                for (int Signerindex : Signers) {
                                    bSet2[Signerindex - 1] = 1;
                                }
                                List<Element> proof2_1 = ZeroKnowledgeProof.Proof2_1(g, R, z, c, pk, bSet2);


                                Element T0 = ((Element) pkPlus.get("T0")).getImmutable();
                                Element psi2 = ((Element) skc.get(7)).getImmutable();
                                List<Element> proof2_2_1 = ZeroKnowledgeProof.Proof2_2_1(T0, psi2, g);

                                Element T1 = ((Element) pkPlus.get("T1")).getImmutable();
                                List<Element> proof2_2_2 = ZeroKnowledgeProof.Proof2_2_2(T1, g, h, psi2, bSet2);

                                List<Element> proof2_3 = ZeroKnowledgeProof.Proof2_3(g, h, bSet2);

                                List<Element>pk_t=new ArrayList<>();
                                pk_t.addAll(pk);
                                pk_t.add(0,Zr.newElement(t));
                                byte[] pkbytes = Serialize.serialize(pk_t);
                                Element x = Zr.newElementFromBytes(pkbytes).getImmutable();
                                Element compk = ((Element) PK.get("compk")).getImmutable();
                                Element rpk = ((Element) skc.get(5)).getImmutable();
                                List<Element> proof3 = ZeroKnowledgeProof.Proof3(g, h, x, compk, rpk);

                                int length = Encresult.size();
                                Element k = ((Element) Encresult.get(length - 4)).getImmutable();
                                Element C1 = ((Element) Encresult.get(length - 3)).getImmutable();
                                Element C2 = ((Element) Encresult.get(length - 2)).getImmutable();
                                String encryptedATSsign = (String) Encresult.get(length - 1);
                                Element u = ((Element) ek.get("u")).getImmutable();
                                List<Element> proof4 = ZeroKnowledgeProof.Proof4(C1, u, k);
                                if(keywords.size()!=inds.size()){
                                    System.out.println("Combine fail");
                                    System.exit(-1);
                                }
                                List<List<Element>>proof5=new ArrayList<>();
                                for(int i=0;i<keywords.size();i++) {
                                    byte[]keyword=keywords.get(i);
                                    Element ind=inds.get(i).getImmutable();
                                    byte[] wbytes = Hash.calculateSM3Hash(keyword);
                                    Element keywordElement = G.newElementFromHash(wbytes, 0, wbytes.length);
                                    Element k2 = (Element) kase_enc.get("k");
                                    Element g1 = PubK[1].getImmutable();
                                    Element gn = PubK[(PubK.length - 1) / 2].getImmutable();
                                    List<Element> proof5_part = ZeroKnowledgeProof.Proof5(ind, keywordElement, g, k2, g1, gn);
                                    proof5.add(proof5_part);
                                }
                                pi.add(proof1_2_1);
                                pi.add(proof1_2_2);
                                pi.add(proof1_3);
                                pi.add(proof2_1);
                                pi.add(proof2_2_1);
                                pi.add(proof2_2_2);
                                pi.add(proof2_3);
                                pi.add(proof3);
                                pi.add(proof4);
                                pi.add(proof5);

                                List<Object> DTPKEcipherText = new ArrayList<>();
                                for (int i = 0; i < length - 5; i++) {
                                    Element e= ((Element) Encresult.get(i)).getImmutable();
                                    DTPKEcipherText.add(e);
                                }
                                DTPKEcipherText.add(tpie);
                                DTPKEcipherText.add(C1);
                                DTPKEcipherText.add(C2);
                                DTPKEcipherText.add(encryptedATSsign);

                                //ECDSA签名
                                StringBuilder sb = new StringBuilder();
                                sb.append(m).append(DTPKEcipherText).append(c1).append(c2).append(inds).append(pi);
                                byte[] ECDsignbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
                                String eta = Base64.getEncoder().encodeToString(SIG.generateSignature(ECDsignbytes, sks));

                                List<Object> DeTAPSsign = new ArrayList<>();
                                DeTAPSsign.add(DTPKEcipherText); //List<Object> 0
                                DeTAPSsign.add(c1); //Element 1
                                DeTAPSsign.add(c2); //Element 2
                                DeTAPSsign.add(inds); //List<Element> 3
                                DeTAPSsign.add (pi); //List<Object> 4
                                DeTAPSsign.add(eta); //Base64编码 5
                                byte[] serialize = CustomSerializer.serialize(DeTAPSsign);
                                System.out.println("serialize.length = " + serialize.length);
                                String finalSign = Base64.getEncoder().encodeToString(serialize);

                                System.out.println("finalSign.length() = " + finalSign.length());

                                System.out.println("Success! ");

                                //插入DeTAPS signature
                                String updateSQL = "update detaps_sign set signature = ? WHERE id = ?";
                                try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                                    updateStmt.setString(1, finalSign);
                                    updateStmt.setLong(2, id);
                                    updateStmt.executeUpdate();
                                }
                                // 上传区块链
                                TransactionReceipt txReceipt = contract.submitM_DeTAPSsign(m, finalSign).send();
                                System.out.println(txReceipt.getBlockHash());
                                System.out.println(txReceipt.getTransactionHash());
                                System.out.println(txReceipt.getBlockNumber());

                                System.out.println("send blockchain");
                            }
                        }
                    }
                }catch (Exception e){
                     e.printStackTrace();
                }
            }
        },error -> {
            error.printStackTrace(); // 打印错误堆栈信息
        });

    }

    public boolean Verify(String m, String finalsign) throws Exception {
        System.out.println("Verify start!");

        Properties PKprop = PropertiesUtil.loadProperties("PK.properties");
        String PKstr = PKprop.getProperty("PK");
        Map<String, Object> PK = (Map<String, Object>) Serialize.deserialize(Hex.decode(PKstr));

        List<Object> DeTAPSsign = (List<Object>)CustomSerializer.deserialize(Base64.getDecoder().decode(finalsign));
        List<Object> DTPKEcipherText = (List<Object>) DeTAPSsign.get(0);
        Element c1 = ((Element) DeTAPSsign.get(1)).getImmutable();
        Element c2 = ((Element) DeTAPSsign.get(2)).getImmutable();
        List<Element> inds = (List<Element>) DeTAPSsign.get(3);
        List<Object> pi = (List<Object>) DeTAPSsign.get(4);

        Map<String, Object> pkPlus = (Map<String, Object>) PK.get("pkPlus");
        List<Element> pk = (List<Element>) pkPlus.get("pk");
        byte[] eta = Base64.getDecoder().decode((String) DeTAPSsign.get(5));
        //验证ECDSA签名
        StringBuilder sb = new StringBuilder();
        sb.append(m).append(DTPKEcipherText).append(c1).append(c2).append(inds).append(pi);
        byte[] ECDbytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        ECPublicKey[] pks_set = (ECPublicKey[]) PK.get("pks_set");
        ECPublicKey pks = pks_set[0];
        boolean verifyresult = SIG.verifySignature(ECDbytes, eta, pks);

        if (verifyresult) {
            System.out.println("SIG scheme verify succeed !");
        } else {
            System.out.println("SIG scheme verify fail !");
        }
        verifyresult = true;
        List<Element> proof1_2_1 = (List<Element>) pi.get(0);
        List<Element> proof1_2_2 = (List<Element>) pi.get(1);
        List<Element> proof1_3 = (List<Element>) pi.get(2);
        List<Element> proof2_1 = (List<Element>) pi.get(3);
        List<Element> proof2_2_1 = (List<Element>) pi.get(4);
        List<Element> proof2_2_2 = (List<Element>) pi.get(5);
        List<Element> proof2_3 = (List<Element>) pi.get(6);
        List<Element> proof3 = (List<Element>) pi.get(7);
        List<Element> proof4 = (List<Element>) pi.get(8);
        List<List<Element>> proof5 = (List<List<Element>>) pi.get(9);
        if (ZeroKnowledgeProof.Verify1_2_1(proof1_2_1)) {
            System.out.println("ZeroKnowledgeProof 1_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_2_1(proof1_2_1)) {
            System.out.println("ZeroKnowledgeProof 1_2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_2_2(proof1_2_2)) {
            System.out.println("ZeroKnowledgeProof 1_2_2 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_2_2 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify1_3(g, h, proof1_3)) {
            System.out.println("ZeroKnowledgeProof 1_3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 1_3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_1(proof2_1, pk)) {
            System.out.println("ZeroKnowledgeProof 2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_2_1(proof2_2_1)) {
            System.out.println("ZeroKnowledgeProof 2_2_1 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_2_1 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_2_2(proof2_2_2)) {
            System.out.println("ZeroKnowledgeProof 2_2_2 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_2_2 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify2_3(g, h, proof2_3)) {
            System.out.println("ZeroKnowledgeProof 2_3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 2_3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify3(proof3)) {
            System.out.println("ZeroKnowledgeProof 3 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 3 verify fail!");
            verifyresult = false;
        }
        if (ZeroKnowledgeProof.Verify4(proof4)) {
            System.out.println("ZeroKnowledgeProof 4 verify succeed!");
        } else {
            System.out.println("ZeroKnowledgeProof 4 verify fail!");
            verifyresult = false;
        }
        for(List<Element>proof5_part:proof5){
            if (!ZeroKnowledgeProof.Verify5(proof5_part)) {
                System.out.println("ZeroKnowledgeProof 5 verify fail!");
                verifyresult = false;
            }
        }
        System.out.println("ZeroKnowledgeProof 5 verify succeed!");
        System.out.println("Verify end !");

        return verifyresult;
    }

    public void Trace2() throws Exception {
            System.out.println("Trace2 start !");

            int TracerIndex=1;

            //get ek from PK
            Properties PKprop = PropertiesUtil.loadProperties("PK.properties");
            String PKstr = PKprop.getProperty("PK");
            Map<String,Object>PK= (Map<String, Object>) Serialize.deserialize(Hex.decode(PKstr));
            //ek
            Map<String,Object>ek= (Map<String, Object>) PK.get("ek");

            //区块链
            String rpcUrl = "http://47.96.177.120:8545";
            String contractAddress="0x6f08ceb8c1356f71c7e150d4b0e889c3c098f82b";
            final int chainID=12345;

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("UTC--2024-05-27T13-37-46.855227516Z--241f39599c888107485bef04f141e17a31ad77ec");
            if (inputStream == null) {
                throw new RuntimeException("Resource file not found");
            }

            // 将输入流写入临时文件
            File tempFile = File.createTempFile("keystore", ".tmp");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            String password_="123";
            Web3j web3j= Web3j.build(new HttpService(rpcUrl));
            Credentials credentials = WalletUtils.loadCredentials(password_,tempFile.getAbsolutePath());

            RawTransactionManager rawTransactionManager = new RawTransactionManager(web3j, credentials, chainID);
            ContractGasProvider contractGasProvider = new DefaultGasProvider();
            DeTAPSContract contract = DeTAPSContract.load(contractAddress, web3j,rawTransactionManager, contractGasProvider);

            //得到skt
            Properties TracerProp = PropertiesUtil.loadProperties("Tracer" + TracerIndex + ".properties");
            String sktStr = TracerProp.getProperty("skt");
            List<Object>skt= (List<Object>) Serialize.deserialize(Hex.decode(sktStr));

            System.out.println("load success");

            int tPlus=2;

            String eventSignatureHash = EventEncoder.encode(DeTAPSContract.NOTARYSHARESUBMITTED_EVENT);
            BigInteger tracerIDValue = BigInteger.valueOf(TracerIndex);
            String tracerIDTopic = "0x" + String.format("%064x",tracerIDValue);

            EthBlockNumber result = web3j.ethBlockNumber().send();
            BigInteger currentBlockNumber = result.getBlockNumber();
            System.out.println("当前区块号: " + currentBlockNumber);

            // 计算从当前最新的区块向前的10个区块
            DefaultBlockParameter startBlock = new DefaultBlockParameterNumber(currentBlockNumber.subtract(BigInteger.TEN.multiply(new BigInteger("2"))));

            // 创建 EthFilter 实例
            EthFilter filter = new EthFilter(startBlock, DefaultBlockParameterName.LATEST, contract.getContractAddress())
                .addSingleTopic(eventSignatureHash)
                .addSingleTopic(tracerIDTopic);

//        // 创建 EthFilter 实例
//        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, contract.getContractAddress())
//                .addSingleTopic(eventSignatureHash)
//                .addSingleTopic(tracerIDTopic);


            web3j.ethLogFlowable(filter).subscribe(log -> {
                Event event=DeTAPSContract.NOTARYSHARESUBMITTED_EVENT;

                //event参数为(uint tracerId,uint DeTAPSsignId,Strng encshare)
                List<Type> values = FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
                BigInteger tracerId = (BigInteger) values.get(0).getValue();
                BigInteger DeTAPSsignId= (BigInteger) values.get(1).getValue();
                BigInteger shareCount=(BigInteger) values.get(2).getValue();
                System.out.println("tracerId = " + tracerId);
                System.out.println("DeTAPSsignId = " + DeTAPSsignId);
                System.out.println("shareCount = " + shareCount);
                List <String>encshares = contract.getNotaryEncShares(tracerId, DeTAPSsignId).send();
                if(shareCount.intValue()==tPlus && encshares.size()==tPlus){
                    System.out.println("encshares = " + encshares);
                    //得到每个公证人的解密份额
                    AsymmetricKeyParameter key = PrivateKeyFactory.createKey(Hex.decode((String) skt.get(0)));
                    ECPrivateKeyParameters ske= (ECPrivateKeyParameters) key;
                    String m="";
                    List<Object> DeTAPSsign = null;
                    String sql="select signature from detaps_sign where id = ?";
                    try (Connection connection = JDBCUtilsByDruid.getConnection();
                         PreparedStatement statement = connection.prepareStatement(sql)){
                            statement.setInt(1,DeTAPSsignId.intValue());
                        try (ResultSet resultSet = statement.executeQuery()) {
                            if (resultSet.next()) { // id为主键，只有一个结果
                                String DeTAPSsignStr = resultSet.getString("signature");
                                if(DeTAPSsignStr!=null){
                                    DeTAPSsign= (List<Object>) CustomSerializer.deserialize(Base64.getDecoder().decode(DeTAPSsignStr));
                                }
                            } else {
                                System.out.println("没有定位到签名");
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    List<Element> T = new ArrayList<>();
                    List<Entry<Element,List<Element>>>NotriesShares=new ArrayList<>();

                    for(String encshare:encshares) {
                        List<Object> orgShare = (List<Object>) CustomSerializer.deserialize(PKE.Dec(ske, encshare));
                        Element uvk = ((Element) orgShare.get(2)).getImmutable();
                        List<Element> threeTriple= (List<Element>) orgShare.get(0);
                        if("".equals(m)){
                            m= (String) orgShare.get(1);
                        }
                        T.add(uvk);
                        NotriesShares.add(new AbstractMap.SimpleEntry<>(uvk,threeTriple));
                    }
                    //提前加载fullHdr，分离出DTPKE加密过的ATS签名：encryptedATSsignature
                    List<Object> dtpkeEncrypt = (List<Object>) DeTAPSsign.get(0);
                    int length=dtpkeEncrypt.size();
                    //加载分离出DTPKE加密过的ATS签名：dtpkeEncrypt
                    String encryptedATSsignature=(String) dtpkeEncrypt.get(length-1);

                    Element[]S=new Element[length-4];
                    for(int i=0;i<length-4;i++){
                        S[i]=(Element) dtpkeEncrypt.get(i);
                    }

                    Element[]Hdr= new Element[2];
                    Hdr[0]=(Element) dtpkeEncrypt.get(length-3);
                    Hdr[1]=(Element) dtpkeEncrypt.get(length-2);

                    //完整头部
                    Object[]fullHdr=new Object[3];
                    fullHdr[0]=S;
                    fullHdr[1]=tPlus;
                    fullHdr[2]=Hdr;

                    if (dtpke.ValidateCT(ek,S,tPlus,Hdr)){
                        System.out.println("ValidateCT succeed !");
                        int count=0;
                        List<Element>[]shares=new ArrayList[tPlus];
                        for(int i=0;i<shares.length;i++){
                            shares[i]=new ArrayList<>();
                            shares[count++]=NotriesShares.get(i).getValue();
                        }

                        for(int i=0;i<NotriesShares.size();i++){
                            boolean  ShareVerifyresult= dtpke.ShareVerify(ek, fullHdr,NotriesShares.get(i).getValue(),NotriesShares.get(i).getKey());
                            if(ShareVerifyresult) {
                                System.out.println("ShareVerify succeed!");
                            }else {
                                System.out.println("ShareVerify fail!");
                            }
                        }
                        Map<String,Object>ck= (Map<String, Object>) skt.get(1);
                        List<Object> ATSsign = dtpke.ShareCombine(ck, fullHdr, T.toArray(new Element[0]), shares, encryptedATSsignature);
                        List<Element>pk= (List<Element>) skt.get(2);
                        int t= (int) skt.get(3);

                        Element R= ((Element) ATSsign.get(0)).getImmutable();
                        Element z= ((Element) ATSsign.get(1)).getImmutable();
                        StringBuilder cSB = new StringBuilder();
                        cSB.append(pk);
                        cSB.append(R);
                        cSB.append(m);
                        //拼接得到c的byte数组
                        byte[] cBytes = cSB.toString().getBytes(StandardCharsets.UTF_8);
                        byte[] chash = Hash.calculateSM3Hash(cBytes);
                        Element c = Zr.newElementFromHash(chash, 0, chash.length).getImmutable();
                        System.out.println("pk.size() = " + pk.size());
                        List<List<Integer>> subsets = SubsetUtil.getSubsets(pk.size(), t);
                        for(List<Integer> subset : subsets) {
                            Element temp=G.newOneElement();
                            for(int index:subset){
                                temp.mul(pk.get(index-1));
                            }
                            if(g.powZn(z).equals(R.mul(temp.duplicate().powZn(c)))){
                                System.out.println("subset = " + subset);
                                String placeholders = String.join(",", Collections.nCopies(subset.size(), "?"));
                                String traceSQL = "SELECT * FROM  signer WHERE id IN (" + placeholders + ")";
                                String insertSQL = "INSERT INTO crimerecords (CompanyName,CrimeType,CrimeDescription) VALUES (?, ?, ?)";
                                String crimeType="金融犯罪";
                                String crimeDescription="2024年盗用资金";
                                List<NotarialRecord>recordList=new ArrayList<>();
                                try(Connection connection = JDBCUtilsByDruid.getConnection();
                                    PreparedStatement statement = connection.prepareStatement(traceSQL)){

                                    int index=1;
                                    for(int id:subset){
                                        statement.setInt(index++,id);
                                    }
                                    List<String>companys=new ArrayList<>();
                                    try(ResultSet resultSet = statement.executeQuery()){
                                        while(resultSet.next()){
                                            String companyName = resultSet.getString("username");
                                            companys.add(companyName);
                                        }
                                    }
                                    try (PreparedStatement statement1 = connection.prepareStatement(insertSQL)){
                                        for(String name:companys) {
                                            statement1.setString(1,name);
                                            statement1.setString(2,"金融犯罪");
                                            statement1.setString(3,"2024年盗用资金");
                                            statement1.addBatch();
                                        }
                                        statement1.executeBatch();
                                        System.out.println("success");
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    }else {
                        System.out.println("ValidateCT fail !");
                    }

                }
            },error-> {
                error.printStackTrace();
            });


    }
}
