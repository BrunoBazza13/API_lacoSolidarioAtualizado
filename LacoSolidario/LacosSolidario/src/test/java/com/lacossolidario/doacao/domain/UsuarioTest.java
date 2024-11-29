package com.lacossolidario.doacao.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioTest {

    @Test
    void testConstructorAndGetters() {
        DadosCadastroUsuario dadosCadastro = new DadosCadastroUsuario("Nome Usuário", "loginUsuario",
                "senhaSegura", "admin", "123456789", "12345678901");

        Usuario usuario = new Usuario(dadosCadastro);

        assertNull(usuario.getId());
        assertEquals("Nome Usuário", usuario.getNome());
        assertEquals("loginUsuario", usuario.getLogin());
        assertEquals("senhaSegura", usuario.getSenha());
        assertEquals("admin", usuario.getTipoDeUsuario());
        assertEquals("123456789", usuario.getTelefone());
        assertEquals("12345678901", usuario.getCpf());
        assertTrue(usuario.getAtivo());
        assertNull(usuario.getDoacoes());
    }

    @Test
    void testAtualizarDados() throws IOException {
        DadosCadastroUsuario dadosCadastro = new DadosCadastroUsuario("Nome Usuário", "loginUsuario",
                "senhaSegura", "admin", "123456789", "12345678901");
        Usuario usuario = new Usuario(dadosCadastro);

        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(1L, "novoLogin",
                "novoLogin", "novaSenha", "novoTelefone", "novoCpf");

        usuario.atualizarDados(dadosAtualizacao);

        assertEquals("novoLogin", usuario.getNome());
        assertEquals("novoLogin", usuario.getLogin());
        assertEquals("novoTelefone", usuario.getTelefone());
        assertEquals("novaSenha", usuario.getSenha());
        assertEquals("novoCpf", usuario.getCpf());
    }

    @Test
    void testAtualizarDadosComDadosNulos() throws IOException {
        DadosCadastroUsuario dadosCadastro = new DadosCadastroUsuario("Nome Usuário", "loginUsuario",
                "senhaSegura", "admin", "123456789", "12345678901");
        Usuario usuario = new Usuario(dadosCadastro);

        DadosAtualizacaoUsuario dadosAtualizacao = new DadosAtualizacaoUsuario(null, null, null, null, null, null);

        usuario.atualizarDados(dadosAtualizacao);

        assertEquals("Nome Usuário", usuario.getNome());
        assertEquals("loginUsuario", usuario.getLogin());
        assertEquals("senhaSegura", usuario.getSenha());
        assertEquals("123456789", usuario.getTelefone());
        assertEquals("12345678901", usuario.getCpf());
    }

    @Test
    void testAdicionarDoacao() {
        DadosCadastroUsuario dadosCadastro = new DadosCadastroUsuario("Nome Usuário", "loginUsuario",
                "senhaSegura", "admin", "123456789", "12345678901");
        Usuario usuario = new Usuario(dadosCadastro);

        Doacao doacao = new Doacao( new DadosCadastroDoacao(1L, "2024-10-11", 1L, 1L, ""), new Instituicao(),
                new Usuario(new DadosCadastroUsuario("Nome do Usuário", "loginUsuario",
                        "senhaSegura", "tipo", "123456789", "12345678901")), new Categoria("Roupa"), "Doar");
        Categoria categoria = new Categoria("Brinquedos");
        List<Doacao> doacoes = new ArrayList<>();
        doacoes.add(doacao);

        usuario.setDoacoes(doacoes);
        assertEquals(1, usuario.getDoacoes().size());
        assertEquals(doacao, usuario.getDoacoes().get(0));
    }
}