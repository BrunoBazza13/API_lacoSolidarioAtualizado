package com.lacossolidario.doacao.app.service;

import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        final var a = new DadosCadastroUsuario("Nome do Usuário", "loginUsuario", "senhaSegura",
                "tipo", "123456789", "12345678901");
        usuario = new Usuario(a);
        usuario.setId(1L);
        usuario.setLogin("loginUsuario");

        ReflectionTestUtils.setField(tokenService, "secret", "minha_chave_secreta");
    }

    @Test
    void testGerarToken_ComUsuarioValido() {
        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
    }

    @Test
    void testGerarToken_ComUsuarioNulo() {
        String token = tokenService.gerarToken(null);

        assertNull(token);
    }


    @Test
    void testValidateToken_ComTokenValido() {
        String token = tokenService.gerarToken(usuario);

        String subject = tokenService.validateToken(token);

        assertEquals("loginUsuario", subject);
    }

    @Test
    void testValidateToken_ComTokenInvalido() {
        String result = tokenService.validateToken("token_invalido");

        assertEquals("erro", result);
    }
}