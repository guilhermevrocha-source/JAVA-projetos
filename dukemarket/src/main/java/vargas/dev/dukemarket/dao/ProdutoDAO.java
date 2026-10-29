package vargas.dev.dukemarket.dao;

import connection.MySQLConnection;
import vargas.dev.dukemarket.bean.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Operações CRUD de Produto usando PreparedStatement. */
public class ProdutoDAO {
    private static final String INSERT = "INSERT INTO produto (codBarras, descricao, qtd, valorCompra, valorVenda) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "SELECT id, codBarras, descricao, qtd, valorCompra, valorVenda, dataCadastro FROM produto ORDER BY id";
    private static final String SELECT_ID = SELECT_ALL.replace(" ORDER BY id", " WHERE id = ?");
    private static final String UPDATE = "UPDATE produto SET codBarras = ?, descricao = ?, qtd = ?, valorCompra = ?, valorVenda = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM produto WHERE id = ?";

    public int create(Produto produto) {
        validar(produto);
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preencher(stmt, produto);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return -1;
        } catch (SQLException ex) {
            throw erro("inserir produto", ex);
        }
    }

    public List<Produto> getResults() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) produtos.add(mapear(rs));
            return produtos;
        } catch (SQLException ex) {
            throw erro("listar produtos", ex);
        }
    }

    public Produto getResultById(int id) {
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ID)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException ex) {
            throw erro("buscar produto", ex);
        }
    }

    public boolean update(Produto produto) {
        validar(produto);
        if (produto.getId() <= 0) throw new IllegalArgumentException("O ID do produto deve ser positivo.");
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            preencher(stmt, produto);
            stmt.setInt(6, produto.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw erro("atualizar produto", ex);
        }
    }

    public boolean delete(int id) {
        if (id <= 0) throw new IllegalArgumentException("O ID do produto deve ser positivo.");
        try (Connection conn = MySQLConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw erro("excluir produto", ex);
        }
    }

    private static void preencher(PreparedStatement stmt, Produto p) throws SQLException {
        stmt.setString(1, p.getCodBarras());
        stmt.setString(2, p.getDescricao().trim());
        stmt.setDouble(3, p.getQtd());
        stmt.setBigDecimal(4, p.getValorCompra());
        stmt.setBigDecimal(5, p.getValorVenda());
    }

    private static Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setCodBarras(rs.getString("codBarras"));
        p.setDescricao(rs.getString("descricao"));
        p.setQtd(rs.getDouble("qtd"));
        p.setValorCompra(rs.getBigDecimal("valorCompra"));
        p.setValorVenda(rs.getBigDecimal("valorVenda"));
        Timestamp data = rs.getTimestamp("dataCadastro");
        if (data != null) p.setDataCadastro(data.toLocalDateTime());
        return p;
    }

    private static void validar(Produto p) {
        if (p == null) throw new IllegalArgumentException("O produto é obrigatório.");
        if (p.getDescricao() == null || p.getDescricao().isBlank()) throw new IllegalArgumentException("A descrição é obrigatória.");
        if (p.getDescricao().trim().length() > 30) throw new IllegalArgumentException("A descrição deve ter no máximo 30 caracteres.");
        if (p.getCodBarras() != null && p.getCodBarras().length() > 15) throw new IllegalArgumentException("O código de barras deve ter no máximo 15 caracteres.");
        if (p.getQtd() < 0 || p.getValorCompra().signum() < 0 || p.getValorVenda().signum() < 0) {
            throw new IllegalArgumentException("Quantidade e valores não podem ser negativos.");
        }
    }

    private static IllegalStateException erro(String operacao, SQLException ex) {
        return new IllegalStateException("Erro ao " + operacao + ": " + ex.getMessage(), ex);
    }
}
