package cn.dyw.auth.jwt.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.repository.AbstractSecurityTokenRepository;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-06-17
 */
public class JwtSecurityTokenRepository extends AbstractSecurityTokenRepository {
    
    public JwtSecurityTokenRepository(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected UserLoginDetails internalLoadToken(String token) {
        return null;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {

    }

    @Override
    public void removeToken(String token) {

    }

    @Override
    public boolean containsToken(String token) {
        return false;
    }

    @Override
    public boolean isExpired(String token) {
        return false;
    }

    @Override
    public void updateExpireTime(String token, long expireTime) {

    }

    @Override
    public void updateExpireTime(String token) {

    }

    @Override
    public void expireToken(String token) {

    }

    @Override
    public void removeExpireToken() {

    }

    @Override
    public int userTokens(String username) {
        return 0;
    }

    @Override
    public List<UserLoginDetails> listUserTokens(String username) {
        return List.of();
    }
}
