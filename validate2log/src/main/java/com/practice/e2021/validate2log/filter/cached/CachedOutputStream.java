package com.practice.e2021.validate2log.filter.cached;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class CachedOutputStream extends ServletOutputStream implements CachedStream {

    private ByteArrayOutputStream cachedOutputStream;
    private ServletOutputStream srcOutputStream;
    private int maxCacheSize;


    public CachedOutputStream(ServletOutputStream srcOutputStream, int initCacheSize, int maxCacheSize) {
        CachedStreamUtils.checkCacheSizeParam(initCacheSize, maxCacheSize);
        this.srcOutputStream = srcOutputStream;
        this.cachedOutputStream = new ByteArrayOutputStream(initCacheSize);
        this.maxCacheSize = maxCacheSize;
    }

    /**
     * 返回缓存的字节数据
     */
    @Override
    public byte[] getCached() {
        return cachedOutputStream.toByteArray();
    }

    /**
     * Checks if a non-blocking write will succeed. If this returns
     * <code>false</code>, it will cause a callback to
     * {@link WriteListener#onWritePossible()} when the buffer has emptied. If
     * this method returns <code>false</code> no further data must be written
     * until the contain calls {@link WriteListener#onWritePossible()}.
     *
     * @return <code>true</code> if data can be written, else <code>false</code>
     * @since Servlet 3.1
     */
    @Override
    public boolean isReady() {
        return srcOutputStream.isReady();
    }

    /**
     * Sets the {@link WriteListener} for this {@link ServletOutputStream} and
     * thereby switches to non-blocking IO. It is only valid to switch to
     * non-blocking IO within async processing or HTTP upgrade processing.
     *
     * @param listener The non-blocking IO write listener
     * @throws IllegalStateException If this method is called if neither
     *         async nor HTTP upgrade is in progress or
     *         if the {@link WriteListener} has already
     *         been set
     * @throws NullPointerException If listener is null
     * @since Servlet 3.1
     */
    @Override
    public void setWriteListener(WriteListener listener) {

    }

    /**
     * Writes the specified byte to this output stream. The general
     * contract for <code>write</code> is that one byte is written
     * to the output stream. The byte to be written is the eight
     * low-order bits of the argument <code>b</code>. The 24
     * high-order bits of <code>b</code> are ignored.
     * <p>
     * Subclasses of <code>OutputStream</code> must provide an
     * implementation for this method.
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs. In particular,
     *         an <code>IOException</code> may be thrown if the
     *         output stream has been closed.
     */
    @Override
    public void write(int b) throws IOException {
        this.srcOutputStream.write(b);
        if (this.cachedOutputStream.size() < maxCacheSize) {
            CachedStreamUtils.safeWrite(cachedOutputStream, b);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.cachedOutputStream.close();
    }
}
