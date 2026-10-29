# DukeMarket — gerenciamento de estoque em Java

Aplicação de terminal para cadastrar, listar, consultar, alterar e excluir produtos em um banco MySQL. O projeto foi organizado como aplicação Maven e usa JDBC com consultas parametrizadas.

## Requisitos

- JDK 17 ou superior
- Maven 3.9+
- MySQL 8+

## 1. Criar o banco e a tabela

Execute o script `src/main/resources/sql/criar_tabela.sql` no MySQL. Ele cria o banco `dukemarket` e a tabela `produto`.

## 2. Configurar a conexão

A aplicação lê as variáveis de ambiente abaixo. Os valores padrão conectam ao MySQL local com usuário `root` e senha vazia; configure credenciais adequadas ao seu ambiente.

- `DB_URL`: padrão `jdbc:mysql://localhost:3306/dukemarket?useSSL=false&serverTimezone=UTC`
- `DB_USER`: padrão `root`
- `DB_PASSWORD`: padrão vazio

Exemplo no PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/dukemarket?useSSL=false&serverTimezone=UTC"
$env:DB_USER="seu_usuario"
$env:DB_PASSWORD="sua_senha"
```

Exemplo no Linux/macOS:

```bash
export DB_URL="jdbc:mysql://localhost:3306/dukemarket?useSSL=false&serverTimezone=UTC"
export DB_USER="seu_usuario"
export DB_PASSWORD="sua_senha"
```

Não versione senhas reais nem envie arquivos `.env` para o Git.

## 3. Compilar e executar

Dentro da pasta `dukemarket`:

```bash
mvn clean package
mvn exec:java
```

## Funcionalidades

1. Listar produtos
2. Cadastrar produto
3. Consultar produto pelo ID
4. Alterar produto
5. Excluir produto com confirmação
0. Encerrar

## Estrutura

```
dukemarket/
├── pom.xml
├── .gitignore
├── .env.example
└── src/main/
    ├── java/
    │   ├── connection/MySQLConnection.java
    │   └── vargas/dev/dukemarket/
    │       ├── app/DukeMarketApp.java
    │       ├── bean/Produto.java
    │       └── dao/ProdutoDAO.java
    └── resources/sql/criar_tabela.sql
```

## Melhorias aplicadas

- Remoção de credenciais fixas e configuração por variáveis de ambiente.
- Fechamento automático de conexões, statements e result sets.
- Prepared statements para operações de banco.
- Validação básica dos campos e mensagens de erro mais claras.
- Valores monetários representados com `BigDecimal`.
- Menu interativo e confirmação antes da exclusão.
