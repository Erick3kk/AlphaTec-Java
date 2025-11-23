package br.com.fiap.ap.resource;

import br.com.fiap.ap.dao.StatusCursoDao;
import br.com.fiap.ap.dto.aluno.DetalhesAlunoDto;
import br.com.fiap.ap.dto.statuscurso.AtualizarStatusCursoDto;
import br.com.fiap.ap.dto.statuscurso.CadastrarStatusCursoDto;
import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.StatusCurso;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;

@Path("/status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatusCursoResource {


    @Inject
    private StatusCursoDao statusCursoDao;

    @Inject
    private ModelMapper modelMapper;

    @POST
    @Path("/criar")
    public Response create(@Valid CadastrarStatusCursoDto dto,
                           @Context UriInfo uriInfo) throws SQLException {

        StatusCurso statusCurso = modelMapper.map(dto, StatusCurso.class);
        statusCursoDao.cadastrar(statusCurso);

        return Response.status(201)
                .entity(statusCurso)
                .build();
    }

    @PUT
    @Path("/atualizar")
    public Response atualizar(@Valid AtualizarStatusCursoDto dto)
            throws EntidadeNaoEncontradaException, SQLException {
        StatusCurso statusCurso = modelMapper.map(dto, StatusCurso.class);

        statusCursoDao.atualizar(statusCurso);
        return Response.ok().build();
    }

    @GET
    @Path("/buscar/{id}")
    public Response buscar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        return Response.ok(statusCursoDao.buscar(id)).build();
    }

    @DELETE
    @Path("/deletar/{id}")
    public Response deletar(@PathParam("id") int id) throws EntidadeNaoEncontradaException, SQLException {
        statusCursoDao.deletar(id);
        return Response.noContent().build();
    }




}
