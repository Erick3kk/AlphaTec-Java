package br.com.fiap.ap.dao;

import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Curso;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CursoDao {

    @Inject
    private DataSource dataSource;

    public CursoDao(DataSource dataSource) throws SQLException, ClassNotFoundException {
        this.dataSource = dataSource;
    }

    public void cadastrar(Curso curso) throws SQLException {
        String sql = "INSERT INTO T_AT_CURSO " +
                "(ID_CURSO, NM_CURSO, DS_DESCRICAO, DR_DURACAO) " +
                "VALUES (SQ_AT_CURSO.NEXTVAL, ?, ?, ?)";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"ID_CURSO"})) {

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getDuracao());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    curso.setIdCurso(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Curso curso) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE T_AT_CURSO " +
                "SET NM_CURSO = ?, DS_DESCRICAO = ?, DR_DURACAO = ? " +
                "WHERE ID_CURSO = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getDuracao());
            stmt.setInt(4, curso.getIdCurso());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new EntidadeNaoEncontradaException("Curso não encontrado para atualização");
            }
        }
    }

    public void deletar(int idCurso) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM T_AT_CURSO WHERE ID_CURSO = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCurso);

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new EntidadeNaoEncontradaException("Curso não encontrado para remoção");
            }
        }
    }

    public Curso buscar(int idCurso) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM T_AT_CURSO WHERE ID_CURSO = ?";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCurso);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Curso não encontrado");
                }
                return parseCurso(rs);
            }
        }
    }

    public List<Curso> listar() throws SQLException {
        String sql = "SELECT * FROM T_AT_CURSO ORDER BY ID_CURSO";

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Curso> cursos = new ArrayList<>();
            while (rs.next()) {
                cursos.add(parseCurso(rs));
            }
            return cursos;
        }
    }

    Curso parseCurso(ResultSet rs) throws SQLException {
        int idCurso     = rs.getInt("ID_CURSO");
        String nome     = rs.getString("NM_CURSO");
        String descricao = rs.getString("DS_DESCRICAO");
        int duracao     = rs.getInt("DR_DURACAO");

        return new Curso(idCurso, nome, descricao, duracao);
    }
}


