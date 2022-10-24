package com.syys;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.HashMap;

public interface HsafsiyhsafeLibrary extends Library {

    /**
     * 接口直接对动态库文件进行调用，
     */
    HsafsiyhsafeLibrary INSTANCE = Native.load("testSpace/hsafsiyhsafe", HsafsiyhsafeLibrary.class, new HashMap() {{
        this.put(Library.OPTION_FUNCTION_MAPPER, new NameFunctionMapper());
    }});

    /**
     * @param pub_key   公钥密钥 128个字符串
     * @param plain     明文
     * @param plain_len 明文长度
     * @param cipher    加密后密文,base64编码
     * @return
     */
    int gm_ecb_encrypt_key(String pub_key, byte[] plain, int plain_len, byte[] cipher);

    /**
     * @param pub_key    公钥密钥 128个字符串(十六进制)
     * @param cipher     密文,base64编码
     * @param cipher_len 密文长度,字节数
     * @param plain      解密后明文
     * @return
     */
    int gm_ecb_decrypt_key(String pub_key, byte[] cipher, int cipher_len, byte[] plain);
}
