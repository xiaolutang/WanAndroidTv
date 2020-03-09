package com.txl.commonlibrary.utils.okhttp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Tls12SocketFactory extends SSLSocketFactory {
    private static final String[] TLS_SUPPORT_VERSION = {"TLSv1","TLSv1.1", "TLSv1.2"};

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

    @Override
    public Socket createSocket() throws IOException {
        return patch(delegate.createSocket());
    }

    private Socket patch(Socket s) {
//        AndroidLogWrapperUtil.d("Tls12SocketFactory","path s is  why why ");
        if (s instanceof SSLSocket) {

//            ArrayList<String> arrayList = new ArrayList();
//            AndroidLogWrapperUtil.d("Tls12SocketFactory","path s is ");
//            String [] m =((SSLSocket) s).getEnabledProtocols();
//            for (String item : m){
//                AndroidLogWrapperUtil.d("Tls12SocketFactory","item is :: "+item);
//                if(item.toUpperCase().contains("TLS")){
//                    arrayList.add(item);
//                }
//            }
//            if(!arrayList.contains("TLSv1.1")){
//                arrayList.add("TLSv1.1");
//            }
//            if(!arrayList.contains("TLSv1.2")){
//                arrayList.add("TLSv1.2");
//            }
//            int size = arrayList.size();
//            String sss[] = new String[size];
//            for (int i=0;i<size;i++){
//                sss[i] = arrayList.get(i);
//            }
            ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);

        }
        return s;
    }
}
