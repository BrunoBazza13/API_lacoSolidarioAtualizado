package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoacaoTest {


    @Test
    void testConstructorAndGetters() {
        Categoria categoria = new Categoria("Roupas");
        Instituicao instituicao = new Instituicao();
        Usuario usuario = new Usuario(new DadosCadastroUsuario("Nome do Usuário", "loginUsuario",
                "senhaSegura", "tipo", "123456789", "12345678901"));

        String data = "2024-10-11";
        String descricao = "Doação de roupas de inverno";

        Doacao doacao = new Doacao(new DadosCadastroDoacao(1L, "2024-10-11", 1L, 1L, ""), instituicao, usuario, categoria, descricao);

        assertNull(doacao.getId());
        assertEquals("Roupas", doacao.getCategoria().getNome());
        assertEquals(data, doacao.getData());
        assertEquals(descricao, doacao.getDescricao());
        assertEquals(instituicao, doacao.getInstituicao());
        assertEquals("Nome do Usuário", doacao.getUsuario().getNome());
    }


    @Test
    void testSetters() {
        Doacao doacao = new Doacao( new DadosCadastroDoacao(1L, "2024-10-11", 1L, 1L, ""), new Instituicao(),
                new Usuario(new DadosCadastroUsuario("Nome do Usuário", "loginUsuario",
                        "senhaSegura", "tipo", "123456789", "12345678901")), new Categoria("Roupa"), "Doar");
        Categoria categoria = new Categoria("Brinquedos");
        Instituicao instituicao = new Instituicao();
        Usuario usuario = new Usuario(new DadosCadastroUsuario("Nome do Usuário", "loginUsuario",
                "senhaSegura", "tipo", "123456789", "12345678901"));
        String data = "2024-12-01";
        String descricao = "Doação de brinquedos de Natal";

        doacao.setId(1L);
        doacao.setCategoria(categoria);
        doacao.setData(data);
        doacao.setDescricao(descricao);
        doacao.setInstituicao(instituicao);
        doacao.setUsuario(usuario);

        assertEquals(1L, doacao.getId());
        assertEquals(categoria, doacao.getCategoria());
        assertEquals(data, doacao.getData());
        assertEquals(descricao, doacao.getDescricao());
        assertEquals(instituicao, doacao.getInstituicao());
        assertEquals(usuario, doacao.getUsuario());
    }
}