package com.mini10.miniserver.common.entity;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * AES加解密
 *
 * @author dongxiyan
 * @date 2019-07-19 16:12
 */
public class AES {

    public static boolean initialized = false;

    /**
     * AES解密
     *
     * @param content 密文
     * @return byte[]
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
        initialize();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIv(ivByte));
        return cipher.doFinal(content);
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }


    public static AlgorithmParameters generateIv(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }
}

