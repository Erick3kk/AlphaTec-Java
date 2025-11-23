package br.com.fiap.ap.resource;

import br.com.fiap.ap.dao.AlunoCursoDao;
import br.com.fiap.ap.dto.aluno.AtualizarAlunoDto;
import br.com.fiap.ap.dto.alunocurso.AtualizarAlunoCursoDto;
import br.com.fiap.ap.dto.alunocurso.DetalhesAlunoCursoDto;
import br.com.fiap.ap.dto.alunocurso.CadastrarAlunoCursoDto;
import br.com.fiap.ap.dto.alunocurso.DetalhesAlunoCursoDto;
import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.AlunoCurso;
import br.com.fiap.ap.model.request.AlunoCursoRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/alunoscursos") // Novo Path
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlunoCursoResource {

    @Inject
    private AlunoCursoDao alunoCursoDao;

    @Inject
    private ModelMapper modelMapper;


    @DELETE
    @Path("/deletar/{id}")
    public Response deletar(@PathParam("id") int id) throws EntidadeNaoEncontradaException, SQLException {
        alunoCursoDao.deletar(id);
        return Response.noContent().build();
    }

    @PUT
    @Path("/atualizar/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarAlunoCursoDto dto)
            throws EntidadeNaoEncontradaException, SQLException {

        // 1. Mapeia o DTO (contém apenas o novo ID_STATUS) para o Request (DAO)
        AlunoCursoRequest alunoCursoRequest = modelMapper.map(dto, AlunoCursoRequest.class);

        // 2. CORREÇÃO: Pegue o ID do PathParam e set no campo de ID da Request.
        // O idStatus já foi mapeado na linha acima.
        alunoCursoRequest.setIdAlunoCurso(id); // <-- CORREÇÃO PRINCIPAL AQUI

        alunoCursoDao.atualizar(alunoCursoRequest);

        return Response.ok().build();
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesAlunoCursoDto dto = modelMapper.map(alunoCursoDao.buscar(id), DetalhesAlunoCursoDto.class);
        return Response.ok(dto).build();
    }


    @GET
    @Path("/listar")
    public List<AlunoCurso> listar() throws SQLException, EntidadeNaoEncontradaException {
        return alunoCursoDao.listarAlunoCurso();

    }

    @GET
    @Path("/cursosAluno/{idAluno}") // Path alterado para refletir a lógica AlunoCurso
    public Response cursosAluno(@PathParam("idAluno") int idAluno) throws SQLException {

        List<AlunoCurso> lstAlunoCurso = alunoCursoDao.listarAlunoCursoPorAluno(idAluno); // Método alterado
        return Response.ok(lstAlunoCurso).build();

    }

    @POST
    @Path("/criar")
    public Response criar(@Valid CadastrarAlunoCursoDto dto, @Context UriInfo uriInfo)
            throws SQLException {

        AlunoCursoRequest alunoCurso = modelMapper.map(dto, AlunoCursoRequest.class);

        int idGerado = alunoCursoDao.cadastrar(alunoCurso);

        URI location = uriInfo.getAbsolutePathBuilder()
                .path("/buscar/{id}")
                .build(idGerado);

        DetalhesAlunoCursoDto resposta = new DetalhesAlunoCursoDto(idGerado, dto.getIdAluno(), dto.getIdCurso(), dto.getIdStatus());

        return Response.created(location).entity(resposta).build();
    }
}
