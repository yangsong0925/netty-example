package com.syys;

import java.nio.charset.StandardCharsets;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/9/28 18:57
 */
public class CehngDuYBTest {

    public static void main(String[] args) {
        String privatekey = "4ab619fca6be7f767796de13028db4e478a17938c8aacbf5908661e2a5765d1b";
        String puKey = "bd4c6d68e041808516eb6c93e92164630e006dc64bac093241bcc2ef45baccce15c1c3da8418ed7d1530a7fa545c3b064b366d4499863448261231fe17a40290";

        String data = "1101|P51140200207202111151243060001|510100|510100|2022-08-01 00:35:39|H51202100027|512000G0000000367134|{\"data\":{\"mdtrt_cert_type\":\"02\",\"mdtrt_cert_no\":\"52020020210315000001\",\"card_sn\":\"\",\"begntime\":\"2021-03-01 00:35:39\",\"psn_cert_type\":\"01\",\"certno\":\"450122199901015587\",\"psn_name\":\"林五\"}}";
        data = data.replaceAll("\\\\", "");
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] returnStr = new byte[bytes.length + (1024 * 10)];
        int count = HsafsitoolLibrary.INSTANCE.gm_sign_key(null, privatekey, puKey, bytes, bytes.length, returnStr);

        System.out.println(count);
        if (count > 0) {
            String utf_8 = new String(returnStr, StandardCharsets.UTF_8);
            System.out.println(utf_8.trim());
        }


        count = HsafsiyhsafeLibrary.INSTANCE.gm_ecb_encrypt_key(puKey, bytes, bytes.length, returnStr);
        if (count > 0) {
            String utf_8 = new String(returnStr, StandardCharsets.UTF_8);
            System.out.println(utf_8.trim());
        }
    }
}
