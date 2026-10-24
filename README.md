# SOL Pintura Eletrostática — Cadastro de Cliente (RF-01)

Grupo G4 · Implementação do caso de uso **Cadastrar Cliente**

## Arquitetura em 3 camadas

```
src/main/java/br/ufms/sol/
├── Main.java                          ponto de entrada
├── negocio/
│   └── Cliente.java                   Camada de Negócio
├── persistencia/
│   ├── Conexao.java                   abre e fecha conexão JDBC
│   └── ClienteDAO.java                DAO Pattern (incluir, alterar, excluir, buscar)
└── view/
    └── ClienteView.java               Camada de Interface (Swing)
```

## Como rodar no VS Code

1. Instalar a extensão **Extension Pack for Java** (Microsoft)
2. Abrir esta pasta no VS Code (`File > Open Folder`)
3. Abrir `Main.java` e clicar no **Run** que aparece acima do `main`

O driver do banco já vem na pasta `lib` e o classpath já está configurado em `.vscode/settings.json`. Não precisa instalar Maven nem baixar nada.

Se o VS Code reclamar do `pom.xml` e tentar resolver dependência pela internet, basta apagar o `pom.xml`. O projeto continua rodando pelo `lib`.

## Como rodar sem IDE nenhuma

Windows: duplo clique em `rodar.bat`
Linux / Mac: `./rodar.sh`

Os dois compilam e abrem o programa. Só precisa do JDK instalado (`javac -version` para conferir).

## Como rodar por Maven

```bash
mvn clean package
java -jar target/sol-cadastro-cliente-1.0-jar-with-dependencies.jar
```

## Banco de dados

SQLite em arquivo local. Nenhum servidor, nenhum script para rodar antes. O arquivo `sol.db` e a tabela `cliente` são criados sozinhos na primeira execução, pelo método `Conexao.criarEstrutura()`.

Para zerar o banco, apague o arquivo `sol.db` e rode de novo.

## Requisitos

- JDK 17 ou superior
