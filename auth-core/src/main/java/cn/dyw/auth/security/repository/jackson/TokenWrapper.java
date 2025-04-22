package cn.dyw.auth.security.repository.jackson;

import cn.dyw.auth.security.TokenAuthenticationToken;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token 包装
 */
@Getter
public class TokenWrapper implements Serializable {

    private final TokenAuthenticationToken token;

    private LocalDateTime expireTime;

    public TokenWrapper(TokenAuthenticationToken token, long expireTime) {
        this.token = token;
        LocalDateTime createTime = this.token.getCreateTime();
        this.expireTime = createTime.plusSeconds(expireTime);
    }

    public TokenWrapper(TokenAuthenticationToken token, LocalDateTime expireTime) {
        this.token = token;
        this.expireTime = expireTime;
    }

    public void updateExpireTime(long expireTime) {
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public boolean isExpired() {
        return expireTime.isBefore(LocalDateTime.now());
    }

    public boolean canRemove(long removeTime) {
        LocalDateTime removeDateTime = expireTime.plusSeconds(removeTime);
        // 如果移出时间小于当前时间，则可以移除
        return removeDateTime.isBefore(LocalDateTime.now());
    }
}
