package br.com.fiap.ap.dto.alunocurso;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalhesAlunoCursoDto {

    private int idAlunoCurso;
    private int idAluno;
    private int idCurso;
    private int idStatus;

}