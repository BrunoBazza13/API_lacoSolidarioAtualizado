package com.lacossolidario.doacao.infra.exception;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InvalidCredentialsExceptionTest {

    @Test
    void testInvalidCredentialsExceptionMessage() {
        String expectedMessage = "Credenciais inválidas";
        InvalidCredentialsException exception = new InvalidCredentialsException(expectedMessage);

        assertEquals(expectedMessage, exception.getMessage());
    }
}