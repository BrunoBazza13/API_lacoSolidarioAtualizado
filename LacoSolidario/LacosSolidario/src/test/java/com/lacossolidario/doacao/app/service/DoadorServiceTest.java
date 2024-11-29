package com.lacossolidario.doacao.app.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.repository.InstituicaoRepository;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.util.Optional;

class DoadorServiceTest {

    @InjectMocks
    private DoadorService doadorService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private InstituicaoRepository instituicaoRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriaUsuario_Success() {
        DadosCadastroUsuario dados = new DadosCadastroUsuario("Nome do Usuário", "login", "senha", "tipo", "123456789", "12345678901");
        Usuario novoUsuario = new Usuario(dados);

        when(usuarioRepository.existsUsuarioByCpf(novoUsuario.getCpf())).thenReturn(false);
        when(passwordEncoder.encode(novoUsuario.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario);

        Usuario usuarioCriado = doadorService.criaUsuario(dados);

        assertEquals(novoUsuario.getNome(), usuarioCriado.getNome());
        assertEquals("senha", usuarioCriado.getSenha());
        verify(usuarioRepository).save(novoUsuario);
    }

    @Test
    void testCriaUsuario_UsuarioComCpfExistente() {
        DadosCadastroUsuario dados = new DadosCadastroUsuario("Nome do Usuário", "login", "senha", "tipo", "123456789", "12345678901");
        Usuario novoUsuario = new Usuario(dados);

        when(usuarioRepository.existsUsuarioByCpf(novoUsuario.getCpf())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            doadorService.criaUsuario(dados);
        });

        assertEquals("Já temos um usuário registrado com este CPF. Por favor, verifique os dados e tente novamente!", exception.getMessage());
    }

    @Test
    void testAtualizarUsuario_Success() throws IOException {
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "Novo Nome", "novaSenha", "tipo", "123456789", "12345678901");
        Usuario usuarioExistente = new Usuario(new DadosCadastroUsuario("Nome do Usuário", "login", "senha", "tipo", "123456789", "12345678901"));

        when(usuarioRepository.findById(dadosAtualizacao.id())).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode(dadosAtualizacao.senha())).thenReturn("novaSenhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioExistente);

        doadorService.atualizarUsuario(dadosAtualizacao);

        assertEquals("Novo Nome", usuarioExistente.getNome());
        assertEquals("novaSenhaCriptografada", usuarioExistente.getSenha());
        verify(usuarioRepository).save(usuarioExistente);
    }

    @Test
    void testAtualizarUsuario_UsuarioNaoEncontrado() {
        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "Novo Nome", null, "tipo", "123456789", "12345678901");

        when(usuarioRepository.findById(dadosAtualizacao.id())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            doadorService.atualizarUsuario(dadosAtualizacao);
        });

        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_UsuarioEncontrado() {
        String username = "login";
        Usuario usuario = new Usuario(new DadosCadastroUsuario("Nome do Usuário", username, "senha", "tipo", "123456789", "12345678901"));

        when(usuarioRepository.findByLogin(username)).thenReturn(usuario);

        UserDetails userDetails = doadorService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
    }



    @Test
    void testObterInstituicao_Success() {
        Instituicao instituicao = new Instituicao();
        when(instituicaoRepository.findById(1L)).thenReturn(Optional.of(instituicao));

        Instituicao resultado = doadorService.obterInstituicao();

        assertNotNull(resultado);
    }

    @Test
    void testObterInstituicao_InstituicaoNaoEncontrada() {
        when(instituicaoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            doadorService.obterInstituicao();
        });

        assertEquals("Instituição não encontrada", exception.getMessage());
    }
}