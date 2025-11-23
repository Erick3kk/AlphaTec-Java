package br.com.fiap.ap.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class Certificado {

    private int idCertificado;
    private Aluno aluno;
    private Curso curso;
    private Date dataConclusao;

    public Certificado() {
    }

    public Certificado(int idCertificado, Aluno aluno, Curso curso, Date dataConclusao) {
        this.idCertificado = idCertificado;
        this.aluno = aluno;
        this.curso = curso;
        this.dataConclusao = dataConclusao;
    }

}
