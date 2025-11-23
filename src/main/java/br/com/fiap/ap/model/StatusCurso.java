package br.com.fiap.ap.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StatusCurso {

    private int idStatus;
    private String descricao;

    public StatusCurso() {
    }

    public StatusCurso(int idStatus, String descricao) {
        this.idStatus = idStatus;
        this.descricao = descricao;
    }

}
