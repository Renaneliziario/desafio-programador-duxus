# Desafio de Desenvolvimento Duxus - Sistema de Escalação

> 📄 **Documentação Adicional:** [Arquitetura e Design do Sistema](docs/ARCHITECTURE.md)

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

#### No Linux ou macOS:
Certifique-se de que o script possui permissão de execução e inicie a aplicação:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

#### No Windows:
Utilize o prompt de comando ou PowerShell na raiz do projeto:
```cmd
mvnw.cmd spring-boot:run
```

#### Utilizando o Maven Global (Caso instalado):
Caso você já possua o Maven configurado no seu PATH:
```bash
mvn spring-boot:run
```

#### Modo Simplificado (Sem Docker / Banco em Memória):
Caso não queira configurar o PostgreSQL ou Docker, utilize o perfil H2 para rodar tudo em memória:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

*Nota: A aplicação está configurada com ddl-auto=update. As tabelas (integrante, time, composicao_time) e chaves estrangeiras serão criadas automaticamente durante a inicialização.*

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

---

## Atualizações e Melhorias Implementadas (Revisão Técnica)

Para elevar a qualidade e a robustez da solução, a implementação contempla os seguintes pontos:

### 1. Refinamento da Lógica de Dados (ApiService)
*   **Identidade de Time:** O método `integrantesDoTimeMaisRecorrente` foi refatorado para respeitar a definição de que um time é a união de **Clube + Composição**. Utiliza um `record` interno como chave de agrupamento para garantir precisão e performance.
*   **Resiliência e Robustez:** Todos os métodos de processamento possuem proteção contra `NullPointerException`, permitindo que parâmetros de data opcionais (nulos) resultem no processamento correto de todo o histórico, conforme os requisitos.
*   **Código Idiomático:** Uso de Java 17 (Streams, Records, Collectors) para manipulação eficiente de estruturas de dados complexas.

### 2. Expansão da Suíte de Testes
*   **Testes de API (MockMvc):** Inclusão de `ApiControllerTest` para validar os contratos REST. Garante que os endpoints entreguem exatamente o formato JSON solicitado (ex: chaves de objetos e estruturas de listas).
*   **Cobertura de Casos de Borda:** Inclusão de `ApiServiceExperimentoTest` focado em cenários críticos: listas vazias, limites de data inclusivos e filtros nulos.
*   **Validação de Integridade:** Adicionado teste de integridade para monitorar a consistência da massa de dados original, demonstrando atenção à qualidade dos dados de entrada.

### 3. Garantia de Integridade de Dados
*   **Correção de Inconsistências:** Identificada e ajustada uma falha na atribuição de IDs na classe `DadosParaTesteApiService`. A correção garante que os algoritmos de agrupamento operem sobre dados únicos e íntegros.

### 4. Interface e APIs
*   **Endpoints de Processamento:** Implementação completa seguindo os requisitos de parâmetros e retornos esperados.
*   **Telas Funcionais:** Interfaces de Cadastro e Escalação validadas e integradas via JavaScript para consumo das APIs REST de forma assíncrona.
