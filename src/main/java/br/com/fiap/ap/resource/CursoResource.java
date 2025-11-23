package br.com.fiap.ap.resource;

import br.com.fiap.ap.dao.CursoDao;
import br.com.fiap.ap.dto.curso.AtualizarCursoDto;
import br.com.fiap.ap.dto.curso.CadastrarCursoDto;
import br.com.fiap.ap.dto.curso.DetalhesCursoDto;
import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Curso;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/cursos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CursoResource {

    @Inject
    private CursoDao cursoDao;

    @Inject
    private ModelMapper modelMapper;

    @DELETE
    @Path("/deletar/{id}")
    public Response deletar(@PathParam("id") int id) throws EntidadeNaoEncontradaException, SQLException {
        cursoDao.deletar(id);
        return Response.noContent().build(); // 204
    }

    @PUT
    @Path("/atualizar")
    public Response atualizar(@Valid AtualizarCursoDto dto)
            throws EntidadeNaoEncontradaException, SQLException {
        Curso curso = modelMapper.map(dto, Curso.class);
        cursoDao.atualizar(curso);
        return Response.ok().build(); // 200
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        DetalhesCursoDto dto = modelMapper.map(cursoDao.buscar(id), DetalhesCursoDto.class);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/listar")
    public List<DetalhesCursoDto> listar() throws SQLException {
        return cursoDao.listar().stream()
                .map(c -> modelMapper.map(c, DetalhesCursoDto.class))
                .toList();
    }

    @POST
    @Path("/criar")
    public Response criar(@Valid CadastrarCursoDto dto, @Context UriInfo uriInfo) throws SQLException {

        Curso curso = modelMapper.map(dto, Curso.class);

        cursoDao.cadastrar(curso);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path("/buscar")
                .path(String.valueOf(curso.getIdCurso()))
                .build();

        return Response.created(uri)
                .entity(modelMapper.map(curso, DetalhesCursoDto.class))
                .build();
    }
}