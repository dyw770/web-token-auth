package cn.dyw.auth.db.security;

import cn.dyw.auth.db.model.ApiResourceDto;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author dyw770
 * @since 2025-04-25
 */
public record ApiResourceRequestMatcherEntry<T>(ApiResourceDto dto, RequestMatcher requestMatcher, T entry) {

}
