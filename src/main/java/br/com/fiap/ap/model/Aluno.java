package br.com.fiap.ap.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class Aluno {

    private int idAluno;
    private String nome;
    private String email;
    private Date dataNascimento;
    private String senha;

    public Aluno() {
    }

    public Aluno(int idAluno, String nome, String email, Date dataNascimento, String senha) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
    }

}
