package com.lpc.crypto;

import java.nio.charset.StandardCharsets;

import com.lpc.common.PairingProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.crypto.digests.SM3Digest;

public class Hash implements PairingProvider {

    public static String calculateSM3Hash(String message) {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        return Hex.toHexString(hash);
    }

    public static byte[] calculateSM3Hash(byte[]bytes) {
        SM3Digest digest = new SM3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);

        return hash;
    }

}
