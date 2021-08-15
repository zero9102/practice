package com.practice.e2021.validate2log.filter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.practice.e2021.validate2log.constant.Constants;
import com.practice.e2021.validate2log.filter.cached.CachedHttpServletRequestWrapper;
import com.practice.e2021.validate2log.filter.cached.CachedHttpServletResponseWrapper;
import com.practice.e2021.validate2log.filter.cached.CachedStream;
import com.practice.e2021.validate2log.utils.UriMatcher;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AccessDataLogFilter extends OncePerRequestFilter {


    private static final int MAX_CACHE_LEN = 2 * 1024 * 1024;
    private static final int INIT_CACHE_LEN = 512 * 1024;

    // TODO 排除的url
    private String excludeUri;

    private static final Set<String> TEXT_CONTENT_TYPES = Sets.newHashSet(ContentType.APPLICATION_JSON.getMimeType(),
            ContentType.APPLICATION_XML.getMimeType(), ContentType.TEXT_HTML.getMimeType(),
            ContentType.TEXT_XML.getMimeType(),
            ContentType.TEXT_PLAIN.getMimeType());
    private static final int DEFAULT_IGNORE_LENGTH = 4096;
    private static final String DEFAULT_IGNORE_TEXT = "{not text or too large, ignored}";

    @Value("${request.log.force.url:xxx}")
    private String forceUrl;

    private List<String> forceLogUrls = Lists.newArrayList();
    private static final Splitter URL_SPLITTER = Splitter.on(", ").omitEmptyStrings().trimResults();

    @PostConstruct
    public void init() {
        this.forceLogUrls = URL_SPLITTER.splitToList(forceUrl);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (UriMatcher.match(excludeUri, request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        putTrace(request);

        long start = System.currentTimeMillis();
        CachedHttpServletRequestWrapper httpServletRequestWrapper = new CachedHttpServletRequestWrapper(request,
                INIT_CACHE_LEN, MAX_CACHE_LEN);
        CachedHttpServletResponseWrapper httpServletResponseWrapper = new CachedHttpServletResponseWrapper(response,
                INIT_CACHE_LEN, MAX_CACHE_LEN);

        try{
            filterChain.doFilter(httpServletRequestWrapper, httpServletResponseWrapper);
        } finally {
            long end = System.currentTimeMillis();
            saveLog(request, httpServletRequestWrapper, httpServletResponseWrapper, end - start);
        }

    }

    private void saveLog(HttpServletRequest request, CachedHttpServletRequestWrapper httpServletRequestWrapper,
            CachedHttpServletResponseWrapper httpServletResponseWrapper, long time) {
        try{
            // 如果使用了Writer就需要刷新流
            httpServletRequestWrapper.flushStream();
            httpServletResponseWrapper.flushStream();

            byte[] requestData = httpServletRequestWrapper.getCachedStream().getCached();
            byte[] responseData = httpServletResponseWrapper.getCachedStream().getCached();

            String requestString = requestData == null ? StringUtils.EMPTY : new String(requestData);
            String responseString = responseData == null ? StringUtils.EMPTY : new String(responseData);
            String uri = request.getRequestURI();

            if (!forceLog(uri)) {
                // 非 text 内容，隐藏
                if (!isTextContentType(httpServletRequestWrapper.getContentType())) {
                    requestString = DEFAULT_IGNORE_TEXT;
                }
                if (!isTextContentType(httpServletResponseWrapper.getContentType())) {
                    responseString = DEFAULT_IGNORE_TEXT;
                }

                // 太长的隐藏
                responseString = ignoreIfTooLarge(responseString);
            }

            // TODO 把关键数据马赛克化，避免敏感数据泄漏，暂不支持模糊匹配
            // 注意这里返回的map不能更改，所以需要复制一份
            Map params = request.getParameterMap();
            params = Maps.newHashMap(params);

            String paramString = StringUtils.EMPTY;
            List<String> pairs = Lists.newArrayList();

            if (MapUtils.isNotEmpty(params)) {
                for (Object name : params.keySet()) {
                    Object value = params.get(name);
                    if (value instanceof String) {
                        pairs.add(name + "=" + StringUtils.trimToEmpty((String) value));
                    } else if (value instanceof String[]) {
                        String[] values = (String[]) value;
                        for (String v : values) {
                            pairs.add(name + "=" + StringUtils.trimToEmpty(v));
                        }
                    } else if (value != null) {
                        pairs.add(name + "=" + value.toString());
                    }
                }
                paramString = Joiner.on("&").join(pairs);
            }

            if (StringUtils.equals(request.getContentType(), MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                paramString = URLDecoder.decode(paramString, "UTF-8");
            }

            StringBuilder buffer = new StringBuilder();
            buffer.append("time=");
            buffer.append(time + " ms");
            buffer.append(",uri=");
            buffer.append(request.getRequestURI());
            buffer.append(", params=");
            buffer.append(paramString);
            buffer.append(", request=");
            buffer.append(requestString.replaceAll("\n|\r", ""));
            buffer.append(", response=");
            buffer.append(responseString.replaceAll("\n|\r", ""));

            logger.info(buffer.toString());

        }catch (Throwable e) {
            log.warn("log request data error", e);
        }finally {
            closeQuietly(httpServletRequestWrapper.getCachedStream());
            closeQuietly(httpServletResponseWrapper.getCachedStream());
        }
    }

    private void closeQuietly(CachedStream cachedStream) {
        IOUtils.closeQuietly(cachedStream);
    }

    private boolean forceLog(String uri) {
        for (String forceLogUrl : forceLogUrls) {
            if (UriMatcher.match(forceLogUrl, uri)) {
                return true;
            }
        }
        return false;
    }

    private String ignoreIfTooLarge(String content) {
        if (content != null && content.length() > DEFAULT_IGNORE_LENGTH) {
            return DEFAULT_IGNORE_TEXT;
        }
        return content;
    }

    private boolean isTextContentType(String contentType) {
        if (contentType == null) {
            return true;
        }

        for (String textContentType : TEXT_CONTENT_TYPES) {
            if (contentType.contains(textContentType)) {
                return true;
            }
        }
        return false;
    }

    private String putTrace(HttpServletRequest request) {
        String spanId = StringUtils.defaultIfBlank(request.getHeader(Constants.SPAN_ID), StringUtils.EMPTY);
        String traceId = StringUtils.defaultIfBlank(request.getHeader(Constants.TRACE_ID), StringUtils.EMPTY);

        if (StringUtils.isEmpty(traceId)) {
            traceId = StringUtils.defaultIfBlank(MDC.get(Constants.GUID), StringUtils.EMPTY);
        }
        String traceLog = Constants.TRACE_ID_IN_LOG + "[" + traceId + "]-"
                + Constants.SPAN_ID_IN_LOG + "[" + spanId + "]";
        MDC.put(Constants.MDC_KEY, traceLog);

        return traceLog;
    }

    public void setExcludeUri(String excludeUri) {
        this.excludeUri = excludeUri;
    }
}
