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
import cn.dyw.auth.token.TokenResolve;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

/**
 * 配置
 *
 * @param <H>
 * @author dyw770
 */
public final class SecurityTokenContextConfigurer<H extends HttpSecurityBuilder<H>>
        extends AbstractHttpConfigurer<SecurityTokenContextConfigurer<H>, H> {


    public SecurityTokenContextConfigurer() {
    }


    public SecurityTokenContextConfigurer<H> securityContextRepository(SecurityTokenRepository securityTokenRepository) {
        getBuilder().setSharedObject(SecurityTokenRepository.class, securityTokenRepository);
        return this;
    }

    public SecurityTokenContextConfigurer<H> tokenResolve(TokenResolve tokenResolve) {
        getBuilder().setSharedObject(TokenResolve.class, tokenResolve);
        return this;
    }


    private SecurityTokenRepository getSecurityTokenRepository() {
        return getBuilder()
                .getSharedObject(SecurityTokenRepository.class);
    }

    private TokenResolve getTokenResolve() {
        return getBuilder()
                .getSharedObject(TokenResolve.class);
    }

    @Override
    public void configure(H http) {
        SecurityTokenRepository securityTokenRepository = getSecurityTokenRepository();
        TokenResolve tokenResolve = getTokenResolve();

        SecurityTokenContextHolderFilter securityContextHolderFilter = postProcess(
                new SecurityTokenContextHolderFilter(securityTokenRepository, tokenResolve));
        securityContextHolderFilter.setSecurityContextHolderStrategy(getSecurityContextHolderStrategy());
        http.addFilterAfter(securityContextHolderFilter, WebAsyncManagerIntegrationFilter.class);

    }

}
