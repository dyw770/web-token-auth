package cn.dyw.auth.db.security;

import cn.dyw.auth.utils.RequestUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.util.Assert;

import java.util.function.Supplier;

/**
 * @author dyw770
 * @since 2025-04-28
 */
public class IpAddressAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final IpAddressMatcher ipAddressMatcher;

    private final String ipAddress;

    private IpAddressAuthorizationManager(String ipAddress) {
        this.ipAddress = ipAddress;
        this.ipAddressMatcher = new IpAddressMatcher(ipAddress);
    }


    public static IpAddressAuthorizationManager hasIpAddress(String ipAddress) {
        Assert.notNull(ipAddress, "ipAddress cannot be null");
        return new IpAddressAuthorizationManager(ipAddress);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestAuthorizationContext) {
        return new AuthorizationDecision(
                this.ipAddressMatcher.matches(RequestUtils.getClientIp(requestAuthorizationContext.getRequest())));
    }

    @Override
    public String toString() {
        return "IpAddressAuthorizationManager[ip='" + this.ipAddress + "']";
    }
}
