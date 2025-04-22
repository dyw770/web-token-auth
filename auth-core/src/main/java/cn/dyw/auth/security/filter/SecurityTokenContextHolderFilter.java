/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.dyw.auth.security.filter;

import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * 上下文注入
 *
 * @author dyw770
 * @since 2025-02-14
 */
public class SecurityTokenContextHolderFilter extends GenericFilterBean {

    private static final String FILTER_APPLIED = SecurityTokenContextHolderFilter.class.getName() + ".APPLIED";

    private final SecurityTokenRepository securityTokenRepository;

    private final TokenResolve tokenResolve;

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();


    public SecurityTokenContextHolderFilter(SecurityTokenRepository securityTokenRepository, TokenResolve tokenResolve) {
        Assert.notNull(securityTokenRepository, "securityContextRepository cannot be null");
        this.securityTokenRepository = securityTokenRepository;
        this.tokenResolve = tokenResolve;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getAttribute(FILTER_APPLIED) != null) {
            chain.doFilter(request, response);
            return;
        }
        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        String tokenStr = tokenResolve.tokenResolve(request);
        boolean checked = tokenResolve.checkToken(tokenStr);

        SecurityContext context;

        // 如果token正确则从存储中取出上下文
        if (checked) {
            context = this.securityTokenRepository.loadContext(tokenStr);
        } else {
            context = SecurityContextHolder.createEmptyContext();
        }

        Supplier<SecurityContext> deferredContext = () -> context;
        try {
            this.securityContextHolderStrategy.setDeferredContext(deferredContext);
            chain.doFilter(request, response);
        } finally {
            this.securityContextHolderStrategy.clearContext();
            request.removeAttribute(FILTER_APPLIED);
            // 如果存在认证信息，则更新token的过期时间
            if (checked && context.getAuthentication() != null) {
                this.securityTokenRepository.updateExpireTime(tokenStr);
            }
        }
    }


    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

}
