package cn.dyw.auth.security;

import cn.dyw.auth.security.repository.SecurityTokenRepository;
import cn.dyw.auth.security.repository.TokenResolve;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * @author dyw770
 * @since 2025-02-14
 */
public class LoginLogoutHandler {

    private final AuthenticationManager authenticationManager;

    private final SecurityTokenRepository tokenRepository;
    
    private final TokenResolve tokenResolve;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy(); 
    
    public LoginLogoutHandler(AuthenticationManager authenticationManager, SecurityTokenRepository tokenRepository, TokenResolve tokenResolve) {
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.tokenResolve = tokenResolve;
    }

    /**
     * 登陆
     * @param username 用户名
     * @param password 密码
     * @return 认证结果
     */
    public TokenAuthenticationToken login(String username, String password, HttpServletRequest request) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

        String token = tokenResolve.createToken(authenticationResponse);
        TokenAuthenticationToken authenticationToken = 
                new TokenAuthenticationToken(
                        authenticationResponse.getPrincipal(),
                        authenticationResponse.getCredentials(), 
                        token,
                        userAgent,
                        authenticationResponse.getAuthorities());
        authenticationToken.setDetails(authenticationResponse.getDetails());
        
        tokenRepository.savaToken(authenticationToken);

        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticationResponse);
        this.securityContextHolderStrategy.setContext(context);
        
        return authenticationToken;
    }

    /**
     * 登出
     * @param authentication 认证信息
     * @param request 请求
     * @param response 响应
     */
    public void logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        
        this.logoutHandler.logout(request, response, authentication);
        
        String token = tokenResolve.tokenResolve(request);
        tokenRepository.removeToken(token);
    }
}
