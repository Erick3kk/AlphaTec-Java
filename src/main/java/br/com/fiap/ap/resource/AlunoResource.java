package br.com.fiap.ap.resource;

import br.com.fiap.ap.dao.AlunoDao;
import br.com.fiap.ap.dto.aluno.*;
import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.request.AlunoRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/alunos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlunoResource {

    @Inject
    private AlunoDao alunoDao;

    @Inject
    private ModelMapper modelMapper;

    @DELETE
    @Path("/deletar/{id}")
    public Response deletar(@PathParam("id") int id) throws EntidadeNaoEncontradaException, SQLException {
        alunoDao.deletar(id);
        return Response.noContent().build(); // 204 No Content
    }

    @PUT
    @Path("/atualizar/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarAlunoDto dto)
            throws EntidadeNaoEncontradaException, SQLException {

        Aluno aluno = modelMapper.map(dto, Aluno.class);
        aluno.setIdAluno(id);
        alunoDao.atualizar(aluno);
        return Response.ok().build(); // 200 OK
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesAlunoDto dto = modelMapper.map(alunoDao.buscar(id), DetalhesAlunoDto.class);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/listar")
    public List<DetalhesAlunoDto> listar() throws SQLException {
        return alunoDao.listar().stream()
                .map(a -> modelMapper.map(a, DetalhesAlunoDto.class))
                .toList();
    }

    @POST
    @Path("/criar")
    public Response criar(@Valid CadastrarAlunoDto dto, @Context UriInfo uriInfo) throws SQLException {

        AlunoRequest alunoRequest = modelMapper.map(dto, AlunoRequest.class);
        alunoDao.cadastrar(alunoRequest);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(alunoRequest.getIdAluno()))
                .build();

        return Response.created(uri)
                .entity(modelMapper.map(alunoRequest, DetalhesAlunoDto.class))
                .build();
    }

}