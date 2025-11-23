package br.com.fiap.ap.dto.statuscurso;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AtualizarStatusCursoDto {

    private int idStatus;

    @NotBlank
    private String descricao;
}
