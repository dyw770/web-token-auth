package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.repository.jackson.TokenWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dyw770
 * @since 2025-02-21
 */
public class RedisMapSecurityTokenRepository implements SecurityTokenRepository {

    /**
     * 过期时间 秒
     */
    private final long expireTime;

    /**
     * 移出时间
     */
    private final long removeTime;

    /**
     * key prefix
     */
    private final String keyPrefix;

    private final TokenResolve tokenResolve;
    
    private final UserDetailsService userDetailsService;

    /**
     * redis
     */
    private final RedisTemplate<String, TokenWrapper> redisTemplate;

    public RedisMapSecurityTokenRepository(RedisTemplate<String, TokenWrapper> redisTemplate,
                                           TokenResolve tokenResolve,
                                           long expireTime,
                                           long removeTime,
                                           String keyPrefix, 
                                           UserDetailsService userDetailsService) {
        this.expireTime = expireTime;
        this.removeTime = removeTime;
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.tokenResolve = tokenResolve;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {
        TokenWrapper wrapper = new TokenWrapper(token, expireTime);
        ValueOperations<String, TokenWrapper> ops = redisTemplate.opsForValue();

        String key = buildRedisKey(token.getToken());

        ops.set(key, wrapper, expireTime + removeTime, TimeUnit.SECONDS);
    }

    @Override
    public void refreshUser(String username) {
        Set<String> keys = redisTemplate.keys(keyPrefix + username + ":");
        if (keys.isEmpty()) {
            return;
        }
        for (String key : keys) {
            ValueOperations<String, TokenWrapper> operations = redisTemplate.opsForValue();
            TokenWrapper wrapper = operations.get(key);
            if (ObjectUtils.isEmpty(wrapper) || wrapper.isExpired()) {
                continue;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            TokenAuthenticationToken authenticationToken = new TokenAuthenticationToken(
                    wrapper.getToken().getPrincipal(),
                    wrapper.getToken().getCredentials(),
                    wrapper.getToken().getToken(),
                    userDetails.getAuthorities());

            operations.set(key, new TokenWrapper(authenticationToken, wrapper.getExpireTime()), expireTime + removeTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public TokenAuthenticationToken loadToken(String token) {
        String key = buildRedisKey(token);
        TokenWrapper authToken = redisTemplate.opsForValue().get(key);
        if (authToken == null) {
            return null;
        }
        if (authToken.isExpired()) {
            return null;
        }
        return authToken.getToken();
    }

    @Override
    public void removeToken(String token) {
        String key = buildRedisKey(token);

        redisTemplate.delete(key);
    }

    @Override
    public boolean containsToken(String token) {
        String key = buildRedisKey(token);

        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean isExpired(String token) {
        String key = buildRedisKey(token);

        TokenWrapper tokenWrapper = redisTemplate.opsForValue().get(key);
        if (tokenWrapper != null) {
            return tokenWrapper.isExpired();
        }

        return true;
    }

    @Override
    public void updateExpireTime(String token, long expireTime) {
        String key = buildRedisKey(token);

        TokenWrapper tokenWrapper = redisTemplate.opsForValue().get(key);

        if (tokenWrapper != null && !tokenWrapper.isExpired()) {
            tokenWrapper.updateExpireTime(expireTime);
            redisTemplate.opsForValue().set(key, tokenWrapper, expireTime + removeTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public void updateExpireTime(String token) {
        updateExpireTime(token, expireTime);
    }

    @Override
    public void expireToken(String token) {
        String key = buildRedisKey(token);

        TokenWrapper tokenWrapper = redisTemplate.opsForValue().get(key);
        if (tokenWrapper != null && !tokenWrapper.isExpired()) {
            tokenWrapper.updateExpireTime(0);
            redisTemplate.opsForValue().set(key, tokenWrapper, removeTime, TimeUnit.SECONDS);
        }
    }

    @Override
    public void removeExpireToken() {
        // redis 无需实现该方法
    }

    @Override
    public int userTokens(String username) {
        if (StringUtils.hasText(username)) {
            Set<String> keys = redisTemplate.keys(keyPrefix + username + ":");
            return keys.size();
        } else {
            return 0;   
        }
    }

    private String buildRedisKey(String token) {
        String username = tokenResolve.parseUser(token);
        return keyPrefix + username + ":" + token;
    }
}
