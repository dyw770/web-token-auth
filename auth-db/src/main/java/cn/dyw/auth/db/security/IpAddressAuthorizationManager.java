package cn.dyw.auth.db.security;

import cn.dyw.auth.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author dyw770
 * @since 2025-04-28
 */
public class IpAddressAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final List<IpAddressMatcher> ipAddressMatchers;

    private final String ipAddress;

    private IpAddressAuthorizationManager(String ipAddress) {
        this.ipAddress = ipAddress;
        this.ipAddressMatchers = new ArrayList<>();

        String[] ips = StringUtils.split(ipAddress, ",");
        for (String ip : ips) {
            this.ipAddressMatchers.add(new IpAddressMatcher(ip));
        }
    }


    public static IpAddressAuthorizationManager hasIpAddress(String ipAddress) {
        Assert.notNull(ipAddress, "ipAddress cannot be null");
        return new IpAddressAuthorizationManager(ipAddress);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestAuthorizationContext) {

        for (IpAddressMatcher ipAddressMatcher : ipAddressMatchers) {
            if (ipAddressMatcher.matches(RequestUtils.getClientIp(requestAuthorizationContext.getRequest()))) {
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);
    }

    @Override
    public String toString() {
        return "IpAddressAuthorizationManager[ip='" + this.ipAddress + "']";
    }
}
