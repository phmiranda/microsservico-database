package br.com.phmiranda.spring.data.model.dto;

import br.com.phmiranda.spring.data.model.Usuario;
import br.com.phmiranda.spring.data.model.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private Long id;
    private String nome;
    private String email;
    private Situacao situacao;

    public static UsuarioDto fromEntity(Usuario usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSituacao()
        );
    }
}
