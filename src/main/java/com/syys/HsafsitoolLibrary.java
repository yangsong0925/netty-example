package com.syys;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.util.HashMap;

public interface HsafsitoolLibrary extends Library {

    HsafsitoolLibrary INSTANCE = Native.load("testSpace/hsafsitool", HsafsitoolLibrary.class, new HashMap() {{
        this.put(Library.OPTION_FUNCTION_MAPPER, new NameFunctionMapper());
    }});


    int gm_sign_key(String userid, String prv_key, String pub_key, byte[] msg, int msg_len, byte[] singrs);

    int gm_verify_key(String userid, String pub_key, byte[] msg, int msg_len, String singrs);


}
