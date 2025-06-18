package cn.dyw.auth.jwt.security;

import cn.dyw.auth.jwt.repository.JwtSecurityTokenRepository;
import cn.dyw.auth.jwt.token.JwtToken;
import cn.dyw.auth.jwt.token.JwtTokenResolve;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import cn.dyw.auth.token.TokenCreateContext;
import cn.dyw.auth.utils.RequestUtils;
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
public class JwtLoginLogoutHandler {

    private final AuthenticationManager authenticationManager;

    private final JwtSecurityTokenRepository tokenRepository;
    
    private final JwtTokenResolve tokenResolve;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy(); 
    
    public JwtLoginLogoutHandler(AuthenticationManager authenticationManager, JwtSecurityTokenRepository tokenRepository, JwtTokenResolve tokenResolve) {
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
    public JwtTokenAuthenticationToken login(String username, String password, HttpServletRequest request) {

        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticationResponse =
                this.authenticationManager.authenticate(authenticationRequest);

        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String loginIp = RequestUtils.getClientIp(request);

        TokenCreateContext createContext = new TokenCreateContext(username);
        createContext.setLoginIp(loginIp);
        createContext.setLoginUserAgent(userAgent);
        JwtToken token = tokenResolve.createToken(authenticationResponse, createContext);
        UserLoginDetails details = new UserLoginDetails(token, username, userAgent, loginIp);

        JwtTokenAuthenticationToken authenticationToken = 
                new JwtTokenAuthenticationToken(
                        authenticationResponse.getPrincipal(),
                        authenticationResponse.getCredentials(),
                        token,
                        authenticationResponse.getAuthorities());
        
        tokenRepository.savaToken(authenticationToken, details);

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
