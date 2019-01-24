/*
 * (C) Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     ohun@live.cn (夜色)
 */

package com.aismono.mpush.util.crypto;





import com.aismono.mpush.api.Constants;
import com.aismono.mpush.client.ClientConfig;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA公钥/私钥/签名工具包
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 */
public final class RSAUtils {

    /**
     * 密钥位数
     */
    private static final int RAS_KEY_SIZE = 1024;

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * 填充方式
     */
    public static final String KEY_ALGORITHM_PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 128 - 11;


    /**
     * 编码密钥，便于存储
     *
     * @param key key
     * @return xxx String String
     * @throws Exception Exception
     */
    public static String encodeBase64(Key key) throws Exception {
        return Base64Utils.encode(key.getEncoded());
    }

    /**
     * 从字符串解码私钥
     *
     * @param key key
     * @return xxx
     * @throws Exception Exception
     */
    public static PrivateKey decodePrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64Utils.decode(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 从字符串解码公钥
     *
     * @param publicKey publicKey
     * @return xxx
     * @throws Exception Exception
     */
    public static PublicKey decodePublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(x509KeySpec);
    }


    /**
     * 用私钥对信息生成数字签名
     *
     * @param data data       已加密数据
     * @param privateKey privateKey 私钥(BASE64编码)
     * @return xxx
     * @throws Exception Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(decodePrivateKey(privateKey));
        signature.update(data);
        return Base64Utils.encode(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data data      已加密数据
     * @param publicKey publicKey 公钥(BASE64编码)
     * @param sign sign      数字签名
     * @return xxx
     * @throws Exception Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(decodePublicKey(publicKey));
        signature.update(data);
        return signature.verify(Base64Utils.decode(sign));
    }


    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，
     * 不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus modulus  模
     * @param exponent exponent 指数
     * @return xxx
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            //LOGGER.error("getPublicKey ex modulus={}, exponent={}", modulus, exponent, e);
        }
        return null;
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，
     * 不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus modulus  模
     * @param exponent exponent 指数
     * @return xxx
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            //LOGGER.error("getPrivateKey ex modulus={}, exponent={}", modulus, exponent, e);
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data data
     * @param publicKey publicKey
     * @return xxx
     */
    public static byte[] encryptByPublicKey(byte[] data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 模长
            int key_len = publicKey.getModulus().bitLength() / 8;
            // 加密数据长度 <= 模长-11
            //如果明文长度大于模长-11则要分组加密
            return doFinal(cipher, data, key_len - 11);
        } catch (Exception e) {
            ClientConfig.I.getLogger().e(e, "encryptByPublicKey ex");
        }
        return Constants.EMPTY_BYTES;
    }

    /**
     * 私钥解密
     *
     * @param data data
     * @param privateKey privateKey
     * @return xxx
     */
    public static byte[] decryptByPrivateKey(byte[] data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            //模长
            int key_len = privateKey.getModulus().bitLength() / 8;
            //如果密文长度大于模长则要分组解密
            return doFinal(cipher, data, key_len);
        } catch (Exception e) {
            ClientConfig.I.getLogger().e(e, "decryptByPrivateKey ex");
        }
        return Constants.EMPTY_BYTES;
    }

    /**
     * 注意：RSA加密明文最大长度117字节，
     * 解密要求密文最大长度为128字节，
     * 所以在加密和解密的过程中需要分块进行。
     *
     * @param cipher cipher
     * @param data data
     * @return xxx
     * @throws BadPaddingException BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     */
    private static byte[] doFinal(Cipher cipher, byte[] data, int key_len) throws BadPaddingException, IllegalBlockSizeException {
        int inputLen = data.length, offSet = 0;
        byte[] tmp;
        ByteArrayOutputStream out = new ByteArrayOutputStream(getTmpArrayLength(inputLen));
        while (inputLen > 0) {
            tmp = cipher.doFinal(data, offSet, Math.min(key_len, inputLen));
            out.write(tmp, 0, tmp.length);
            offSet += key_len;
            inputLen -= key_len;
        }
        return out.toByteArray();
    }

    private static int getTmpArrayLength(int L) {
        int S = MAX_DECRYPT_BLOCK;
        while (S < L) S <<= 1;
        return S;
    }

    /**
     * 私钥解密
     *
     * @param data data       已加密数据
     * @param privateKey privateKey 私钥(BASE64编码)
     * @return xxx
     * @throws Exception Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        PrivateKey key = decodePrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return doFinal(cipher, data, MAX_DECRYPT_BLOCK);
    }

    /**
     * 公钥解密
     *
     * @param data data      已加密数据
     * @param publicKey publicKey 公钥(BASE64编码)
     * @return xxx
     * @throws Exception Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String publicKey) throws Exception {
        PublicKey key = decodePublicKey(publicKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return doFinal(cipher, data, MAX_DECRYPT_BLOCK);
    }

    /**
     * 公钥加密
     *
     * @param data data      源数据
     * @param publicKey publicKey 公钥(BASE64编码)
     * @return xxx
     * @throws Exception Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        PublicKey key = decodePublicKey(publicKey);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return doFinal(cipher, data, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 私钥加密
     *
     * @param data data       源数据
     * @param privateKey privateKey 私钥(BASE64编码)
     * @return xxx
     * @throws Exception Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        PrivateKey key = decodePrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return doFinal(cipher, data, MAX_ENCRYPT_BLOCK);
    }
}