package com.syys;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 医保对码
 *
 * @name: MappingController
 * @author: tq
 * @date: 2022-02-24 10:32
 **/
@Log4j2
@RestController
@RequestMapping("yb")
public class YbController {

    private static final String priKey = "9F31B6AC613AA8CB55DC87B34DF4DF3A639E899750C020C3F6D1CA6341006278";
    private static final String pubKey = "399CC2AC171F5DB6FFB371910046A207A8032379A492CFE70DCFA39BB3605D83198BE4C634075DACDC7FFC205D73E1931F9C0F1851F55ABC6F6DE820E80282DF";

    @PostMapping("gm_sign_key")
    public static String gm_sign_key(@RequestBody Map<String, String> body) {
        String signSource = MapUtil.getStr(body, "input");
        System.out.println("gm_sign_key:" + signSource);
        byte[] bytes = signSource.getBytes(StandardCharsets.UTF_8);
        byte[] returnStr = new byte[bytes.length + (1024 * 10)];
        int count = HsafsitoolLibrary.INSTANCE.gm_sign_key(null, priKey, pubKey, bytes, bytes.length, returnStr);
        if (count < 0) {
            System.out.println("gm_sign_key error:" + count);
            return StrUtil.EMPTY;
        }
        String cainfo = new String(returnStr, StandardCharsets.UTF_8);
        System.out.println("gm_sign_key return:" + cainfo);
        return cainfo.trim();
    }

    @PostMapping("gm_ecb_encrypt_key")
    public String gm_ecb_encrypt_key(@RequestBody Map<String, String> body) {
        String input = MapUtil.getStr(body, "input");
        System.out.println("gm_ecb_encrypt_key:" + input);
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] returnStr = new byte[bytes.length + (1024 * 10)];
        int count = HsafsiyhsafeLibrary.INSTANCE.gm_ecb_encrypt_key(pubKey, bytes, bytes.length, returnStr);
        if (count < 0) {
            System.out.println("gm_ecb_encrypt_key error:" + count);
            return StrUtil.EMPTY;
        }
        String inputRe = new String(returnStr, StandardCharsets.UTF_8);
        System.out.println("gm_ecb_encrypt_key return:" + inputRe);
        return inputRe.trim();
    }

    @PostMapping("gm_ecb_decrypt_key")
    public String gm_ecb_decrypt_key(@RequestBody Map<String, String> body) {
        String input = MapUtil.getStr(body, "input");
        System.out.println("gm_ecb_decrypt_key:" + input);
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] returnStr = new byte[bytes.length + (1024 * 10)];
        int count = HsafsiyhsafeLibrary.INSTANCE.gm_ecb_decrypt_key(pubKey, bytes, bytes.length, returnStr);
        if (count < 0) {
            System.out.println("gm_ecb_decrypt_key error:" + count);
            return StrUtil.EMPTY;
        }
        String inputRe = new String(returnStr, StandardCharsets.UTF_8);
        System.out.println("gm_ecb_decrypt_key return:" + inputRe);
        return inputRe.trim();
    }

    public static void main(String[] args) {
        String inputSource = "oYKWnsbT72hZKaIVDL/snrW2QtuU0m1/GDzwJ+1ecEiKInuHP6d9kA4Bv1abToxV93PYxmujg6Ez4SGa35kUSq+EjYqbCCWAteSSfXqgcdgC8HJ8cm1V2GTLjznA09UaelDJ0ksgp8AsFJQuijNhim5gL3BE4454LQrBLpLFMBmL5VUe/t2iiPKiqmsJBZM19lK6qGBCTgVZmRrdC1fJkwbjzNnHSoF8UdY9DPIk2pXzsKfGuGuAyS+zPrwerrOABLouwP3m4mMuv2TjTxI4QQ==";
        Map<String, Object> map = new HashMap<>();
        map.put("input", inputSource);
        System.out.println(HttpUtil.post("http://43.142.171.192/yb/gm_ecb_encrypt_key", JSONUtil.toJsonStr(map)));
        ;
    }

}
