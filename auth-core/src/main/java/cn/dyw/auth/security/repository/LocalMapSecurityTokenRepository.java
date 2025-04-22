package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.repository.jackson.TokenWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map实现
 *
 * @author dyw770
 * @since 2025-02-13
 */
public class LocalMapSecurityTokenRepository implements SecurityTokenRepository {

    private final Map<String, TokenWrapper> map;

    /**
     * 过期时间 秒
     */
    private final long expireTime;

    /**
     * 移出时间
     */
    private final long removeTime;

    public LocalMapSecurityTokenRepository(long expireTime, long removeTime) {
        this.map = new ConcurrentHashMap<>(32);
        this.expireTime = expireTime;
        this.removeTime = removeTime;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {
        TokenWrapper wrapper = new TokenWrapper(token, expireTime);
        map.put(token.getToken(), wrapper);
    }

    @Override
    public TokenAuthenticationToken loadToken(String token) {
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

}

