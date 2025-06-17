package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map实现
 *
 * @author dyw770
 * @since 2025-02-13
 */
public class LocalMapSecurityTokenRepository extends AbstractSecurityTokenRepository {

    private final Map<String, TokenWrapper> map;

    /**
     * 过期时间 秒
     */
    private final long expireTime;

    /**
     * 移出时间
     */
    private final long removeTime;

    public LocalMapSecurityTokenRepository(UserDetailsService userDetailsService,
                                           long expireTime) {
        super(userDetailsService);
        this.map = new ConcurrentHashMap<>(32);
        this.expireTime = expireTime;
        this.removeTime = 60 * 5;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {
        UserLoginDetails details = token.getDetails();
        TokenWrapper wrapper = new TokenWrapper(details, expireTime);
        map.put(token.getToken().token(), wrapper);
    }
    
    @Override
    protected UserLoginDetails internalLoadToken(String token) {
        TokenWrapper authToken = map.get(token);
        if (authToken == null) {
            return null;
        }
        if (authToken.isExpired()) {
            removeToken(token);
            return null;
        }
        authToken.updateExpireTime(expireTime);
        return authToken.getToken();
    }

    @Override
    public void removeToken(String token) {
        map.remove(token);
    }

    @Override
    public boolean containsToken(String token) {
        return isExpired(token);
    }

    @Override
    public boolean isExpired(String token) {
        if (map.containsKey(token)) {
            TokenWrapper tokenWrapper = map.get(token);
            if (tokenWrapper != null)
                return tokenWrapper.isExpired();
        }
        return true;
    }

    @Override
    public void updateExpireTime(String token, long expireTime) {
        if (!map.containsKey(token)) {
            return;
        }
        synchronized (token.intern()) {
            if (map.containsKey(token)) {
                TokenWrapper tokenWrapper = map.get(token);
                if (!tokenWrapper.isExpired()) {
                    tokenWrapper.updateExpireTime(expireTime);
                }
            }
        }
    }

    @Override
    public void updateExpireTime(String token) {
        updateExpireTime(token, expireTime);
    }

    @Override
    public void expireToken(String token) {
        removeToken(token);
    }

    @Override
    public void removeExpireToken() {
        map.entrySet().removeIf(entry -> entry.getValue().canRemove(removeTime));
    }

    @Override
    public int userTokens(String username) {
        if (StringUtils.hasText(username)) {
            return (int) map.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getToken().username().equals(username))
                    .count();
        }
        return 0;
    }

    @Override
    public List<UserLoginDetails> listUserTokens(String username) {
        if (StringUtils.hasText(username)) {
            return map.values()
                    .stream()
                    .map(TokenWrapper::getToken)
                    .filter(token -> token.username().equals(username))
                    .toList();
        }
        return List.of();
    }

}

