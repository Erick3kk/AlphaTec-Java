package br.com.fiap.ap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AlunoCurso {

    private int idAlunoCurso;
    private Aluno aluno;
    private Curso curso;
    private StatusCurso status;

}
