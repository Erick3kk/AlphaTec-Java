package br.com.fiap.ap.resource;

import br.com.fiap.ap.dao.AlunoDao;
import br.com.fiap.ap.dto.aluno.LoginAlunoDto;
import br.com.fiap.ap.model.request.LoginRequest;
import br.com.fiap.ap.model.response.LoginResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @Inject
    private AlunoDao alunoDao;

    @Inject
    private ModelMapper modelMapper;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginAlunoDto dto) throws SQLException {

        LoginRequest login = modelMapper.map(dto, LoginRequest.class);
        LoginResponse aluno = alunoDao.login(login.getEmail(), login.getSenha());

        if (aluno == null) {
            return Response.status(401).entity("Email ou Senha incorretos").build();
        }
        return Response.ok(aluno).build();
    }
}
