package br.ufms.sol;

import br.ufms.sol.persistencia.Conexao;
import br.ufms.sol.view.ClienteView;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

/**
 * Ponto de entrada do sistema.
 * Prepara o banco e abre a tela de cadastro de cliente.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // mantem o look and feel padrao
        }

        try {
            Conexao.criarEstrutura();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao preparar o banco de dados:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> new ClienteView().setVisible(true));
    }
}
