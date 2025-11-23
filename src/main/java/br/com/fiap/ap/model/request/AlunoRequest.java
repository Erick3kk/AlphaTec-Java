package br.com.fiap.ap.model.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class AlunoRequest {

    private int idAluno;

    private String nome;

    private String email;

    private Date dataNascimento;

    private String senha;

}