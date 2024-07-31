package com.lpc.cryptography.base;

import com.lpc.cryptography.common.PairingProvider;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;

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
