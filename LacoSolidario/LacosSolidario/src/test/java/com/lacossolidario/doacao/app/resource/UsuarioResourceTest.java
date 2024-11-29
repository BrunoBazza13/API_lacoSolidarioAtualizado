package com.lacossolidario.doacao.app.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.app.service.DoadorService;
import com.lacossolidario.doacao.app.service.TokenService;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.model.DadosLogin;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UsuarioResourceTest {

    @InjectMocks
    private UsuarioResource usuarioResource;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private DoadorService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioResource).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCadastrarUsuario() throws Exception {
        DadosCadastroUsuario dados = new DadosCadastroUsuario("Nome do Usuário", "usuario@exemplo.com", "senhaSegura",
                "doador", "555-1234", "231.457.198-31");
        Usuario usuario = new Usuario(dados);
        usuario.setId(1L);

        when(usuarioService.criaUsuario(any(DadosCadastroUsuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuario/cadastro")
                        .contentType("application/json")
                        .content("{\"nome\": \"Nome do Usuário\", \"login\": \"usuario@exemplo.com\", \"senha\": \"senhaSegura\","
                                + " \"tipo_de_usuario\": \"doador\", \"cpf\": \"231.457.198-31\", \"rg\": \"12345678901\", \"telefone\": \"555-1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/usuario/1")))
                .andExpect(jsonPath("$.nome").value("Nome do Usuário"))
                .andExpect(jsonPath("$.login").value("usuario@exemplo.com"));
    }




    @Test
    void testLogin() throws Exception {
        DadosLogin dadosLogin = new DadosLogin("gabi0@gmail.com", "12");
        final var a = new DadosCadastroUsuario("Nome do Usuário", "loginUsuario", "senhaSegura", "tipo", "123456789", "12345678901");
        Usuario usuario = new Usuario(a);
        String token = "token";

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.gerarToken(usuario)).thenReturn(token);

        mockMvc.perform(post("/usuario/login")
                        .contentType("application/json")
                        .content("{\"login\": \"" + dadosLogin.login() + "\", \"senha\": \"" + dadosLogin.senha() + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    void testDetalharPorId() throws Exception {
        Long id = 1L;
        final var a = new DadosCadastroUsuario("Nome do Usuário", "loginUsuario", "senhaSegura",
                "doador", "555-1234", "23145719831");
        Usuario usuario = new Usuario(a);
        usuario.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuario/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }



    @Test
    void testDesativarUsuario() throws Exception {
        Long id = 1L;
        final var a = new DadosCadastroUsuario("Nome do Usuário", "loginUsuario", "senhaSegura",
                "tipo", "123456789", "12345678901");
        Usuario usuario = new Usuario(a);
        when(repository.getReferenceById(id)).thenReturn(usuario);

        mockMvc.perform(delete("/usuario/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testAtualizar_IllegalArgumentException() throws Exception {
        Long id = 1L;
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "loginUsuario", "usuario@exemplo.com", "senhaSegura", "123456789", "23145719831");

        doThrow(new IllegalArgumentException("Dados inválidos")).when(usuarioService).atualizarUsuario(any(DadosAtualizacaoUsuario.class));

        String jsonContent = new ObjectMapper().writeValueAsString(dadosAtualizacao);

        mockMvc.perform(put("/usuario/{id}", id)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos"));
    }

    @Test
    void testAtualizar_GeneralException() throws Exception {
        Long id = 1L;
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "loginUsuario", "usuario@exemplo.com", "senhaSegura", "123456789", "23145719831");

        doThrow(new RuntimeException()).when(usuarioService).atualizarUsuario(any(DadosAtualizacaoUsuario.class));

        String jsonContent = new ObjectMapper().writeValueAsString(dadosAtualizacao);

        mockMvc.perform(put("/usuario/{id}", id)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isInternalServerError());
    }



    @Test
    void testAtualizar() throws Exception {
        Long id = 1L;
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "loginUsuario", "usuario@exemplo.com", "senhaSegura", "123456789", "23145719831");

        doNothing().when(usuarioService).atualizarUsuario(any(DadosAtualizacaoUsuario.class));

        String jsonContent = new ObjectMapper().writeValueAsString(dadosAtualizacao);

        mockMvc.perform(put("/usuario/{id}", id)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk());
    }


}