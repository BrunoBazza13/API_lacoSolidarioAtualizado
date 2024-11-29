package com.lacossolidario.doacao.infra.model;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroDoacao (
        @NotNull
        Long categoria,

        String data,

        @NotNull
        Long instituicao,

        @NotNull
        Long usuario,
        
        String descricao
        


){
}
