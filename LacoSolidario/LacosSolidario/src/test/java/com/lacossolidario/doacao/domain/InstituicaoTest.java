package com.lacossolidario.doacao.domain;

import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.model.DadosEndereco;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class InstituicaoTest {


    @Test
    void testConstructorAndGetters() {
        Endereco endereco = new Endereco(new DadosEndereco("Rua A", "12345-678", "10", "Apartamento"));
        Instituicao instituicao = new Instituicao();

        assertNull(instituicao.getId());
        assertNull(instituicao.getEndereco());
        assertTrue(instituicao.getDoacoes().isEmpty());
    }

    @Test
    void testSetters() {
        Instituicao instituicao = new Instituicao();
        instituicao.setId(2L);

        Endereco endereco = new Endereco(new DadosEndereco("Rua A", "12345-678", "10", "Apartamento"));
        instituicao.setEndereco(endereco);

        List<Doacao> doacoes = new ArrayList<>();
        doacoes.add(new Doacao(new DadosCadastroDoacao(1L, "2024-10-11", 1L, 1L, ""), new Instituicao(),
                new Usuario(new DadosCadastroUsuario("Nome do Usuário", "loginUsuario",
                        "senhaSegura", "tipo", "123456789", "12345678901")), new Categoria("Roupa"), "Doar"));
        instituicao.setDoacoes(doacoes);

        assertEquals(2L, instituicao.getId());
        assertEquals(endereco, instituicao.getEndereco());
        assertEquals(1, instituicao.getDoacoes().size());
    }
}