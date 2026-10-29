package vargas.dev.dukemarket.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Entidade que representa um produto do estoque. */
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATA_FORMATADA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private int id;
    private String codBarras;
    private String descricao;
    private double qtd;
    private BigDecimal valorCompra = BigDecimal.ZERO;
    private BigDecimal valorVenda = BigDecimal.ZERO;
    private LocalDateTime dataCadastro;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodBarras() { return codBarras; }
    public void setCodBarras(String codBarras) { this.codBarras = codBarras; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getQtd() { return qtd; }
    public void setQtd(double qtd) { this.qtd = qtd; }
    public BigDecimal getValorCompra() { return valorCompra; }
    public void setValorCompra(BigDecimal valorCompra) { this.valorCompra = valorCompra == null ? BigDecimal.ZERO : valorCompra; }
    public BigDecimal getValorVenda() { return valorVenda; }
    public void setValorVenda(BigDecimal valorVenda) { this.valorVenda = valorVenda == null ? BigDecimal.ZERO : valorVenda; }
    public LocalDateTime getDataCadastroLocalDateTime() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    /** Mantém compatibilidade com o formato usado no código original. */
    public String getDataCadastro() {
        return dataCadastro == null ? "" : dataCadastro.format(DATA_FORMATADA);
    }

    @Override
    public String toString() {
        return String.format("%3d | %-15s | %-30s | %10.2f | %10s | %10s | %19s",
            id, codBarras == null ? "" : codBarras, descricao == null ? "" : descricao,
            qtd, valorCompra.toPlainString(), valorVenda.toPlainString(), getDataCadastro());
    }
}
