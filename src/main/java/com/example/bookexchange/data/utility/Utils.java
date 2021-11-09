package com.example.bookexchange.data.utility;

import com.example.bookexchange.data.model.system.User;
import com.example.bookexchange.data.service.system.UserService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class Utils {

    public static User currentUser(UserService userService) {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
