package br.com.fiap.ap.dao;

import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.StatusCurso;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;

@ApplicationScoped
public class StatusCursoDao {

    @Inject
    private DataSource dataSource;

    public StatusCursoDao(DataSource dataSource) throws SQLException, ClassNotFoundException {
        this.dataSource = dataSource;
    }

    public void cadastrar(StatusCurso statusCurso) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO T_AT_STATUS_CURSO (ID_STATUS, DS_DESCRICAO) " +
                    "VALUES (SQ_AT_STATUS_CURSO.nextval, ?)", new String[]{"ID_STATUS"});

            stmt.setString(1, statusCurso.getDescricao());

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Consulta cadastrada com sucesso. Linhas afetadas: " + rowsAffected);

        }
    }
    public StatusCurso buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_STATUS_CURSO WHERE ID_STATUS = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                throw new EntidadeNaoEncontradaException("Status não encontrado");

            return parseStatus(rs);
        }

    }
    public void atualizar(StatusCurso statusCurso) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("UPDATE T_AT_STATUS_CURSO SET DS_DESCRICAO = ? WHERE ID_STATUS = ?");

            stmt.setString(1, statusCurso.getDescricao());
            stmt.setInt(2, statusCurso.getIdStatus());

            stmt.executeUpdate();

        }
    }

    public void deletar(int idStatus) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM T_AT_STATUS_CURSO WHERE ID_STATUS = ?");

            stmt.setInt(1, idStatus);

            ResultSet rs = stmt.executeQuery();

        }
    }

    private StatusCurso parseStatus(ResultSet rs) throws SQLException, EntidadeNaoEncontradaException {

        return new StatusCurso(
                rs.getInt("ID_STATUS"),
                rs.getString("DS_DESCRICAO")
        );
    }
}