package br.com.fiap.ap.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Curso {

    private int idCurso;
    private String nomeCurso;
    private String descricao;
    private int duracao;

    public Curso() {
    }

    public Curso(int idCurso, String nomeCurso, String descricao, int duracao) {
        this.idCurso = idCurso;
        this.nomeCurso = nomeCurso;
        this.descricao = descricao;
        this.duracao = duracao;
    }

}
