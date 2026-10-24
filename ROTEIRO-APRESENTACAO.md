# Roteiro de apresentação — 5 minutos

Grupo G4 · SOL Pintura Eletrostática · Caso de uso Cadastrar Cliente (RF-01)

Deixe o projeto já aberto no IDE e o programa rodando em segundo plano antes de começar.

---

## 0:00 a 0:30 — Abertura

> "O caso de uso que a gente implementou foi o Cadastrar Cliente, o RF-01, que na nossa fase de concepção ficou como prioridade alta e foi alocado na primeira iteração, porque é ele que habilita a geração de orçamento e ordem de serviço.
>
> A implementação é desktop, em Java, separada nas três camadas pedidas: negócio, persistência e interface."

Mostrar a árvore de pacotes no IDE: `negocio`, `persistencia`, `view`.

---

## 0:30 a 1:30 — Camada de Negócio (`Cliente.java`)

Abrir a classe.

> "Na camada de negócio a gente tem a classe Cliente, que espelha exatamente os atributos que levantamos no Documento de Requisitos: nome ou razão social, CPF ou CNPJ, inscrição estadual, telefone, e-mail, endereço, vendedor responsável e observações de faturamento.
>
> Ela não é só um saco de getters e setters. Tem o método `validar`, que centraliza a regra: nome e documento são obrigatórios, o documento tem que ter 11 ou 14 dígitos, e o e-mail precisa ter arroba. Se quebrar, ele lança exceção e quem trata é a tela.
>
> Tem também o `isPessoaJuridica`, que decide pelo tamanho do documento. Isso é regra de negócio, então mora aqui, não na interface."

Descer até o método `validar` e mostrar as três verificações.

---

## 1:30 a 3:00 — Camada de Persistência (`Conexao` e `ClienteDAO`)

Abrir `Conexao.java`.

> "A camada de persistência usa JDBC puro e tem duas classes. A Conexao é só a porta de entrada do banco: `abrir` devolve a conexão e `fechar` encerra. Usamos SQLite em arquivo, então não precisa subir servidor nenhum.
>
> O `criarEstrutura` roda um CREATE TABLE IF NOT EXISTS na inicialização. Isso significa que quem clonar o projeto roda e já funciona, sem script manual."

Abrir `ClienteDAO.java`.

> "E aqui está o DAO, o Data Access Object. A ideia do padrão é que todo o SQL fica preso nessa classe. Nem a tela nem a classe Cliente sabem que existe banco de dados.
>
> Os quatro métodos que o caso de uso pede estão aqui: incluir, alterar, excluir e buscar. As queries ficam em constantes no topo, e todas usam PreparedStatement com parâmetro, que além de organizar evita SQL injection.
>
> O incluir usa `RETURN_GENERATED_KEYS` para pegar o id que o banco gerou e devolver já no objeto. O alterar e o excluir checam o retorno do `executeUpdate`: se veio zero, é porque o id não existe, e a gente lança exceção em vez de deixar passar em silêncio.
>
> E tem dois métodos privados no fim que evitam repetição: o `preencherParametros`, usado pelo insert e pelo update, e o `montarCliente`, que converte uma linha do ResultSet em objeto."

Rolar rápido até `montarCliente`.

---

## 3:00 a 4:00 — Camada de Interface (`ClienteView.java`)

Abrir a classe e mostrar o topo.

> "A interface é Swing. O ponto principal é o que ela não tem: não tem uma linha de SQL, não tem import de JDBC. Ela instancia o DAO e conversa só com objetos Cliente.
>
> A tela tem o formulário com todos os campos em cima, a tabela de clientes no meio com busca por nome ou documento, e os botões Novo, Incluir, Alterar e Excluir embaixo. Clicar numa linha da tabela carrega o cliente no formulário.
>
> O fluxo de cada botão é sempre o mesmo: lê o formulário, monta o objeto, chama o `validar` da camada de negócio, chama o DAO e atualiza a tabela. Os dois tipos de erro são tratados separados: erro de regra vira aviso amarelo, erro de banco vira mensagem de erro."

Mostrar o método `incluir` e os dois `catch`.

---

## 4:00 a 5:00 — Demonstração e fechamento

Rodar o programa e executar, sem pressa:

1. Preencher um cliente e clicar em **Incluir** → aparece na tabela
2. Tentar incluir com o CPF em branco → mostra o aviso da validação
3. Clicar na linha, mudar o telefone e clicar em **Alterar**
4. Digitar parte do nome e clicar em **Buscar**
5. Selecionar e clicar em **Excluir**, confirmando

> "É isso: as quatro operações do caso de uso funcionando, com as três camadas separadas e o DAO isolando o banco. O próximo passo, seguindo nosso plano de iterações, é o RF-04, a tabela de preços, e depois o RF-05, que é a geração do orçamento e da ordem de serviço."

---

## Se perguntarem

**Por que SQLite e não MySQL?**
Só para o trabalho rodar sem setup. A troca é de uma linha, a constante `URL` na classe `Conexao`. Nada mais no projeto sabe qual banco é, justamente por causa do DAO.

**Por que a validação está no Cliente e não na tela?**
Porque se amanhã entrar uma tela web ou uma API, a regra continua valendo. Validação na tela morre junto com a tela.

**Onde entraria a próxima camada?**
Entre a View e o DAO caberia um Controller ou Service, que hoje não existe porque o caso de uso é simples. Quando entrar o orçamento, que cruza cliente, tinta e tabela de preços, aí compensa.
