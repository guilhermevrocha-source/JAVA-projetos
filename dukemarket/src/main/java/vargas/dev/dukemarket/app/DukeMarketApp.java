package vargas.dev.dukemarket.app;

import vargas.dev.dukemarket.bean.Produto;
import vargas.dev.dukemarket.dao.ProdutoDAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/** Interface de terminal para cadastrar, listar, consultar, alterar e excluir produtos. */
public class DukeMarketApp {
    private final Scanner scanner = new Scanner(System.in);
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        new DukeMarketApp().executar();
    }

    private void executar() {
        System.out.println("==================================");
        System.out.println("        DUKE MARKET - ESTOQUE");
        System.out.println("==================================");
        boolean executando = true;
        while (executando) {
            System.out.println("\n1 - Listar produtos\n2 - Cadastrar produto\n3 - Consultar por ID\n4 - Alterar produto\n5 - Excluir produto\n0 - Sair");
            int opcao = lerInteiro("Escolha: ");
            try {
                switch (opcao) {
                    case 1 -> listar();
                    case 2 -> cadastrar();
                    case 3 -> consultar();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> executando = false;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (RuntimeException ex) {
                System.out.println("Não foi possível concluir a operação: " + ex.getMessage());
            }
        }
        scanner.close();
        System.out.println("Programa encerrado.");
    }

    private void listar() {
        List<Produto> produtos = produtoDAO.getResults();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        System.out.println(" ID | Código          | Descrição                      | Quantidade | Compra     | Venda      | Cadastro");
        System.out.println("-".repeat(125));
        produtos.forEach(System.out::println);
    }

    private void cadastrar() {
        Produto p = lerProduto();
        int id = produtoDAO.create(p);
        System.out.println(id > 0 ? "Produto cadastrado com ID " + id + "." : "Produto cadastrado.");
    }

    private void consultar() {
        int id = lerInteiro("ID do produto: ");
        Produto p = produtoDAO.getResultById(id);
        System.out.println(p == null ? "Produto não encontrado." : p);
    }

    private void alterar() {
        int id = lerInteiro("ID do produto que deseja alterar: ");
        Produto p = produtoDAO.getResultById(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + p);
        Produto novos = lerProduto();
        novos.setId(id);
        System.out.println(produtoDAO.update(novos) ? "Produto atualizado." : "Nenhuma alteração realizada.");
    }

    private void excluir() {
        int id = lerInteiro("ID do produto que deseja excluir: ");
        Produto p = produtoDAO.getResultById(id);
        if (p == null) {
            System.out.println("Produto não encontrado.");
            return;
        }
        System.out.println("Confirma a exclusão de: " + p.getDescricao() + "? (s/n)");
        String confirmacao = scanner.nextLine().trim();
        if (confirmacao.equalsIgnoreCase("s")) {
            System.out.println(produtoDAO.delete(id) ? "Produto excluído." : "Produto não encontrado.");
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private Produto lerProduto() {
        Produto p = new Produto();
        System.out.print("Código de barras (opcional): ");
        String codigo = scanner.nextLine().trim();
        p.setCodBarras(codigo.isEmpty() ? null : codigo);
        System.out.print("Descrição: ");
        p.setDescricao(scanner.nextLine().trim());
        p.setQtd(lerDouble("Quantidade: "));
        p.setValorCompra(lerDecimal("Valor de compra: "));
        p.setValorVenda(lerDecimal("Valor de venda: "));
        return p;
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try { return Integer.parseInt(scanner.nextLine().trim()); }
            catch (NumberFormatException ex) { System.out.println("Digite um número inteiro válido."); }
        }
    }

    private double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                if (Double.isFinite(valor) && valor >= 0) return valor;
            } catch (NumberFormatException ignored) { }
            System.out.println("Digite um número válido, maior ou igual a zero.");
        }
    }

    private BigDecimal lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                BigDecimal valor = new BigDecimal(scanner.nextLine().trim().replace(',', '.'));
                if (valor.signum() >= 0) return valor;
            } catch (NumberFormatException ignored) { }
            System.out.println("Digite um valor válido, maior ou igual a zero.");
        }
    }
}
