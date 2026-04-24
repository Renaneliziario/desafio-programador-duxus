# Desafio de Desenvolvimento Duxus - Sistema de Escalação

Este repositório contém a solução para o desafio técnico de escalação de times, englobando a lógica de processamento de dados em memória, persistência em banco de dados relacional e interfaces de usuário.

## Tecnologias Utilizadas
A arquitetura foi construída utilizando o ecossistema Java atual:
- **Java 17 (LTS):** Uso da Stream API para processamento funcional de dados e Records para imutabilidade de DTOs.
- **Spring Boot 3.2.5:** Framework principal (MVC, Data JPA, Web).
- **PostgreSQL 15:** Banco de dados relacional oficial da aplicação.
- **Docker & Docker Compose:** Containerização da infraestrutura de banco de dados.
- **Swagger (SpringDoc OpenAPI):** Documentação interativa da API REST.
- **Thymeleaf & Vanilla JS:** Motor de templates para renderização das interfaces de usuário.
- **JUnit 5:** Suíte de testes unitários parametrizados.

---

## Como Executar e Testar o Projeto

A infraestrutura foi isolada via Docker para eliminar a necessidade de instalação de dependências locais de banco de dados por parte do avaliador.

### 1. Subindo a Infraestrutura (Banco de Dados)
Na raiz do projeto, certifique-se de que a porta 5432 está livre e execute o comando abaixo para iniciar o PostgreSQL:
```bash
docker-compose up -d
```
*O arquivo docker-compose.yml está pré-configurado para ler as credenciais de segurança do ambiente (renomeie o .env.example para .env se fornecido, ou crie o seu próprio de acordo).*

### 2. Iniciando a Aplicação
Execute a classe principal DuxusdesafioApplication.java através da sua IDE preferida ou utilize o Maven Wrapper:
```bash
./mvnw spring-boot:run
```
*Nota: A aplicação está configurada com ddl-auto=update. As tabelas (integrante, time, composicao_time) e chaves estrangeiras serão criadas automaticamente no banco de dados duxus durante a inicialização.*

---

## Guia de Navegação (Endpoints e Telas)

A aplicação foi dividida em 4 escopos principais, acessíveis com a aplicação executando em localhost:8080.

### 1. Documentação Interativa (Swagger UI)
A forma recomendada para testar as APIs de Cadastro e Processamento (Passos 2 e 3 do desafio) é através da interface OpenAPI:
- **Acesso:** http://localhost:8080/swagger-ui/index.html
*A interface contém exemplos de payloads JSON (Request/Response) formatados conforme os requisitos do desafio.*

### 2. Interface de Usuário (Telas - Passo 4)
Para a experiência completa, foram desenvolvidas telas utilizando Thymeleaf:
- **Cadastro de Integrantes:** http://localhost:8080/telas/integrantes (Formulário para popular o banco de dados).
- **Montagem de Times:** http://localhost:8080/telas/time (Interface para escalação através de seleção múltipla, consumindo os integrantes do banco).

### 3. Validação da Lógica de Dados (Passo 1)
A lógica central do desafio (ApiService) foi testada e refinada para garantir a contagem de integrantes únicos (via operador .distinct()) em cenários de agrupamento. 
Para validar, execute a suíte de testes unitários:
```bash
./mvnw test
```

---

## Decisões de Arquitetura

- **Segurança de Credenciais:** Credenciais de banco de dados não estão fixadas no application.properties. O projeto lê variáveis de ambiente injetadas via .env.
- **Desacoplamento via DTOs:** As entidades JPA (Time, Integrante) não são expostas nas camadas Web. Utiliza-se Records do Java 17 (TimeDTO, TimeResponseDTO) para garantir a segurança dos dados de entrada (anotações @Valid) e evitar recursão infinita na serialização de respostas JSON bidirecionais.
- **Transações:** A lógica de montagem de times no CadastroService é anotada com @Transactional, garantindo consistência relacional entre as tabelas time e composicao_time.
