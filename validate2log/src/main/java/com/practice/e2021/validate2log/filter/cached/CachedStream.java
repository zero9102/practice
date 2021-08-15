package com.practice.e2021.validate2log.filter.cached;

import java.io.Closeable;

/**
 * 可以在内存中缓存为字节数组的流，主要用于记录流读取或者写入的数据
 */
public interface CachedStream extends Closeable {

    /**
     * 返回缓存的字节数据
     */
    byte[] getCached();
}
