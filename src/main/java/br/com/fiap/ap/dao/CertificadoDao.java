package br.com.fiap.ap.dao;

import br.com.fiap.ap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.ap.model.Certificado;
import br.com.fiap.ap.model.Aluno;
import br.com.fiap.ap.model.Curso;
import br.com.fiap.ap.model.request.CertificadoRequest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static br.com.fiap.ap.utils.Metodos.formatDataHora;

@ApplicationScoped
public class CertificadoDao {

    @Inject
    private DataSource dataSource;

    private final AlunoDao alunoDao;
    private final CursoDao cursoDao;

    public CertificadoDao() throws SQLException, ClassNotFoundException {
        this.alunoDao = new AlunoDao(this.dataSource);
        this.cursoDao = new CursoDao(this.dataSource);
    }

    public Certificado cadastrar(CertificadoRequest certificado) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {

            PreparedStatement stmt = conexao.prepareStatement(
                    "INSERT INTO T_AT_CERTIFICADO (ID_CERTIFICADO, ID_CURSO, ID_ALUNO, DT_CONCLUSAO) " +
                            "VALUES (SQ_AT_CERTIFICADO.NEXTVAL, ?, ?, ?)", new String[]{"ID_CERTIFICADO"});

            String dataHoraStr = formatDataHora(certificado.getDataConclusao());


            stmt.setInt(1, certificado.getIdCurso());
            stmt.setInt(2, certificado.getIdAluno());
            stmt.setString(3, dataHoraStr);
            stmt.executeUpdate();

            ResultSet resultSet = stmt.getGeneratedKeys();

            int idCertificado = 0;
            if (resultSet.next()) {
                idCertificado = resultSet.getInt(1);
            }

            return buscar(idCertificado);
        }
    }

    public void atualizar(Certificado certificado) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE T_AT_CERTIFICADO SET ID_CURSO = ?, ID_ALUNO = ?, DT_CONCLUSAO = ? " +
                            "WHERE ID_CERTIFICADO = ?");


            stmt.setInt(1, certificado.getCurso().getIdCurso());
            stmt.setInt(2, certificado.getAluno().getIdAluno());
            stmt.setDate(3, certificado.getDataConclusao());
            stmt.setInt(4, certificado.getIdCertificado());
            stmt.executeUpdate();

            if (stmt.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Não existe Certificado para ser atualizado");
        }
    }

    public void deletar(int idCertificado) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement("DELETE FROM T_AT_CERTIFICADO WHERE ID_CERTIFICADO = ?");
            stmt.setInt(1, idCertificado);

            if (stmt.executeUpdate() == 0)
                throw new EntidadeNaoEncontradaException("Não tem Certificado para deletar");
        }
    }

    public Certificado buscar(int idCertificado) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT cert.*, a.*, c.* " +
                            "FROM T_AT_CERTIFICADO cert " +
                            "INNER JOIN T_AT_ALUNO a ON cert.ID_ALUNO = a.ID_ALUNO " +
                            "INNER JOIN T_AT_CURSO c ON cert.ID_CURSO = c.ID_CURSO " +
                            "WHERE cert.ID_CERTIFICADO = ?");

            stmt.setInt(1, idCertificado);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                throw new EntidadeNaoEncontradaException("Certificado não encontrado");
            return parseCertificado(rs);
        }
    }

    public List<Certificado> listar() throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT cert.*, a.*, c.* " +
                            "FROM T_AT_CERTIFICADO cert " +
                            "INNER JOIN T_AT_ALUNO a ON cert.ID_ALUNO = a.ID_ALUNO " +
                            "INNER JOIN T_AT_CURSO c ON cert.ID_CURSO = c.ID_CURSO");
            ResultSet rs = stmt.executeQuery();
            List<Certificado> lista = new ArrayList<>();

            while (rs.next()) {
                Certificado certificado = parseCertificado(rs);
                lista.add(certificado);
            }
            return lista;
        }
    }

    public List<Certificado> listarCertificadosAluno(int idAluno) throws SQLException {
        try (Connection conexao = dataSource.getConnection()) {
            PreparedStatement stmt = conexao.prepareStatement(
                    "SELECT cert.*, c.* " +
                            "FROM T_AT_CERTIFICADO cert " +
                            "INNER JOIN T_AT_CURSO c ON cert.ID_CURSO = c.ID_CURSO " +
                            "WHERE cert.ID_ALUNO = ?");
            stmt.setInt(1, idAluno);
            ResultSet rs = stmt.executeQuery();
            List<Certificado> certificados = new ArrayList<>();
            while (rs.next()) {
                Certificado certificado = parseCertificadoPorAluno(rs);
                certificados.add(certificado);
            }
            return certificados;
        }
    }

    private Certificado parseCertificado(ResultSet rs) throws SQLException {
        int idAluno = rs.getInt("ID_ALUNO");
        int idCurso = rs.getInt("ID_CURSO");

        Aluno aluno = alunoDao.buscar(idAluno);
        Curso curso = cursoDao.buscar(idCurso);

        Certificado certificado = new Certificado();
        certificado.setIdCertificado(rs.getInt("ID_CERTIFICADO"));
        certificado.setAluno(aluno);
        certificado.setCurso(curso);
        certificado.setDataConclusao(rs.getDate("DT_CONCLUSAO"));

        return certificado;
    }

    private Certificado parseCertificadoPorAluno(ResultSet rs) throws SQLException {
        int idCurso = rs.getInt("ID_CURSO");

        Curso curso = cursoDao.buscar(idCurso);

        Certificado certificado = new Certificado();
        certificado.setIdCertificado(rs.getInt("ID_CERTIFICADO"));
        certificado.setCurso(curso);
        certificado.setDataConclusao(rs.getDate("DT_CONCLUSAO"));

        Aluno aluno = new Aluno();
        aluno.setIdAluno(rs.getInt("ID_ALUNO"));
        certificado.setAluno(aluno);

        return certificado;
    }
}