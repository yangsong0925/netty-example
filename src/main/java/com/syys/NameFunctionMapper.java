package com.syys;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/9/29 12:08
 */
public class NameFunctionMapper implements FunctionMapper {

    private static final Map<String, String> methodName = new HashMap() {{
        this.put("gm_sc_encrypt", "gm_encrypt@12");
        this.put("gm_ecb_decrypt_key", "gm_ecb_decrypt_key@16");
        this.put("gm_ecb_encrypt_key", "gm_ecb_encrypt_key@16");
        this.put("gm_keygen", "gm_keygen@8");
        this.put("gm_sign_key", "gm_sign_key@24");
        this.put("gm_verify_key", "gm_verify_key@20l");
    }};


    @Override
    public String getFunctionName(NativeLibrary library, Method method) {
        return methodName.get(method.getName());
    }
}
