package br.com.fiap.ap.dto.statuscurso;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CadastrarStatusCursoDto {


    @NotBlank
    private String descricao;

}
