package br.com.fiap.ap.dto.certificado;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class AtualizarCertificadoDto {

    private int idAluno;

    private int idCurso;

    private Date dataConclusao;
}
