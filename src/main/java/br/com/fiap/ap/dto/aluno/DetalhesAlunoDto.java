package br.com.fiap.ap.dto.aluno;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class DetalhesAlunoDto {

    private int idAluno;
    private String nome;
    private String email;
    private Date dataNascimento;
    private String senha;

}
