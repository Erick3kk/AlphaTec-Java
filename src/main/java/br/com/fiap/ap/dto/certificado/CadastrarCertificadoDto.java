package br.com.fiap.ap.dto.certificado;

import jakarta.validation.constraints.*;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class CadastrarCertificadoDto {

    private int idAluno;

    private int idCurso;

    private Date dataConclusao;

}
