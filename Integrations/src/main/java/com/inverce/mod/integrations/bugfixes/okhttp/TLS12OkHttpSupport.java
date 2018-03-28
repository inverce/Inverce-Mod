package com.inverce.mod.integrations.bugfixes.okhttp;

import android.os.Build;
import android.support.annotation.NonNull;

import com.google.android.gms.security.ProviderInstaller;
import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class TLS12OkHttpSupport {
    @NonNull
    public static OkHttpClient.Builder enableTls12OnPreLollipop(@NonNull OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                ProviderInstaller.installIfNeeded(IM.context());
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                String[] protocols = sc.getSupportedSSLParameters().getProtocols();
                for (String protocol : protocols) {
                    Log.e("Context supported" , protocol);
                }
                if (getTrustManager() != null) {
                    client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), getTrustManager());
                }

            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }


    private static X509TrustManager getTrustManager() {
        TrustManagerFactory trustManagerFactory = null;
        try {
            trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                Log.e("tls",   "Unexpected default trust managers:" + Arrays.toString(trustManagers));
                return null;
            }
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException e) {
            Log.e("tls",e.toString());
            e.printStackTrace();
            return null;
        } catch (KeyStoreException e) {
            Log.e("tls",e.toString());
            e.printStackTrace();
            return null;
        }
    }


    public static class Tls12SocketFactory extends SSLSocketFactory {
        private static final String[] TLS_V12_ONLY = {"TLSv1.2"};

        final SSLSocketFactory delegate;

        public Tls12SocketFactory(SSLSocketFactory base) {
            this.delegate = base;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket s) {
            if (s instanceof SSLSocket) {
                ((SSLSocket) s).setEnabledProtocols(TLS_V12_ONLY);
            }
            return s;
        }
    }

}
