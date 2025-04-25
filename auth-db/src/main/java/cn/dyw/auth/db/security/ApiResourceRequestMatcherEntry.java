package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.ApiResourceDto;
import lombok.Getter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author dyw770
 * @since 2025-04-25
 */
@Getter
public class ApiResourceRequestMatcherEntry<T> {

    private final ApiResourceDto dto;

    private final RequestMatcher requestMatcher;

    private final T entry;

    public ApiResourceRequestMatcherEntry(ApiResourceDto dto,
                                          RequestMatcher requestMatcher, 
                                          T entry) {
        this.dto = dto;
        this.requestMatcher = requestMatcher;
        this.entry = entry;
    }
}
