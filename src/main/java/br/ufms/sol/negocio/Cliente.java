package br.ufms.sol.negocio;

/**
 * Camada de Negocio.
 * Representa o cliente da SOL Pintura Eletrostatica (RF-01).
 * Atributos definidos no Documento de Requisitos do Grupo G4.
 */
public class Cliente {

    private int id;
    private String nomeRazaoSocial;
    private String cpfCnpj;
    private String inscricaoEstadual;
    private String telefone;
    private String email;
    private String endereco;
    private String vendedorResponsavel;
    private String observacoesFaturamento;

    public Cliente() {
    }

    public Cliente(String nomeRazaoSocial, String cpfCnpj, String inscricaoEstadual,
                   String telefone, String email, String endereco,
                   String vendedorResponsavel, String observacoesFaturamento) {
        this.nomeRazaoSocial = nomeRazaoSocial;
        this.cpfCnpj = cpfCnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.vendedorResponsavel = vendedorResponsavel;
        this.observacoesFaturamento = observacoesFaturamento;
    }

    /**
     * Regra de negocio simples: nome e documento sao obrigatorios.
     * Lanca excecao para a camada de interface tratar.
     */
    public void validar() {
        if (nomeRazaoSocial == null || nomeRazaoSocial.isBlank()) {
            throw new IllegalArgumentException("Nome / Razao Social e obrigatorio.");
        }
        if (cpfCnpj == null || cpfCnpj.isBlank()) {
            throw new IllegalArgumentException("CPF / CNPJ e obrigatorio.");
        }
        String doc = cpfCnpj.replaceAll("\\D", "");
        if (doc.length() != 11 && doc.length() != 14) {
            throw new IllegalArgumentException("CPF deve ter 11 digitos e CNPJ 14 digitos.");
        }
        if (email != null && !email.isBlank() && !email.contains("@")) {
            throw new IllegalArgumentException("E-mail invalido.");
        }
    }

    public boolean isPessoaJuridica() {
        if (cpfCnpj == null) {
            return false;
        }
        return cpfCnpj.replaceAll("\\D", "").length() == 14;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getVendedorResponsavel() {
        return vendedorResponsavel;
    }

    public void setVendedorResponsavel(String vendedorResponsavel) {
        this.vendedorResponsavel = vendedorResponsavel;
    }

    public String getObservacoesFaturamento() {
        return observacoesFaturamento;
    }

    public void setObservacoesFaturamento(String observacoesFaturamento) {
        this.observacoesFaturamento = observacoesFaturamento;
    }

    @Override
    public String toString() {
        return id + " - " + nomeRazaoSocial + " (" + cpfCnpj + ")";
    }
}
