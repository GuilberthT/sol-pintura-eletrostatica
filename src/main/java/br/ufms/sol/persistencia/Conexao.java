package br.ufms.sol.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Camada de Persistencia - API JDBC.
 * Responsavel apenas por abrir e fechar a conexao com o banco.
 * Usa SQLite em arquivo local (sol.db), criado automaticamente na primeira execucao.
 */
public class Conexao {

    private static final String URL = "jdbc:sqlite:sol.db";

    private Conexao() {
    }

    /**
     * Abre uma conexao com o banco de dados.
     */
    public static Connection abrir() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL);
        try (Statement st = conexao.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    /**
     * Fecha a conexao recebida, ignorando falhas de fechamento.
     */
    public static void fechar(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                System.err.println("Falha ao fechar conexao: " + e.getMessage());
            }
        }
    }

    /**
     * Cria a tabela cliente caso ela ainda nao exista.
     * Evita qualquer passo manual de setup do banco.
     */
    public static void criarEstrutura() throws SQLException {
        String ddl = "CREATE TABLE IF NOT EXISTS cliente ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome_razao_social TEXT NOT NULL, "
                + "cpf_cnpj TEXT NOT NULL UNIQUE, "
                + "inscricao_estadual TEXT, "
                + "telefone TEXT, "
                + "email TEXT, "
                + "endereco TEXT, "
                + "vendedor_responsavel TEXT, "
                + "observacoes_faturamento TEXT)";

        try (Connection conexao = abrir(); Statement st = conexao.createStatement()) {
            st.execute(ddl);
        }
    }
}
