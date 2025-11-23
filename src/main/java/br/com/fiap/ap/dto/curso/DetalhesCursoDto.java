package br.com.fiap.ap.dto.curso;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DetalhesCursoDto {

    private int idCurso;
    private String nomeCurso;
    private String descricao;
    private int duracao;

}
