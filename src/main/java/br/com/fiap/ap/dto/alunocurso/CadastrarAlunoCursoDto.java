package br.com.fiap.ap.dto.alunocurso;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarAlunoCursoDto {

    private int idAlunoCurso;
    private int idAluno;
    private int idCurso;
    private int idStatus;



}