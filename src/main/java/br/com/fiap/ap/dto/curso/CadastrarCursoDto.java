package br.com.fiap.ap.dto.curso;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CadastrarCursoDto {

    @NotBlank
    @Size(max = 50, min = 2)
    private String nomeCurso;

    @Size(max = 500, min = 2)
    private String descricao;

    @PositiveOrZero
    private int duracao;

}
