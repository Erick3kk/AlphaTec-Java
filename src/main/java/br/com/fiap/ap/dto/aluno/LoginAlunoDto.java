package br.com.fiap.ap.dto.aluno;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginAlunoDto {


    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ter formato valido")
    private String email;

    @NotBlank(message = "A senha é obrigatoria")
    @Size(max = 150, min = 2)
    private String senha;
}
