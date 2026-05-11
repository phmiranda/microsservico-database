package br.com.phmiranda.spring.data.service;

import br.com.phmiranda.spring.data.model.Usuario;
import br.com.phmiranda.spring.data.model.dto.UsuarioDto;
import br.com.phmiranda.spring.data.model.dto.UsuarioRequestDto;
import br.com.phmiranda.spring.data.model.enums.Situacao;
import br.com.phmiranda.spring.data.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UsuarioDto> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorId(Long id) {
        return UsuarioDto.fromEntity(buscarEntidadePorId(id));
    }

    @Transactional
    public UsuarioDto criar(UsuarioRequestDto dto) {
        validarEmailDisponivel(dto.getEmail());

        Usuario usuario = new Usuario(
                dto.getNome(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getSenha())
        );

        return UsuarioDto.fromEntity(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDto atualizar(Long id, UsuarioRequestDto dto) {
        Usuario usuario = buscarEntidadePorId(id);
        validarEmailDisponivelParaUsuario(dto.getEmail(), id);

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return UsuarioDto.fromEntity(usuario);
    }

    @Transactional
    public UsuarioDto atualizarSituacao(Long id, Situacao situacao) {
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setSituacao(situacao);
        return UsuarioDto.fromEntity(usuario);
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setSituacao(Situacao.INATIVO);
    }

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    }

    private void validarEmailDisponivel(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }
    }

    private void validarEmailDisponivelParaUsuario(String email, Long id) {
        if (usuarioRepository.existsByEmailAndIdNot(email, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
        }
    }
}
