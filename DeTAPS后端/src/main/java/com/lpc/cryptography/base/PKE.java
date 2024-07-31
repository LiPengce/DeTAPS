package com.lpc.cryptography.base;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.SecureRandom;
import java.security.Security;

public class PKE {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 加密函数
    public static String Enc(ECPublicKeyParameters publicKey, byte[] data) throws Exception {
        SM2Engine sm2Engine = new SM2Engine();
        ParametersWithRandom paramsWithRandom = new ParametersWithRandom(publicKey, new SecureRandom());
        sm2Engine.init(true, paramsWithRandom);
        String ciphertext=new String(Base64.encode(sm2Engine.processBlock(data, 0, data.length)));

        return ciphertext;
    }

    // 解密函数
    public static byte[] Dec(ECPrivateKeyParameters privateKey, String ciphertext) throws Exception {
        byte[] encryptedData = Base64.decode(ciphertext);
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKey);
        byte[] decryptedData = sm2Engine.processBlock(encryptedData, 0, encryptedData.length);

        return decryptedData;
    }

    // 生成SM2密钥对函数
    public static AsymmetricCipherKeyPair generateKeyPair() {
        X9ECParameters ecParameters = ECUtil.getNamedCurveByName("sm2p256v1");
        ECDomainParameters ecParams = new ECDomainParameters(
                ecParameters.getCurve(),
                ecParameters.getG(),
                ecParameters.getN(),
                ecParameters.getH()
        );
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(ecParams, new SecureRandom());
        generator.init(keyGenParams);

        return generator.generateKeyPair();
    }

}
