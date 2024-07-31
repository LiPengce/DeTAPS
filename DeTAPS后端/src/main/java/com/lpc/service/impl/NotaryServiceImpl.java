package com.lpc.service.impl;

import com.lpc.cryptography.base.PKE;
import com.lpc.cryptography.common.PairingProvider;
import com.lpc.cryptography.detaps.DeTAPS;
import com.lpc.cryptography.dtpke.DTPKE;
import com.lpc.cryptography.kase.KASE2;
import com.lpc.cryptography.util.Serialize;
import com.lpc.mapper.NotaryMapper;
import com.lpc.pojo.NotarialRecord;
import com.lpc.pojo.Result;
import com.lpc.response.DetapsSign;
import com.lpc.response.NotaryResponse;
import com.lpc.service.NotaryService;
import com.lpc.util.CustomSerializer;
import com.lpc.util.ThreadLocalUtil;
import com.lpc.util.encoders.HexDERCodec;
import it.unisa.dia.gas.jpbc.Element;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;

@Service
public class NotaryServiceImpl implements NotaryService, PairingProvider {
    @Autowired
    private NotaryMapper notaryMapper;
    @Autowired
    private ResourceLoader resourceLoader;
    private KASE2 kase = new KASE2();
    private DTPKE dtpke = new DTPKE();
    private DeTAPS deTAPS = new DeTAPS();

    @Override
    public Result trace(MultipartFile kaggStr, MultipartFile uskStr) {
        try {
            String kaggHexStr="",uskHexStr="";
            if(!kaggStr.isEmpty()){
                kaggHexStr = HexDERCodec.decodeFromDER(kaggStr);
            }
            if(!uskStr.isEmpty()){
                uskHexStr = HexDERCodec.decodeFromDER(uskStr);
            }
            System.out.println("kaggStr = " + kaggHexStr);
            System.out.println("uskStr = " + uskHexStr);
            Element kagg = G.newElementFromBytes(Hex.decode(kaggHexStr)).getImmutable();
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer id = (Integer) claims.get("id");
            String name= (String) claims.get("username");
            System.out.println("name = " + name);
            System.out.println("id = " + id);
            String uvkStr = notaryMapper.finduvkById(id);
            Element uvk = Zr.newElementFromBytes(Hex.decode(uvkStr)).getImmutable();
            System.out.println("uvk = " + uvk);
            Element usk = G.newElementFromBytes(Hex.decode(uskHexStr)).getImmutable();
            System.out.println("Trace1 start!");

            Resource resource = resourceLoader.getResource("classpath:PK.properties");
            Element[] PubK = null;
            try (InputStream is = resource.getInputStream()) {
                Properties PKprop = new Properties();
                PKprop.load(is);
                String PKstr = PKprop.getProperty("PK");
                Map<String, Object> PK = (Map<String, Object>) Serialize.deserialize(Hex.decode(PKstr));
                PubK = (Element[]) PK.get("PubK");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Element Tr = kase.Trapdoor(kagg, uvk.toBytes()).getImmutable();
            String m; //预定义m
            byte[] shareBytes = null;
            boolean testresult = false;

            List<DetapsSign> allDetapsSigns = notaryMapper.getAllDetapsSigns();

            String signature=null;
            int id2=0;
            for (DetapsSign detapsSign : allDetapsSigns) {
                id2 = detapsSign.getId();
                signature = detapsSign.getSignature();
                List<Object> DeTAPSsign = (List<Object>) CustomSerializer.deserialize(Base64.getDecoder().decode(signature));
                Element c1 = ((Element) DeTAPSsign.get(1)).getImmutable();
                Element c2 = ((Element) DeTAPSsign.get(2)).getImmutable();
                List<Element> inds = (List<Element>) DeTAPSsign.get(3);
                Element Tri = kase.Adjust(id2, SIGNER_GROUPS, Tr, PubK).getImmutable();
                testresult = kase.Test(Tri, SIGNER_GROUPS, inds, c1, c2, PubK);
                if (testresult) {
                    m = detapsSign.getM();
                    List<Object> dtpkeEncrypt = (List<Object>) DeTAPSsign.get(0);
                    int length = dtpkeEncrypt.size();
                    Element[] S = new Element[length - 4];
                    for (int i = 0; i < length - 4; i++) {
                        S[i] = (Element) dtpkeEncrypt.get(i);
                    }
                    if (!deTAPS.Verify(m, signature)) {
                        System.out.println("Signature Verify fail!");
                    } else {
                        System.out.println("Signature Verify succeed!");
                    }
                    Element[] Hdr = new Element[2];
                    Hdr[0] = (Element) dtpkeEncrypt.get(length - 3);
                    Hdr[1] = (Element) dtpkeEncrypt.get(length - 2);

                    List<Element> threeTriple = dtpke.ShareDecrypt(usk, Hdr);

                    //最终上传的
                    List<Object>orgShare=new ArrayList<>();
                    orgShare.add(threeTriple); //0 加密的份额
                    orgShare.add(m); //1 消息m
                    orgShare.add(uvk); //2 验证密钥

                    shareBytes = CustomSerializer.serialize(orgShare);

                    break;
                }
            }
            if (shareBytes != null) {
                if(ManagerNotaryGroups.getTracerId(signature)==null) {
                    ManagerNotaryGroups.record(signature,new Random().nextInt(5)+1);
                }
//                int TracerIndex=ManagerNotaryGroups.getTracerId(signature);
                int TracerIndex=1;
                String pkeStr = notaryMapper.getPublicKeyEncryptionById(TracerIndex);
                AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.createKey(Hex.decode(pkeStr));
                if (asymmetricKeyParameter instanceof ECPublicKeyParameters) {
                    ECPublicKeyParameters pke = (ECPublicKeyParameters) asymmetricKeyParameter;
                    String encshare = PKE.Enc(pke, shareBytes); // 使用pke加密shareBytes
                    if(id2!=0) {
                        System.out.println("DeTAPSsignId = " + id2);
                        return Result.success(new NotaryResponse(TracerIndex, id2, encshare));
                    }
                } else {
                    throw new IllegalArgumentException("Recovered key is not an instance of ECPublicKeyParameters");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("操作失败！");
        }

        return Result.error("操作失败！");
    }

    @Override
    public List<NotarialRecord> getRecords() {

        List<NotarialRecord> records = this.notaryMapper.getRecords();
        return records;
    }
}

