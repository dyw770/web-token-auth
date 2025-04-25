package cn.dyw.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author dyw770
 * @since 2025-04-25
 */
public class IpAddressMatcherTests {
    
    @Test
    public void testIpAddressMatcher() throws Exception {
        IpAddressMatcher matcher = new IpAddressMatcher("192.168.0.0/24");
        assertTrue(matcher.matches("192.168.0.1"));
        assertTrue(matcher.matches("192.168.0.2"));

        assertFalse(matcher.matches("192.168.1.2"));
    }
    
}
