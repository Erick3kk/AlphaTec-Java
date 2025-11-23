package br.com.fiap.ap.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LoginRequest {

    private String email;

    private String senha;

}
