package br.com.fiap.ap.dao;

import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.request.AlunoRequest;
import br.com.fiap.ap.model.response.LoginResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static br.com.fiap.ap.utils.Metodos.formatDataHora;

@ApplicationScoped
public class AlunoDao {

    @Inject
    private DataSource dataSource;


    public AlunoDao(DataSource dataSource) throws SQLException, ClassNotFoundException {
        this.dataSource = dataSource;
    }

    public void cadastrar(AlunoRequest aluno) throws SQLException {
        String sql = """
        INSERT INTO T_AT_ALUNO 
        (ID_ALUNO, NM_ALUNO, DS_EMAIL, DT_NASCIMENTO, PWD_SENHA) 
        VALUES (SQ_AT_ALUNO.NEXTVAL, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)
        """;

        try (Connection conexao = dataSource.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql, new String[]{"ID_ALUNO"})) {  // ← FUNCIONA NO ORACLE!

            String dataHoraStr = formatDataHora(aluno.getDataNascimento());

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setString(3, dataHoraStr);  // formato "yyyy-MM-dd"
            stmt.setString(4, aluno.getSenha());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    aluno.setIdAluno(rs.getInt(1));  // ← Aqui pega o ID gerado
                }
            }
        }
    }


    public void atualizar(Aluno aluno) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("UPDATE T_AT_ALUNO SET DS_EMAIL = ?, PWD_SENHA = ? " +
                    " WHERE ID_ALUNO = ?");

            stmt.setString(1, aluno.getEmail());
            stmt.setString(2, aluno.getSenha());
            stmt.setInt(3, aluno.getIdAluno());
            stmt.executeUpdate();

            if (stmt.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Aluno não encontrado para atualização");
        }
    }


    public void deletar(int idAluno) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM T_AT_ALUNO WHERE ID_ALUNO = ?");

            stmt.setInt(1, idAluno);

            if (stmt.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Aluno não encontrado para remoção");
        }
    }

    public Aluno buscar(int idAluno) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_ALUNO WHERE ID_ALUNO = ?");
            stmt.setInt(1, idAluno);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                throw new EntidadeNaoEncontradaException("Aluno não encontrado");
            return parseAluno(rs);

        }
    }

    public List<Aluno> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM T_AT_ALUNO");
            ResultSet rs = stmt.executeQuery();

            List<Aluno> alunos = new ArrayList<>();
            while (rs.next()) {
                Aluno aluno = parseAluno(rs);
                alunos.add(aluno);
            }
            return alunos;
        }
    }

    public LoginResponse login(String email, String senha ) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(" SELECT * FROM T_AT_ALUNO " +
                    " WHERE DS_EMAIL = ? " +
                    " AND   PWD_SENHA   = ? ");

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            LoginResponse aluno = new LoginResponse();
            while (rs.next()) {
                aluno = parseLogin(rs);
            }
            return aluno;
        }
    }

    Aluno parseAluno(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_ALUNO");
        String nome = rs.getString("NM_ALUNO");
        String email = rs.getString("DS_EMAIL");
        Date dataNascimento = rs.getDate("DT_NASCIMENTO");
        String senha = rs.getString("PWD_SENHA");



        return new Aluno(id, nome, email, dataNascimento, senha);
    }

    private LoginResponse parseLogin(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_ALUNO");
        String nome = rs.getString("NM_ALUNO");
        String email = rs.getString("DS_EMAIL");
        Date dataNascimento = rs.getDate("DT_NASCIMENTO");
        String senha = rs.getString("PWD_SENHA");

        return new LoginResponse(id, nome, email, dataNascimento, senha);
    }
}