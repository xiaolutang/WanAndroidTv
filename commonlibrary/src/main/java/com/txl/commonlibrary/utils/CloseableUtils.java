package com.txl.commonlibrary.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {
    public static void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
