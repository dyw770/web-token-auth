package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.serializable.UserLoginDetails;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token 包装
 */
@Getter
public class TokenWrapper implements Serializable {

    private final UserLoginDetails token;

    private LocalDateTime expireTime;


    public TokenWrapper(UserLoginDetails token, long expireTime) {
        this.token = token;
        LocalDateTime createTime = this.token.createTime();
        this.expireTime = createTime.plusSeconds(expireTime);
    }

    @JsonCreator
    public TokenWrapper(@JsonProperty("token") UserLoginDetails token, @JsonProperty("expireTime") LocalDateTime expireTime) {
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
