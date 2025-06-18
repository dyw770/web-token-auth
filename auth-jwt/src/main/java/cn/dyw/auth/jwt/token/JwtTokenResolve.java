package cn.dyw.auth.jwt.token;

import cn.dyw.auth.token.TokenCreateContext;
import cn.dyw.auth.token.TokenResolve;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.UUID;

/**
 * jwt
 *
 * @author dyw770
 * @since 2025-06-17
 */
public class JwtTokenResolve implements TokenResolve {

    private final DefaultBearerTokenResolver defaultBearerTokenResolver = new DefaultBearerTokenResolver();

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    private final OAuth2TokenValidator<Jwt> validator;

    public JwtTokenResolve(String tokenHeaderName, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        defaultBearerTokenResolver.setBearerTokenHeaderName(tokenHeaderName);
        validator = JwtValidators.createDefault();
    }

    @Override
    public String tokenResolve(HttpServletRequest request) {
        return defaultBearerTokenResolver.resolve(request);
    }

    @Override
    public JwtToken createToken(Authentication token, TokenCreateContext context) {
        return new JwtToken(generateJwt(token).getTokenValue(), refreshTokenGenerator.generateKey(), LocalDateTime.now());
    }

    @Override
    public String parseUser(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    @Override
    public boolean checkToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        OAuth2TokenValidatorResult validate = validator.validate(jwt);
        return !validate.hasErrors();
    }

    private final StringKeyGenerator refreshTokenGenerator = new Base64StringKeyGenerator(
            Base64.getUrlEncoder().withoutPadding(), 96);

    private Jwt generateJwt(Authentication authentication) {

        Instant issuedAt = Instant.now();
        Instant expiresAt;
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;

        expiresAt = issuedAt.plus(5, ChronoUnit.MINUTES);

        // @formatter:off
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();

        claimsBuilder
                .subject(authentication.getName())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .id(UUID.randomUUID().toString());
        claimsBuilder.notBefore(issuedAt);

        JwsHeader.Builder jwsHeaderBuilder = JwsHeader.with(jwsAlgorithm);

        JwsHeader jwsHeader = jwsHeaderBuilder.build();
        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims));
    }
}
