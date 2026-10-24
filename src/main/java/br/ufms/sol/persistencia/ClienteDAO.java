package br.ufms.sol.persistencia;

import br.ufms.sol.negocio.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de Persistencia - DAO (Data Access Object) Pattern.
 * Isola todo o SQL do restante do sistema: a View nunca escreve SQL,
 * ela conversa apenas com objetos Cliente.
 */
public class ClienteDAO {

    private static final String SQL_INCLUIR =
            "INSERT INTO cliente (nome_razao_social, cpf_cnpj, inscricao_estadual, telefone, "
            + "email, endereco, vendedor_responsavel, observacoes_faturamento) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ALTERAR =
            "UPDATE cliente SET nome_razao_social = ?, cpf_cnpj = ?, inscricao_estadual = ?, "
            + "telefone = ?, email = ?, endereco = ?, vendedor_responsavel = ?, "
            + "observacoes_faturamento = ? WHERE id = ?";

    private static final String SQL_EXCLUIR =
            "DELETE FROM cliente WHERE id = ?";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM cliente WHERE id = ?";

    private static final String SQL_BUSCAR =
            "SELECT * FROM cliente WHERE nome_razao_social LIKE ? OR cpf_cnpj LIKE ? "
            + "ORDER BY nome_razao_social";

    private static final String SQL_LISTAR =
            "SELECT * FROM cliente ORDER BY nome_razao_social";

    /**
     * Inclui um novo cliente e devolve o id gerado pelo banco.
     */
    public int incluir(Cliente cliente) throws SQLException {
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_INCLUIR, Statement.RETURN_GENERATED_KEYS)) {
            preencherParametros(ps, cliente);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
            return cliente.getId();
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Altera os dados de um cliente ja cadastrado.
     */
    public void alterar(Cliente cliente) throws SQLException {
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_ALTERAR)) {
            preencherParametros(ps, cliente);
            ps.setInt(9, cliente.getId());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Nenhum cliente encontrado com o id " + cliente.getId());
            }
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Exclui o cliente pelo id.
     */
    public void excluir(int id) throws SQLException {
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_EXCLUIR)) {
            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Nenhum cliente encontrado com o id " + id);
            }
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Busca um unico cliente pelo id. Devolve null quando nao existe.
     */
    public Cliente buscarPorId(int id) throws SQLException {
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_BUSCAR_POR_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? montarCliente(rs) : null;
            }
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Busca clientes por parte do nome ou do documento.
     */
    public List<Cliente> buscar(String filtro) throws SQLException {
        if (filtro == null || filtro.isBlank()) {
            return listarTodos();
        }

        List<Cliente> clientes = new ArrayList<>();
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_BUSCAR)) {
            String termo = "%" + filtro.trim() + "%";
            ps.setString(1, termo);
            ps.setString(2, termo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    clientes.add(montarCliente(rs));
                }
            }
            return clientes;
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Lista todos os clientes cadastrados.
     */
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        Connection conexao = Conexao.abrir();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(montarCliente(rs));
            }
            return clientes;
        } finally {
            Conexao.fechar(conexao);
        }
    }

    /**
     * Preenche os 8 primeiros parametros, comuns ao INSERT e ao UPDATE.
     */
    private void preencherParametros(PreparedStatement ps, Cliente cliente) throws SQLException {
        ps.setString(1, cliente.getNomeRazaoSocial());
        ps.setString(2, cliente.getCpfCnpj());
        ps.setString(3, cliente.getInscricaoEstadual());
        ps.setString(4, cliente.getTelefone());
        ps.setString(5, cliente.getEmail());
        ps.setString(6, cliente.getEndereco());
        ps.setString(7, cliente.getVendedorResponsavel());
        ps.setString(8, cliente.getObservacoesFaturamento());
    }

    /**
     * Converte uma linha do ResultSet em um objeto Cliente.
     */
    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNomeRazaoSocial(rs.getString("nome_razao_social"));
        cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
        cliente.setInscricaoEstadual(rs.getString("inscricao_estadual"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setVendedorResponsavel(rs.getString("vendedor_responsavel"));
        cliente.setObservacoesFaturamento(rs.getString("observacoes_faturamento"));
        return cliente;
    }
}
