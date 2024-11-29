package com.lacossolidario.doacao.app.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.lacossolidario.doacao.app.dto.DadosListagemDoacoes;
import com.lacossolidario.doacao.app.service.DoadorService;
import com.lacossolidario.doacao.app.service.TokenService;
import com.lacossolidario.doacao.domain.Categoria;
import com.lacossolidario.doacao.domain.Doacao;
import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroDoacao;
import com.lacossolidario.doacao.infra.repository.CategoriaRepository;
import com.lacossolidario.doacao.infra.repository.DoacaoRepository;
import com.lacossolidario.doacao.infra.repository.InstituicaoRepository;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;

import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/doacao")
public class DoacaoResource {

	@Autowired
	private DoacaoRepository repository;

	@Autowired
	private InstituicaoRepository instituicaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private DoadorService doadorService;
	
	@Autowired
	private TokenService tokenService;

	@PostMapping
	@Transactional
	public ResponseEntity registrarDoacao(@RequestBody @Valid DadosCadastroDoacao dados,
			UriComponentsBuilder uriBuilder) {

		Instituicao instituicao = instituicaoRepository.findById(dados.instituicao())
				.orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

		Usuario usuario = usuarioRepository.findById(dados.usuario())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		Categoria categoria = categoriaRepository.findById(dados.categoria())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		categoriaRepository.save(categoria);

		String descricao = dados.descricao();

		var doacao = new Doacao(dados, instituicao, usuario, categoria, descricao);
		repository.save(doacao);

	    var uri = uriBuilder.path("/doacao/{id}").buildAndExpand(doacao.getId()).toUri();

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Doação realizada com sucesso!");
	    response.put("doacaoUri", uri.toString());

	    return ResponseEntity.ok(response);
	}


	@GetMapping
	public ResponseEntity<List<DadosListagemDoacoes>> listarDoacoes() {
		var list = repository.findAll().stream().map(DadosListagemDoacoes::new).toList();

		return ResponseEntity.ok(list);

	}
	
	@GetMapping("/carrinho")
	public ResponseEntity<List<DadosListagemDoacoes>> detalharPorId(@AuthenticationPrincipal Usuario usuario) {
	    
		
	    if (usuario == null) {
	        // Retorna 401 Unauthorized se o usuário não estiver logado
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // Obtém as doações associadas ao usuário
	    List<Doacao> doacoes = usuario.getDoacoes();

	    // Verifica se o usuário possui doações
	    if (doacoes != null && !doacoes.isEmpty()) {
	        // Mapeia a lista de Doacao para DadosListagemDoacoes
	        List<DadosListagemDoacoes> listaDeDoacoes = doacoes.stream()
	            .map(DadosListagemDoacoes::new) // Construtor que aceita uma entidade Doacao
	            .collect(Collectors.toList());

	        return ResponseEntity.ok(listaDeDoacoes);
	    }

	    // Retorna uma lista vazia se o usuário não possuir doações
	    return ResponseEntity.ok(Collections.emptyList());
	}



	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id) {
		repository.deleteById(id);

		return ResponseEntity.noContent().build();

	}

}
