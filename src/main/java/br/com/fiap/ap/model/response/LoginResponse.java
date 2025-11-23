package br.com.fiap.ap.model.response;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LoginResponse {

    private int idAluno;

    private String nome;

    private String email;

    private Date dataNascimento;

    private String senha;


}
