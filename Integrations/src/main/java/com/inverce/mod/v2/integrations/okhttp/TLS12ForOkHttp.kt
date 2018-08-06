package com.inverce.mod.v2.integrations.okhttp

import android.os.Build
import com.google.android.gms.security.ProviderInstaller
import com.inverce.mod.v2.core.Log
import com.inverce.mod.v2.core.context
import okhttp3.OkHttpClient
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.*

fun OkHttpClient.Builder.enableTlsSupportForPreLollipop() = apply {
    if (Build.VERSION.SDK_INT in 16..21) {
        try {
            ProviderInstaller.installIfNeeded(context)
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            val protocols = sc.supportedSSLParameters.protocols
            for (protocol in protocols) {
                Log.e("Context supported", protocol)
            }

            val support = TLS12OkHttpSupport()

            support.trustManager?.let {
                this.sslSocketFactory(Tls12SocketFactory(sc.socketFactory), it)
            }
        } catch (exc: Exception) {
            Log.ex(exc, "Error while setting TLS 1.2", tag = "OkHttpTLSCompat")
        }
    }
}

open class TLS12OkHttpSupport {
    val trustManager by lazy {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)

            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                Log.e(tag = "tls", message = "Unexpected default trust managers:" + Arrays.toString(trustManagers))
                return@lazy null
            }
            return@lazy trustManagers[0] as X509TrustManager
        } catch (e: NoSuchAlgorithmException) {
            Log.e(tag = "tls", message = e.toString())
            e.printStackTrace()
            return@lazy null
        } catch (e: KeyStoreException) {
            Log.e(tag = "tls", message = e.toString())
            e.printStackTrace()
            return@lazy null
        }
    }
}

open class Tls12SocketFactory(val delegate: SSLSocketFactory) : SSLSocketFactory() {
    override fun getDefaultCipherSuites(): Array<String> = delegate.defaultCipherSuites
    override fun getSupportedCipherSuites(): Array<String> = delegate.supportedCipherSuites

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket? {
        return patch(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int): Socket? {
        return patch(delegate.createSocket(host, port))
    }

    @Throws(IOException::class, UnknownHostException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket? {
        return patch(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket? {
        return patch(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket? {
        return patch(delegate.createSocket(address, port, localAddress, localPort))
    }

    private fun patch(s: Socket): Socket {
        if (s is SSLSocket) {
            s.enabledProtocols = TLS_V12_ONLY
        }
        return s
    }

    companion object {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")
    }
}