package br.com.phmiranda.spring.data.model.dto;

import br.com.phmiranda.spring.data.model.enums.Situacao;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UsuarioSituacaoDto {

    @NotNull
    private Situacao situacao;
}
