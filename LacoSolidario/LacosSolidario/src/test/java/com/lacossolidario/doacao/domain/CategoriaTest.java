package com.lacossolidario.doacao.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CategotiaTest {

        @Test
        void testConstructorAndGetters() {
            Categoria categoria = new Categoria("Alimentos");
            assertNull(categoria.getId());
            assertEquals("Alimentos", categoria.getNome());
        }

        @Test
        void testSetters() {
            Categoria categoria = new Categoria("nome");

            categoria.setId(1L);
            categoria.setNome("Brinquedos");

            assertEquals(1L, categoria.getId());
            assertEquals("Brinquedos", categoria.getNome());
        }
}