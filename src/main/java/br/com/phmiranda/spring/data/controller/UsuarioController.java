package br.com.phmiranda.spring.data.controller;

import br.com.phmiranda.spring.data.model.dto.UsuarioDto;
import br.com.phmiranda.spring.data.model.dto.UsuarioRequestDto;
import br.com.phmiranda.spring.data.model.dto.UsuarioSituacaoDto;
import br.com.phmiranda.spring.data.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> listar(@PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(usuarioService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> criar(@Valid @RequestBody UsuarioRequestDto dto) {
        UsuarioDto usuario = usuarioService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<UsuarioDto> atualizarSituacao(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioSituacaoDto dto
    ) {
        return ResponseEntity.ok(usuarioService.atualizarSituacao(id, dto.getSituacao()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
