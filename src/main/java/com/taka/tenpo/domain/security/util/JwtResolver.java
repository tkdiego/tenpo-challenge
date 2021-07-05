package com.taka.tenpo.domain.security.util;

import com.taka.tenpo.domain.security.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.StringUtils.hasText;

public class JwtResolver {

    private static final Logger LOGGER = getLogger(JwtResolver.class);

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_BEARER = "Bearer ";
    private static final int START_TOKEN_INDEX = 7;

    public static String getUsername(String token, String signKey) {

        return ofNullable(getClaims(token, signKey)).map(claim -> claim.getSubject())
                .orElseThrow(() -> {
                    String msg = "User token is invalid.";
                    LOGGER.error(msg);
                    return new InvalidTokenException(msg);
                });
    }

    public static boolean isValid(String token, String signKey) {
        return getClaims(token, signKey) != null;
    }

    public static String getTokenFromHeader(HttpServletRequest request) {
        String tokenHeader = request.getHeader(TOKEN_HEADER);
        return removeBearerToToken(tokenHeader);
    }

    private static String removeBearerToToken(String token) {
        if (containBearer(token)) {
            return token.substring(START_TOKEN_INDEX);
        }
        return token;
    }

    private static boolean containBearer(String token) {
        return hasText(token) && token.startsWith(TOKEN_BEARER);
    }

    private static Claims getClaims(String token, String signKey) {
        String jwt = removeBearerToToken(token);
        if (jwt != null) {
            try {
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(jwt).getBody();
            } catch (SignatureException se) {
                LOGGER.error("Invalid Signature {}", se.getMessage());
            } catch (MalformedJwtException mje) {
                LOGGER.error("Invalid JWT token: {}", mje.getMessage());
            } catch (ExpiredJwtException eje) {
                LOGGER.error("Jwt token is expired: {}", eje.getMessage());
            } catch (UnsupportedJwtException uje) {
                LOGGER.error("Jwt token is unsupported: {}", uje.getMessage());
            } catch (IllegalArgumentException iae) {
                LOGGER.error("Jwt claims string is empty: {}", iae.getMessage());
            }
        }
        return null;
    }

}


