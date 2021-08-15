package com.practice.e2021.validate2log.filter.cached;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachedStreamUtils {

    /**
     * 检查缓存的参数是否有效，无效抛出 IllegalArgumentException
     * @param initCacheSize 初始缓存.
     * @param maxCacheSize 最大缓存.
     */
    public static void checkCacheSizeParam(int initCacheSize, int maxCacheSize) {
        if (initCacheSize <= 0) {
            throw new IllegalArgumentException("init cache size is invalid!");
        }

        if (maxCacheSize <= 0) {
            throw new IllegalArgumentException("max cache size is valid!");
        }
        if (initCacheSize > maxCacheSize) {
            throw new IllegalArgumentException("init cache is large than max cache size!!");
        }
    }

    /**
     * 安全写入流中，如果遇到异常会吞掉，不抛异常
     */
    public static void safeWrite(OutputStream out, int val) {
        try {
            out.write(val);
        } catch (IOException e) {
            log.debug("", e);
            //ignore
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "asdlk=alskdjl";
        System.out.println(URLEncoder.encode(url, "utf-8"));
    }
}
