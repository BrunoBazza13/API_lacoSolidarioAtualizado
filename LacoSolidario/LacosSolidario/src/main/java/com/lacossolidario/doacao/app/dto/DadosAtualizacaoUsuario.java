package com.lacossolidario.doacao.app.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
public record DadosAtualizacaoUsuario(
		
        Long id,
        String nome,
        @Email
        String login,
        String senha,
        String telefone,
        String cpf

) {
}
