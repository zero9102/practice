package com.practice.e2021.validate2log.filter;

import com.practice.e2021.validate2log.servlet.MyRequestWrapper;
import com.practice.e2021.validate2log.servlet.MyResponseWrapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 监听所有请求
 * https://www.jianshu.com/p/1d9ebb00fc9e
 * @author Administrator
 *
 */
@Slf4j
@Component
public class RequestWrapperFilter extends OncePerRequestFilter {
    private MyRequestWrapper requestWrapper;
    private MyResponseWrapper reponseWrapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            requestWrapper = new MyRequestWrapper(request);
            reponseWrapper = new MyResponseWrapper(response);
            filterChain.doFilter(requestWrapper, reponseWrapper);
            String requestBody = requestWrapper.getRequestBody();
            String reponseBody = reponseWrapper.getResponseBody();
                        //做你想做的事情
            log.info("现在接收到: {}", reponseBody);
        } catch (Exception e) {
                        //失败时，默认即可
            filterChain.doFilter(request, response);
        }
    }
}