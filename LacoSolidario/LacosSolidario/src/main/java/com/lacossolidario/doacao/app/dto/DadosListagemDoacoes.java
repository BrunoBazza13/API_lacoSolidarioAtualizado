package com.lacossolidario.doacao.app.dto;


import com.lacossolidario.doacao.domain.Categoria;
import com.lacossolidario.doacao.domain.Doacao;
import com.lacossolidario.doacao.domain.Instituicao;

public record DadosListagemDoacoes (
//        Long id,
        String data,
        Instituicao instituicao,
        String descricao,
        Categoria categoria
       
) {

        public DadosListagemDoacoes(Doacao doacao) {
                this( doacao.getData(), doacao.getInstituicao(), doacao.getDescricao(), doacao.getCategoria());
        }

		
}

