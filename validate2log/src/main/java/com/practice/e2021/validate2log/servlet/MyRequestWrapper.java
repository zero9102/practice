package com.practice.e2021.validate2log.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import com.alibaba.fastjson.JSONObject;

public class MyRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;
    private ServletInputStreamWrapper inputStreamWrapper;
    private Object requestBody;

    public MyRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        int le = request.getContentLength();
        if (le > 0) {
            body = new byte[le];
            request.getInputStream().read(body);
            requestBody = JSONObject.parse(body, 0, body.length, Charset.forName("UTF-8").newDecoder(), 0);
        } else {
            body = new byte[0];
            requestBody = "";
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
        this.inputStreamWrapper = new ServletInputStreamWrapper(byteArrayInputStream);
        resetInputStream();
    }

    public String getRequestBody() {
        return String.valueOf(requestBody);
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    private void resetInputStream() {
        this.inputStreamWrapper.setInputStream(new ByteArrayInputStream(this.body != null ? this.body : new byte[0]));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.inputStreamWrapper;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.inputStreamWrapper));
    }

    private static class ServletInputStreamWrapper extends ServletInputStream {

        private InputStream inputStream;

        public ServletInputStreamWrapper(InputStream inputStream) {
            super();
            this.inputStream = inputStream;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return this.inputStream.read();
        }
    }
}