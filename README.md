# Projeto Cardápio de Drinks (TDE - Programação Orientada a Objetos)

## Visão Geral

Este projeto é um sistema web para gerenciar um cardápio de drinks, desenvolvido como parte do Trabalho Discente Efetivo (TDE) da disciplina de Programação Orientada a Objetos do professor Eduardo Lino[cite: 1]. O sistema permite visualizar drinks, adicionar novos drinks e ver os drinks mais recentemente adicionados.

Conforme os requisitos do TDE:
* O backend é desenvolvido em Java, rodando em um servidor[cite: 3].
* A persistência de dados é feita em um banco de dados relacional MySQL[cite: 3].
* O frontend é construído com HTML, CSS e JavaScript, utilizando a API `fetch` para comunicação com o backend[cite: 2].
* O framework Spring/Spring Boot **não** é utilizado[cite: 4].
* O sistema possui pelo menos 3 páginas com interface gráfica[cite: 2].
* O trabalho pode ser realizado em equipes de até 3 estudantes.
* A data de entrega é 08/06[cite: 5].

## Requisitos de Software

Antes de começar, certifique-se de ter os seguintes softwares instalados:

1.  **Java Development Kit (JDK):** Versão 8 ou superior.
2.  **Apache Maven:** Para gerenciamento de dependências e build do projeto. O IntelliJ IDEA geralmente vem com uma versão embutida.
3.  **IntelliJ IDEA:** Community ou Ultimate Edition.
4.  **MySQL Server:** Versão 5.7 ou superior (MySQL 8+ recomendado).
5.  **MySQL Workbench (Opcional, mas Recomendado):** Para facilitar a administração do banco de dados.
6.  **Apache Tomcat:** Versão 9.x ou 11.x.

## Configuração do Ambiente de Desenvolvimento

Siga estes passos para configurar o projeto no IntelliJ IDEA:

### 1. Clone o Repositório (ou Crie o Projeto Manualmente)

Se o projeto estiver em um repositório Git (ex: GitHub):
```bash
git clone <URL_DO_REPOSITORIO_GIT>
cd <NOME_DA_PASTA_DO_PROJETO>
```

Se você estiver criando o projeto do zero com os códigos fornecidos:
* Siga as instruções de criação de um novo projeto Maven ("Maven Archetype" `maven-archetype-webapp`) no IntelliJ IDEA.
* Copie os arquivos Java, `pom.xml`, arquivos HTML, CSS, JS para as respectivas pastas na estrutura do projeto.

### 2. Abra o Projeto no IntelliJ IDEA

1.  Abra o IntelliJ IDEA.
2.  Se você clonou o repositório, clique em "Open" e navegue até a pasta do projeto clonado. Selecione o arquivo `pom.xml` ou o diretório raiz do projeto.
3.  Se for um projeto Maven, o IntelliJ geralmente detecta o `pom.xml` e pergunta se você deseja importá-lo. Confirme.
4.  Aguarde o IntelliJ IDEA sincronizar o projeto e baixar as dependências do Maven. Você pode ver o progresso na barra de status inferior. Se houver problemas, verifique sua conexão com a internet e as configurações do Maven no IntelliJ.

## Configuração do Banco de Dados (MySQL)

### 1. Crie o Banco de Dados e a Tabela

1.  Abra o MySQL Workbench (ou use o cliente de linha de comando do MySQL).
2.  Conecte-se ao seu servidor MySQL.
3.  Execute os seguintes comandos SQL para criar o banco de dados e a tabela `drinks`:

    ```sql
    CREATE DATABASE IF NOT EXISTS drink_menu_db;
    USE drink_menu_db;

    CREATE TABLE IF NOT EXISTS drinks (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        ingredients TEXT,
        instructions TEXT,
        image_url VARCHAR(255),
        rating INT,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```

### 2. Configure as Credenciais do Banco de Dados no Código

1.  No IntelliJ IDEA, navegue até o arquivo `src/main/java/com/yourname/drinkmenu/dao/DrinkDAO.java`.
2.  Localize as seguintes linhas e atualize com suas credenciais do MySQL:

    ```java
    private String jdbcURL = "jdbc:mysql://localhost:3306/drink_menu_db?useSSL=false&serverTimezone=UTC";
    private String jdbcUsername = "your_mysql_user"; // << Substitua pelo seu usuário MySQL
    private String jdbcPassword = "your_mysql_password"; // << Substitua pela sua senha MySQL
    ```
    * **`your_mysql_user`**: Geralmente `root` para instalações locais, ou um usuário específico que você criou.
    * **`your_mysql_password`**: A senha correspondente ao usuário.

## Configuração do Servidor Tomcat no IntelliJ IDEA

### 1. Adicione um Servidor Tomcat

1.  No IntelliJ IDEA, vá em "Run" (Executar) -> "Edit Configurations..." (Editar Configurações...).
2.  Clique no botão "+" (Add New Configuration) no canto superior esquerdo e selecione "Tomcat Server" -> "Local".
3.  **Name:** Dê um nome para a configuração (ex: `Tomcat TDE Drinks`).
4.  **Application server:** Clique em "Configure..." e aponte para o diretório de instalação do seu Apache Tomcat (ex: `/opt/homebrew/opt/tomcat` ou `/Users/seu_usuario/apache-tomcat-9.0.105`). O IntelliJ deve detectar a versão do Tomcat.
    * *Se você não tem o Tomcat, baixe-o do site oficial do Apache Tomcat e descompacte-o em um local da sua máquina.*
5.  **HTTP port:** Verifique se a porta está configurada para `8080` (ou outra porta de sua preferência, caso a 8080 esteja em uso).

### 2. Configure o Artefato de Implantação (Deployment)

1.  Ainda na janela "Run/Debug Configurations", vá para a aba "Deployment".
2.  Clique no botão "+" no lado direito, selecione "Artifact...".
3.  Escolha o artefato do seu projeto. Geralmente será algo como `drinkmenu:war exploded` (para desenvolvimento, permite hot swap) ou `drinkmenu:war`. `war exploded` é recomendado.
4.  **Application context:** No campo "Application context" que aparece abaixo do artefato adicionado, defina o caminho como `/drinkmenu`. Este será o caminho base da URL da sua aplicação (ex: `http://localhost:8080/drinkmenu`).
5.  Clique em "Apply" e depois em "OK".

## Executando a Aplicação

### 1. Inicie o MySQL Server

Certifique-se de que seu servidor MySQL está em execução. Você pode iniciá-lo através do MySQL Workbench, linha de comando ou como um serviço do sistema.

### 2. Inicie o Servidor Tomcat

1.  No IntelliJ IDEA, selecione a configuração do Tomcat que você criou (ex: `Tomcat TDE Drinks`) na barra de ferramentas superior.
2.  Clique no botão "Run" (ícone de play verde ▶️) ou "Debug" (ícone de inseto verde 🐞).
3.  Aguarde o Tomcat iniciar. Acompanhe os logs na aba "Services" ou "Run" do IntelliJ.
    * **Atenção:** Se você vir um erro `java.net.BindException: Address already in use`, significa que a porta configurada para o Tomcat (ex: 8080) já está sendo usada por outro processo. Você precisará parar esse outro processo ou mudar a porta do Tomcat na configuração do IntelliJ.

### 3. Acesse a Aplicação

Após o Tomcat iniciar e a aplicação ser implantada com sucesso (você verá mensagens como "Artifact ...: Artifact is deployed successfully" nos logs), abra seu navegador web e acesse:

`http://localhost:8080/drinkmenu/`

(Se você configurou uma porta diferente da 8080 ou um contexto de aplicação diferente, ajuste a URL).

Você deverá ver a página inicial da aplicação de cardápio de drinks.

## Estrutura do Projeto (Visão Geral)

```
drinkmenu/
├── pom.xml                   # Arquivo de configuração do Maven (dependências, build)
└── src/
    └── main/
        ├── java/
        │   └── com/yourname/drinkmenu/ # Pacote base para o código Java
        │       ├── model/              # Classes de modelo (ex: Drink.java)
        │       ├── dao/                # Data Access Objects (ex: DrinkDAO.java para interagir com o DB)
        │       └── servlet/            # Servlets Java (ex: ListDrinksServlet.java, AddDrinkServlet.java)
        └── webapp/                   # Raiz dos arquivos web
            ├── WEB-INF/
            │   └── web.xml           # Descritor de implantação (configuração de servlets, welcome files)
            ├── css/                  # Arquivos CSS (ex: style.css)
            ├── js/                   # Arquivos JavaScript (ex: script.js)
            ├── index.html            # Página inicial
            ├── drinks.html           # Página para listar todos os drinks
            └── add-drink.html        # Página para adicionar novos drinks
```

## Solução de Problemas Comuns

### Erros de Dependência do Maven

* **Sintoma:** Mensagens como "Dependency '...' not found" no IntelliJ ou no console Maven.
* **Solução:**
    1.  Verifique sua conexão com a internet.
    2.  No IntelliJ, clique com o botão direito no `pom.xml` -> "Maven" -> "Reload project".
    3.  Tente executar `mvn clean install -U` no terminal do IntelliJ.
    4.  Verifique se há erros de digitação no `pom.xml`.
    5.  Considere limpar o cache local do Maven (excluindo a pasta `.m2/repository`, mas isso fará o download de tudo novamente).

### Conflito de Porta do Tomcat (`java.net.BindException: Address already in use`)

* **Sintoma:** O Tomcat falha ao iniciar com esta mensagem nos logs.
* **Solução:**
    1.  Identifique e pare o processo que está usando a porta (geralmente 8080). No macOS/Linux, use `sudo lsof -i :8080` e depois `sudo kill <PID>`.
    2.  Ou, mude a porta HTTP do Tomcat na configuração do servidor no IntelliJ (Run -> Edit Configurations...).

### Erro HTTP 404 - Not Found

* **Sintoma:** Ao acessar a URL da aplicação, você vê uma página de erro 404 do Tomcat.
* **Solução:**
    1.  **Verifique os logs do Tomcat:** Procure por erros durante a implantação da aplicação `drinkmenu`.
    2.  **Contexto da Aplicação:** Confirme se o "Application context" na configuração de implantação do Tomcat no IntelliJ está correto (ex: `/drinkmenu`).
    3.  **Arquivo de Boas-Vindas:** Garanta que `index.html` existe em `src/main/webapp/` e está listado no `WEB-INF/web.xml` em `<welcome-file-list>`.
    4.  **Artefato Implantado:** Verifique se o artefato correto (`drinkmenu:war exploded`) está sendo implantado.
    5.  **Limpe e Reconstrua:** Pare o Tomcat, execute "Build" -> "Rebuild Project" no IntelliJ e tente iniciar o Tomcat novamente.

---
Este guia deve cobrir os passos para instalar e rodar o código. Boa sorte com seu TDE!
