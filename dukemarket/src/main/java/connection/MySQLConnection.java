package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Cria conexões MySQL usando variáveis de ambiente, sem credenciais no código. */
public final class MySQLConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/dukemarket?useSSL=false&serverTimezone=UTC";
    private MySQLConnection() {}

    public static Connection getConnection() {
        String url = env("DB_URL", DEFAULT_URL);
        String user = env("DB_USER", "root");
        String password = env("DB_PASSWORD", "");
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new IllegalStateException(
                "Não foi possível conectar ao MySQL. Confira se o servidor está ativo e configure DB_URL, DB_USER e DB_PASSWORD.", ex);
        }
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);
        return value == null ? fallback : value;
    }
}
