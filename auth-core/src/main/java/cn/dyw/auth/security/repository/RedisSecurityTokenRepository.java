package cn.dyw.auth.security.repository;

import cn.dyw.auth.exception.BusinessException;
import cn.dyw.auth.message.MessageCode;
import cn.dyw.auth.security.TokenAuthenticationToken;
import cn.dyw.auth.security.serializable.UserLoginDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author dyw770
 * @since 2025-02-21
 */
@Slf4j
public class RedisSecurityTokenRepository extends AbstractSecurityTokenRepository {

    /**
     * 过期时间 秒
     */
    private final long expireTime;

    /**
     * key prefix
     */
    private final String keyPrefix;

    private final TokenResolve tokenResolve;


    /**
     * redis
     */
    private final RedisTemplate<String, UserLoginDetails> redisTemplate;

    public RedisSecurityTokenRepository(RedisTemplate<String, UserLoginDetails> redisTemplate,
                                        TokenResolve tokenResolve,
                                        long expireTime,
                                        String keyPrefix,
                                        UserDetailsService userDetailsService) {
        super(userDetailsService);
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        this.tokenResolve = tokenResolve;
    }

    @Override
    public void savaToken(TokenAuthenticationToken token) {
        UserLoginDetails details = token.getDetails();
        ValueOperations<String, UserLoginDetails> ops = redisTemplate.opsForValue();

        String key = buildRedisKey(token.getToken().token());

        ops.set(key, details, expireTime, TimeUnit.SECONDS);
    }

    @Override
    protected UserLoginDetails internalLoadToken(String token) {
        String key = buildRedisKey(token);
        return redisTemplate.opsForValue().getAndExpire(key, expireTime, TimeUnit.SECONDS);
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
        UserLoginDetails details = redisTemplate.opsForValue().get(key);
        return ObjectUtils.isEmpty(details);
    }

    @Override
    public void updateExpireTime(String token, long expireTime) {
        String key = buildRedisKey(token);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void updateExpireTime(String token) {
        updateExpireTime(token, expireTime);
    }

    @Override
    public void expireToken(String token) {
        String key = buildRedisKey(token);
        redisTemplate.expire(key, 0, TimeUnit.SECONDS);
    }

    @Override
    public void removeExpireToken() {
        // redis 无需实现该方法
    }

    @Override
    public int userTokens(String username) {
        if (StringUtils.hasText(username)) {
            return userKeys(username).size();
        } else {
            return 0;
        }
    }

    @Override
    public List<UserLoginDetails> listUserTokens(String username) {
        if (StringUtils.hasText(username)) {
            List<String> keys = userKeys(username);
            
            if (CollectionUtils.isEmpty(keys)) {
                return List.of();
            }
            List<UserLoginDetails> list = redisTemplate.opsForValue().multiGet(keys);
            if (CollectionUtils.isEmpty(list)) {
                return List.of();
            }
            return list;
        } else {
            return List.of();
        }
    }
    
    private List<String> userKeys(String username) {

        ScanOptions options = ScanOptions.scanOptions()
                .match(keyPrefix + username + ":*")
                .count(100)
                .build();
        
        List<String> keys = new ArrayList<>(12);

        try (Cursor<String> scan = redisTemplate.scan(options)){
            while (scan.hasNext()) {
                keys.add(scan.next());
            }
        } catch (Exception e) {
            log.error("扫描redis key 失败", e);
            throw new BusinessException(MessageCode.ERROR);
        }
        return keys;
    }

    private String buildRedisKey(String token) {
        String username = tokenResolve.parseUser(token);
        return keyPrefix + username + ":" + token;
    }
}
