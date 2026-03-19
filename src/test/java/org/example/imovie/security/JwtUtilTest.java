package org.example.imovie.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "iMovieSecretKeyForJWTAuthenticationPleaseChangeInProduction2026");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
        jwtUtil.init();
    }

    @Test
    void generateToken_shouldReturnNonNullToken() {
        String token = jwtUtil.generateToken("testuser");
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void getAccountFromToken_shouldReturnCorrectAccount() {
        String account = "testuser";
        String token = jwtUtil.generateToken(account);
        assertThat(jwtUtil.getAccountFromToken(token)).isEqualTo(account);
    }

    @Test
    void validateToken_validToken_shouldReturnTrue() {
        String token = jwtUtil.generateToken("testuser");
        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_invalidToken_shouldReturnFalse() {
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_emptyString_shouldReturnFalse() {
        assertThat(jwtUtil.validateToken("")).isFalse();
    }

    @Test
    void generateToken_differentAccounts_shouldReturnDifferentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        assertThat(token1).isNotEqualTo(token2);
    }
}
