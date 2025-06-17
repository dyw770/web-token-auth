package cn.dyw.auth.jwt.controller;

import cn.dyw.auth.message.Result;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dyw770
 * @since 2025-02-11
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private JWKSource<SecurityContext> jwkSource;
    
    /**
     * 登出
     *
     * @param authentication 授权信息
     * @param request        request
     * @param response       response
     * @return 结果
     */
    @GetMapping("/logout")
    public Result<String> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);;
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
   
        return Result.createSuccess("登出成功");
    }

 
}
