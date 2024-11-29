package com.lacossolidario.doacao.app.resource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lacossolidario.doacao.app.config.SecurityConfig;
import com.lacossolidario.doacao.infra.repository.SecurityFilter;

class SecurityConfigTest {

    @Mock
    private SecurityFilter securityFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        String senha = "senha123";
        String senhaCodificada = passwordEncoder.encode(senha);

        assertNotNull(senhaCodificada, "A senha codificada não deve ser nula");
        assertTrue(passwordEncoder.matches(senha, senhaCodificada), "A senha deve corresponder à codificação");
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager mockAuthenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthenticationManager);

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(authenticationConfiguration);
        assertNotNull("A", "O AuthenticationManager não deve ser nulo");
    }


}