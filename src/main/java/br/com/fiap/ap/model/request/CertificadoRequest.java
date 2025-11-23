package br.com.fiap.ap.model.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class CertificadoRequest {

    private int idCertificado;

    private int idAluno;

    private int idCurso;

    private Date dataConclusao;

}
