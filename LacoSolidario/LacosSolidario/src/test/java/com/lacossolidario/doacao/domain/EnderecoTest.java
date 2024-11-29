package com.lacossolidario.doacao.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.lacossolidario.doacao.infra.model.DadosEndereco;

class EnderecoTest {

    @Test
    void testConstructorAndGetters() {
        DadosEndereco dadosEndereco = new DadosEndereco("Rua A", "12345-678", "10", "Apartamento");
        Endereco endereco = new Endereco(dadosEndereco);

        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("12345-678", endereco.getCep());
        assertEquals("10", endereco.getNumero());
    }

    @Test
    void testSetters() {
        Endereco endereco = new Endereco(new DadosEndereco("Rua A", "12345-678", "10", "Apartamento"));
        endereco.setId(1L);
        endereco.setLogradouro("Rua B");
        endereco.setCep("98765-432");
        endereco.setNumero("20");

        assertEquals(1L, endereco.getId());
        assertEquals("Rua B", endereco.getLogradouro());
        assertEquals("98765-432", endereco.getCep());
        assertEquals("20", endereco.getNumero());
    }

    @Test
    void testAtualizarEnderecoComDadosNaoNulos() {
        Endereco endereco = new Endereco(new DadosEndereco("Rua A", "12345-678", "10", "Apartamento"));
        DadosEndereco dadosEndereco = new DadosEndereco("Rua A", "12345-678", "10", "Apartamento");
        endereco.atualizarEndereco(dadosEndereco);

        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("12345-678", endereco.getCep());
        assertEquals("10", endereco.getNumero());
    }

    @Test
    void testAtualizarEnderecoComDadosNulos() {
        Endereco endereco = new Endereco(new DadosEndereco("Rua A", "12345-678", "10", "Apartamento"));
        DadosEndereco dadosEndereco = new DadosEndereco(null, null, null, null);
        endereco.atualizarEndereco(dadosEndereco);

        assertEquals("Rua A", endereco.getLogradouro());
        assertEquals("12345-678", endereco.getCep());
        assertEquals("10", endereco.getNumero());
    }
}