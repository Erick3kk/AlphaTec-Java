package br.com.fiap.ap.dto.certificado;

import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.Curso;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter


public class DetalhesCertificadoDto {

    private int idCertificado;
    private Aluno aluno;
    private Curso curso;
    private Date dataConclusao;

}
