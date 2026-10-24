package br.ufms.sol.view;

import br.ufms.sol.negocio.Cliente;
import br.ufms.sol.persistencia.ClienteDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

/**
 * Camada de Interface com o usuario.
 * Nao conhece SQL nem JDBC: fala apenas com o ClienteDAO e com a classe Cliente.
 */
public class ClienteView extends JFrame {

    private final ClienteDAO dao = new ClienteDAO();

    private final JTextField txtId = new JTextField(6);
    private final JTextField txtNome = new JTextField(28);
    private final JTextField txtCpfCnpj = new JTextField(18);
    private final JTextField txtInscricao = new JTextField(18);
    private final JTextField txtTelefone = new JTextField(18);
    private final JTextField txtEmail = new JTextField(28);
    private final JTextField txtEndereco = new JTextField(28);
    private final JTextField txtVendedor = new JTextField(18);
    private final JTextField txtObservacoes = new JTextField(28);
    private final JTextField txtFiltro = new JTextField(20);

    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nome / Razao Social", "CPF / CNPJ", "Telefone", "Vendedor"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabela = new JTable(modelo);

    public ClienteView() {
        setTitle("SOL Pintura Eletrostatica - Cadastro de Cliente (RF-01)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(criarPainelFormulario(), BorderLayout.NORTH);
        add(criarPainelTabela(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        txtId.setEditable(false);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> carregarSelecionado());

        pack();
        setLocationRelativeTo(null);
        atualizarTabela(null);
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        adicionar(painel, gbc, 0, 0, "ID:", txtId);
        adicionar(painel, gbc, 2, 0, "Nome / Razao Social:", txtNome);
        adicionar(painel, gbc, 0, 1, "CPF / CNPJ:", txtCpfCnpj);
        adicionar(painel, gbc, 2, 1, "Inscricao Estadual:", txtInscricao);
        adicionar(painel, gbc, 0, 2, "Telefone / WhatsApp:", txtTelefone);
        adicionar(painel, gbc, 2, 2, "E-mail:", txtEmail);
        adicionar(painel, gbc, 0, 3, "Endereco completo:", txtEndereco);
        adicionar(painel, gbc, 2, 3, "Vendedor responsavel:", txtVendedor);
        adicionar(painel, gbc, 0, 4, "Obs. de faturamento:", txtObservacoes);

        return painel;
    }

    private void adicionar(JPanel painel, GridBagConstraints gbc, int coluna, int linha,
                           String rotulo, JTextField campo) {
        gbc.gridx = coluna;
        gbc.gridy = linha;
        painel.add(new JLabel(rotulo), gbc);

        gbc.gridx = coluna + 1;
        painel.add(campo, gbc);
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(6, 6));
        painel.setBorder(BorderFactory.createTitledBorder("Clientes cadastrados"));

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> atualizarTabela(txtFiltro.getText()));
        busca.add(new JLabel("Nome ou documento:"));
        busca.add(txtFiltro);
        busca.add(btnBuscar);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(820, 220));

        painel.add(busca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnNovo = new JButton("Novo");
        JButton btnIncluir = new JButton("Incluir");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");

        btnNovo.addActionListener(e -> limparFormulario());
        btnIncluir.addActionListener(e -> incluir());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());

        painel.add(btnNovo);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnIncluir);
        painel.add(btnAlterar);
        painel.add(btnExcluir);
        return painel;
    }

    private void incluir() {
        try {
            Cliente cliente = lerFormulario();
            cliente.validar();
            dao.incluir(cliente);

            JOptionPane.showMessageDialog(this, "Cliente incluido com o codigo " + cliente.getId());
            limparFormulario();
            atualizarTabela(null);
        } catch (IllegalArgumentException e) {
            avisar(e.getMessage());
        } catch (SQLException e) {
            erro("Nao foi possivel incluir o cliente", e);
        }
    }

    private void alterar() {
        if (txtId.getText().isBlank()) {
            avisar("Selecione um cliente na tabela antes de alterar.");
            return;
        }

        try {
            Cliente cliente = lerFormulario();
            cliente.setId(Integer.parseInt(txtId.getText()));
            cliente.validar();
            dao.alterar(cliente);

            JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso.");
            atualizarTabela(null);
        } catch (IllegalArgumentException e) {
            avisar(e.getMessage());
        } catch (SQLException e) {
            erro("Nao foi possivel alterar o cliente", e);
        }
    }

    private void excluir() {
        if (txtId.getText().isBlank()) {
            avisar("Selecione um cliente na tabela antes de excluir.");
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
                "Confirma a exclusao do cliente " + txtNome.getText() + "?",
                "Excluir cliente", JOptionPane.YES_NO_OPTION);

        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            dao.excluir(Integer.parseInt(txtId.getText()));
            limparFormulario();
            atualizarTabela(null);
        } catch (SQLException e) {
            erro("Nao foi possivel excluir o cliente", e);
        }
    }

    private void atualizarTabela(String filtro) {
        try {
            List<Cliente> clientes = dao.buscar(filtro);
            modelo.setRowCount(0);

            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{
                        c.getId(), c.getNomeRazaoSocial(), c.getCpfCnpj(),
                        c.getTelefone(), c.getVendedorResponsavel()
                });
            }
        } catch (SQLException e) {
            erro("Nao foi possivel carregar os clientes", e);
        }
    }

    private void carregarSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }

        try {
            int id = (int) modelo.getValueAt(linha, 0);
            Cliente cliente = dao.buscarPorId(id);
            if (cliente != null) {
                preencherFormulario(cliente);
            }
        } catch (SQLException e) {
            erro("Nao foi possivel carregar o cliente selecionado", e);
        }
    }

    private Cliente lerFormulario() {
        Cliente cliente = new Cliente();
        cliente.setNomeRazaoSocial(txtNome.getText().trim());
        cliente.setCpfCnpj(txtCpfCnpj.getText().trim());
        cliente.setInscricaoEstadual(txtInscricao.getText().trim());
        cliente.setTelefone(txtTelefone.getText().trim());
        cliente.setEmail(txtEmail.getText().trim());
        cliente.setEndereco(txtEndereco.getText().trim());
        cliente.setVendedorResponsavel(txtVendedor.getText().trim());
        cliente.setObservacoesFaturamento(txtObservacoes.getText().trim());
        return cliente;
    }

    private void preencherFormulario(Cliente cliente) {
        txtId.setText(String.valueOf(cliente.getId()));
        txtNome.setText(cliente.getNomeRazaoSocial());
        txtCpfCnpj.setText(cliente.getCpfCnpj());
        txtInscricao.setText(cliente.getInscricaoEstadual());
        txtTelefone.setText(cliente.getTelefone());
        txtEmail.setText(cliente.getEmail());
        txtEndereco.setText(cliente.getEndereco());
        txtVendedor.setText(cliente.getVendedorResponsavel());
        txtObservacoes.setText(cliente.getObservacoesFaturamento());
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        txtCpfCnpj.setText("");
        txtInscricao.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtVendedor.setText("");
        txtObservacoes.setText("");
        tabela.clearSelection();
    }

    private void avisar(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    private void erro(String contexto, Exception e) {
        JOptionPane.showMessageDialog(this, contexto + ":\n" + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
