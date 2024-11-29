package com.lacossolidario.doacao.app.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lacossolidario.doacao.app.dto.DadosAtualizacaoUsuario;
import com.lacossolidario.doacao.domain.Instituicao;
import com.lacossolidario.doacao.domain.Usuario;
import com.lacossolidario.doacao.infra.model.DadosCadastroUsuario;
import com.lacossolidario.doacao.infra.repository.InstituicaoRepository;
import com.lacossolidario.doacao.infra.repository.UsuarioRepository;
import com.teste.api.exception.RepositoryNotInjectedException;

@Service
public class DoadorService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private InstituicaoRepository instituicaoRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	

	public Usuario criaUsuario(DadosCadastroUsuario dados) {
		Usuario novoUsuario = new Usuario(dados);

		if (usuarioRepository.existsUsuarioByCpf(novoUsuario.getCpf())) {
			throw new IllegalArgumentException(
					"Já temos um usuário registrado com este CPF. Por favor, verifique os dados e tente novamente!");
		}
		
		novoUsuario.setTipoDeUsuario("doador");

		String encryptedPassword = passwordEncoder.encode(novoUsuario.getSenha());
		novoUsuario.setSenha(encryptedPassword);

		return usuarioRepository.save(novoUsuario);
	}

	public void atualizarUsuario(DadosAtualizacaoUsuario dados) throws IOException {
		Usuario usuario = usuarioRepository.findById(dados.id())
				.orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

		// Atualizar senha se fornecida
		if (dados.senha() != null && !dados.senha().isEmpty()) {
			if (!passwordEncoder.matches(dados.senha(), usuario.getSenha())) {
				String encryptedPassword = passwordEncoder.encode(dados.senha());
				usuario.setSenha(encryptedPassword);
			}
		}

		// Atualizar outros campos se fornecidos
		if (dados.nome() != null) {
			usuario.setNome(dados.nome());
		}
		if (dados.login() != null) {
			usuario.setLogin(dados.login());
		}
		if (dados.telefone() != null) {
			usuario.setTelefone(dados.telefone());
		}
		if (dados.cpf() != null) {
			usuario.setCpf(dados.cpf());
		}

		// Salvar o usuário atualizado no repositório
		usuarioRepository.save(usuario);
	}

	@Override
	public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return usuarioRepository.findByLogin(username);
	}
	
	public Usuario obterUsuarioPorLogin(String login) throws RepositoryNotInjectedException {
		if (usuarioRepository == null) {
			throw new RepositoryNotInjectedException("UsuarioRepository não foi injetado");
		}
		return usuarioRepository.findByLogin(login);
	}

	public Instituicao obterInstituicao() {
		return instituicaoRepository.findById(1L) // Assume que a única instituição tem ID 1
				.orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
	}
	

}
