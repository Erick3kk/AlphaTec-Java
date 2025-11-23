package br.com.fiap.ap.dto.aluno;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter

public class CadastrarAlunoDto {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, min = 2)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ter formato valido")
    private String email;

    @Past(message = "A data deve estar no passado")
    private Date dataNascimento;

    @Size(max = 150, min = 2)
    private String senha;

}
