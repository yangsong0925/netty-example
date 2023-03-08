package com.syys.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/12/21 14:06
 */
public final class ServerUtil {

    private static final boolean SSL = System.getProperty("ssl") != null;

    private ServerUtil() {
    }

    public static SslContext buildSslContext() throws CertificateException, SSLException {
        if (!SSL) {
            return null;
        }
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        return SslContextBuilder
            .forServer(ssc.certificate(), ssc.privateKey())
            .build();
    }
}