package com.lpc.service.impl;

import com.lpc.cryptography.base.Hash;
import com.lpc.cryptography.base.PKE;
import com.lpc.cryptography.common.PairingProvider;
import com.lpc.mapper.SignerMapper;
import com.lpc.pojo.Result;
import com.lpc.response.SignResponse;
import com.lpc.service.SignerService;
import com.lpc.util.CustomSerializer;
import com.lpc.util.ThreadLocalUtil;
import com.lpc.util.encoders.HexDERCodec;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SignerServiceImpl implements SignerService, PairingProvider {
    @Autowired
    private SignerMapper signerMapper;

    @Transactional
    @Override
    public Result Sign(@RequestParam("skstr") MultipartFile skstr, @RequestParam("m") MultipartFile m, @RequestParam("pids") MultipartFile pids,@RequestParam("gid") MultipartFile gid) {
        //与Enclave通信
        byte[]M=null;
        String skHexString=null;
        String PIDS=null;
        String GID=null;
        try {
            //对私钥进行解码
            if(!skstr.isEmpty()){
                skHexString = HexDERCodec.decodeFromDER(skstr);
                System.out.println("skHexString = " + skHexString);
            }
            if(!m.isEmpty()){
                M = m.getBytes();
            }
            System.out.println("Base64.toBase64String(m) = " + Base64.toBase64String(M));
            if(!pids.isEmpty()){
                PIDS = new String(pids.getBytes());
            }
            if(!gid.isEmpty()){
                GID = new String(gid.getBytes());
            }

            Element r = Zr.newRandomElement().getImmutable();
            Element Ri = g.powZn(r).getImmutable();

            //开始与Combiner进行通信
            String address="47.96.177.120";
            Element R=G.newOneElement();
            try (Socket socket = new Socket(address, 9999);
                 OutputStream outputStream = socket.getOutputStream();
                 InputStream inputStream = socket.getInputStream()) {

                System.out.println("GID = " + GID);

                byte[] gidBytes = Hex.decode(GID);
                byte[] RiBytes = Ri.toBytes();
                byte[] message = new byte[gidBytes.length + RiBytes.length];
                //先写gid，再写gBytes
                System.arraycopy(gidBytes, 0, message, 0, gidBytes.length);
                System.arraycopy(RiBytes, 0, message, gidBytes.length, RiBytes.length);

                outputStream.write(message);

                System.out.println("write one Ri successfully");

                byte[] receivedBytes = new byte[128];
                //等待读取
                int readlen = inputStream.read(receivedBytes);

                if (readlen == 128) {
                    R = G.newElementFromBytes(receivedBytes).getImmutable();
                    System.out.println("Received R successfully : " + R);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            int CombinerIndex = 1;
//            if(ManangerSignerGroups.getCombinerId(gid)==null) {
//                //随机选取一个Combiner
//                ManangerSignerGroups.record(gid,new Random().nextInt(5)+1);
//            }
//            CombinerIndex = ManangerSignerGroups.getCombinerId(gid);
            List<Element> pk = new ArrayList<>();
            List<Object> plainText = new ArrayList<>();

            List<String> pkStr = signerMapper.getAllPublicKeys();
            for (String str : pkStr) {
                pk.add(G.newElementFromBytes(Hex.decode(str)).getImmutable());
            }
            StringBuffer csb = new StringBuffer();
            csb.append(pk);
            csb.append(R);
            csb.append(Base64.toBase64String(M));
            byte[] cBytes = csb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] chash = Hash.calculateSM3Hash(cBytes);
            Element c = Zr.newElementFromHash(chash, 0, chash.length).getImmutable();
            Element sk = Zr.newElementFromBytes(Hex.decode(skHexString)).getImmutable();
            Element zi = r.add(sk.mul(c)).getImmutable();

            List<String> upks = signerMapper.findUpksByPids(Arrays.asList(PIDS.split(",")));

            List<Element> Notries = upks.stream().map(upk -> Zr.newElementFromBytes(Hex.decode(upk)).getImmutable()).toList();

            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer id = (Integer) claims.get("id");
            System.out.println("id = " + id);
            String mypk = signerMapper.findpkById(id);

            List<Element> share = new ArrayList<>();
            share.add(Ri);
            share.add(zi);

            plainText.add(mypk); //0
            plainText.add(Base64.toBase64String(M)); // 1
            plainText.add(share); // 2
            plainText.add(Notries); //3
            plainText.add(GID); //4

            String pkeStr = signerMapper.getPublicKeyEncryptionById(CombinerIndex);

            AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(Hex.decode(pkeStr));
            if (asymmetricKeyParameter instanceof ECPublicKeyParameters) {
                ECPublicKeyParameters pke = (ECPublicKeyParameters) asymmetricKeyParameter;
                String encshare = PKE.Enc(pke, CustomSerializer.serialize(plainText));
                System.out.println("CombinerIndex = " + CombinerIndex);

                return Result.success(new SignResponse(CombinerIndex, GID, encshare));
            } else {
                throw new IllegalArgumentException("Recovered key is not an instance of ECPublicKeyParameters");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("operate fail");
        }
    }
}

