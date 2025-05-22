package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

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
                                           long expireTime,
                                           long removeTime) {
        super(userDetailsService);
        this.map = new ConcurrentHashMap<>(32);
        this.expireTime = expireTime;
        this.removeTime = removeTime;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {
        UserLoginDetails details = token.getDetails();
        TokenWrapper wrapper = new TokenWrapper(details, expireTime);
        map.put(token.getToken(), wrapper);
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
        return authToken.getToken();
    }

    @Override
    public void removeToken(String token) {
        map.remove(token);
    }

    @Override
    public boolean containsToken(String token) {
        return map.containsKey(token);
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
        // TODO 待实现
        return 0;
    }

    @Override
    public List<TokenWrapper> listUserTokens(String username) {
        // TODO 待实现
        return List.of();
    }

}

