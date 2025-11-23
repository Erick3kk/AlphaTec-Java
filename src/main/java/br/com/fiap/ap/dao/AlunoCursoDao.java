package br.com.fiap.ap.dao;

import br.com.fiap.ap.dto.alunocurso.AtualizarAlunoCursoDto;
import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.AlunoCurso;
import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.Curso;
import br.com.fiap.ap.model.StatusCurso;
import br.com.fiap.ap.model.request.AlunoCursoRequest;
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
public class AlunoCursoDao {

    @Inject
    private DataSource dataSource;
    private final AlunoDao alunoDao;
    private final CursoDao cursoDao;
    private final StatusCursoDao statusCursoDao;


    public AlunoCursoDao(DataSource dataSource, StatusCursoDao statusCursoDao) throws SQLException, ClassNotFoundException {
        this.dataSource = dataSource;
        this.statusCursoDao = new StatusCursoDao(this.dataSource);
        this.cursoDao = new CursoDao(this.dataSource);
        this.alunoDao = new AlunoDao(this.dataSource);
    }


    public int cadastrar(AlunoCursoRequest alunoCursoRequest) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_AT_ALUNOS_CURSO (ID_ALUNO_CURSO, ID_ALUNO, ID_CURSO, ID_STATUS) " +
                            "VALUES (SQ_AT_ALUNOS_CURSO.nextval, ?, ?, ?)", new String[]{"ID_ALUNO_CURSO"});

            stmt.setInt(1, alunoCursoRequest.getIdAluno());
            stmt.setInt(2, alunoCursoRequest.getIdCurso());
            stmt.setInt(3, alunoCursoRequest.getIdStatus());


            int rowsAffected = stmt.executeUpdate();
            System.out.println("AlunoCurso cadastrado com sucesso. Linhas afetadas: " + rowsAffected);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    alunoCursoRequest.setIdAlunoCurso(rs.getInt(1));
                }

                return alunoCursoRequest.getIdAlunoCurso();

            } catch (SQLException e) {
                System.err.println("Erro ao cadastrar AlunoCurso: " + e.getMessage());
                throw e;
            }

        }
    }

    public AlunoCurso buscar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_ALUNOS_CURSO WHERE ID_ALUNO_CURSO = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
                throw new EntidadeNaoEncontradaException("AlunoCurso não encontrado");

            return parseAlunoCurso(rs);
        }
    }

    public void atualizar(AlunoCursoRequest alunoCursoRequest) throws SQLException, EntidadeNaoEncontradaException {

        // ATENÇÃO: Adicione o @Transactional no serviço/resource que chama este método
        // se você estivesse usando Panache, mas como é JDBC, isso não é estritamente
        // necessário aqui, mas é uma boa prática fechar a conexão no bloco finally.

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("UPDATE T_AT_ALUNOS_CURSO SET ID_STATUS = ? WHERE ID_ALUNO_CURSO = ?")) {

            // 1. SET ID_STATUS (índice 1)
            stmt.setInt(1, alunoCursoRequest.getIdStatus());

            // 2. WHERE ID_ALUNO_CURSO (índice 2)
            stmt.setInt(2, alunoCursoRequest.getIdAlunoCurso()); // <-- CORREÇÃO PRINCIPAL AQUI

            stmt.executeUpdate();

        } catch (SQLException e) {
            // Logar o erro
            throw e;
        }
    }

    public List<AlunoCurso> listarAlunoCurso() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_ALUNOS_CURSO");

            ResultSet rs = stmt.executeQuery();
            List<AlunoCurso> alunosCursos = new ArrayList<>();
            while (rs.next()) {
                AlunoCurso alunoCurso = parseAlunoCurso(rs);
                alunosCursos.add(alunoCurso);
            }
            return alunosCursos;
        }
    }

    public List<AlunoCurso> listarAlunoCursoPorAluno(int idAluno) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_ALUNOS_CURSO WHERE ID_ALUNO = ? ");
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            List<AlunoCurso> alunosCursos = new ArrayList<>();
            while (rs.next()) {
                AlunoCurso alunoCurso = parseAlunoCurso(rs);
                alunosCursos.add(alunoCurso);
            }
            return alunosCursos;
        }
    }


    public void deletar(int idAlunoCurso) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM T_AT_ALUNOS_CURSO WHERE ID_ALUNO_CURSO = ?");
            stmt.setInt(1, idAlunoCurso);

            if (stmt.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Não tem AlunoCurso para deletar");
        }
    }


    private AlunoCurso parseAlunoCurso(ResultSet rs) throws SQLException, EntidadeNaoEncontradaException {
        int idAluno = rs.getInt("ID_ALUNO");
        int idCurso = rs.getInt("ID_CURSO");
        int idStatus = rs.getInt("ID_STATUS");


        Aluno aluno = alunoDao.buscar(idAluno);
        Curso curso = cursoDao.buscar(idCurso);
        StatusCurso status = statusCursoDao.buscar(idStatus);


        return new AlunoCurso(
                rs.getInt("ID_ALUNO_CURSO"),
                aluno,
                curso,
                status

        );
    }


}