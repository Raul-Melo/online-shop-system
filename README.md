# Online Shop System

Sistema backend para operações de um mini e-commerce com foco em catálogo de produtos, criação de pedidos e consulta de pedido por ID, seguindo organização em camadas inspirada em Clean Architecture.

## 1. Contexto e Objetivo

Este projeto foi construído para demonstrar maturidade em backend Java com:
- separação clara de responsabilidades;
- regras de negócio explícitas;
- integração com API externa;
- persistência relacional;
- testes automatizados de unidade e integração.

Objetivo principal: disponibilizar uma API REST para fluxo de compra simples com regras de validação de pedido e estoque.

## 2. Escopo Funcional

- Listar produtos (fonte externa Fake Store API, adaptada para o domínio local).
- Criar pedido com dados do cliente, forma de pagamento e itens com validação de estoque.
- Consultar pedido por ID.
- Padronizar retorno de erro em contrato único (`ApiError`).

## 3. Arquitetura (Clean Architecture)

### 3.1 Visão de camadas

- `domain`: entidades e regras de negócio (`Order`, `OrderItem`, `Product`), contratos (ports) de acesso (`ProductGateway`, `OrderRepository`) e enum de pagamento (`PaymentMethod`).
- `application`: DTOs de entrada/saída, interfaces de casos de uso e serviços de orquestração (`CreateOrderUseCaseService`, `GetProductsUseCaseService`, `GetOrderByIdUseCaseService`).
- `infrastructure`: adapters HTTP (controllers), adapters de persistência JPA (entities, mapper, repository), adapter de integração externa (`FakeStoreProductClient`/`Gateway`) e tratamento global de exceções/configurações.

### 3.2 Fluxo de dependências

Controller -> UseCase Service -> Ports do Domain -> Adapters Infrastructure (JPA/External API)

Essa estrutura reduz acoplamento com frameworks e facilita evolução/testabilidade.

### 3.3 Observação arquitetural importante

Foram identificados pontos de acoplamento que devem ser reduzidos para maior aderência à Clean Architecture:
- `domain` depende de exceção da camada `infrastructure` em `Order`;
- `application` depende diretamente de mapper e exceções de `infrastructure`.

Funciona tecnicamente, mas a direção ideal é manter `domain` e `application` independentes de adapters.

## 4. Princípios Aplicados

### 4.1 DDD (Domain-Driven Design)

- Entidades de domínio com invariantes no construtor/factory.
- Regras de negócio centralizadas nas entidades (ex.: cálculo de total em `Order`, subtotal em `OrderItem`).
- Uso de contratos (ports) para persistência e integração externa.

### 4.2 SOLID

- SRP: cada classe possui responsabilidade dominante (ex.: `OrderPersistenceMapper`, `ApiExceptionHandler`).
- OCP: novas integrações podem ser adicionadas implementando ports sem alterar domínio.
- LSP: implementações de `ProductGateway`/`OrderRepository` substituem contratos sem quebrar uso.
- ISP: interfaces pequenas e focadas (`GetProductsUseCase`, `GetOrderByIdUseCase`).
- DIP: casos de uso dependem de abstrações do domínio.

### 4.3 Clean Code

- nomes claros e orientados ao domínio;
- validações explícitas e mensagens de erro diretas;
- testes cobrindo regras críticas e integrações.

## 5. Tecnologias e Versões

- Java: 21
- Spring Boot: 4.0.3
- Maven Wrapper: 3.9.12
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring RestClient
- SpringDoc OpenAPI UI: 3.0.2
- MapStruct: 1.6.3
- Lombok (suporte em entidades)
- Banco principal: MySQL 8.x
- Banco para testes: H2 (in-memory)
- Testes: JUnit 5 + Mockito + Spring Test
- Cobertura: JaCoCo

## 6. Banco de Dados e Modelagem

### 6.1 Entidades persistidas

- `orders`
- `order_items`

### 6.2 Relacionamento

- `orders` 1:N `order_items` (FK `order_id`).
- carregamento de itens otimizado com `@EntityGraph` no repositório Spring Data.

### 6.3 Boas práticas observadas

- uso de `UUID` para identidade de pedido/item;
- colunas monetárias com `precision/scale`;
- cascata e `orphanRemoval` para integridade do agregado.

## 7. Estrutura da API

### 7.1 Endpoints principais

- `GET /products`
- `POST /orders`
- `GET /orders/{id}`

### 7.2 Contrato de erro padronizado

Formato `ApiError` com:
- timestamp;
- status HTTP;
- código interno;
- mensagem amigável;
- path da requisição.

## 8. Integração Externa e Normalização

A integração com Fake Store API é encapsulada por adapter dedicado:
- `FakeStoreProductClient` (HTTP);
- `FakeStoreProductGateway` (implementa port do domínio).

Normalizações aplicadas:
- preço convertido para cenário local (`price * 5.00`);
- estoque simulado padrão (`10`) para fluxo de pedido.

## 9. Validação Técnica Executada

Data de validação: **09/03/2026**

### 9.1 Comandos executados

- `./mvnw -q test`

### 9.2 Resultado

- Testes executados: **52**
- Falhas: **1**
- Erros: **0**

Falha encontrada:
- `CreateOrderUseCaseServiceTest.shouldThrowWhenPaymentMethodIsNull`
- motivo: o serviço valida produtos antes de validar forma de pagamento nula, retornando `ProductNotFoundException` em vez de `InvalidPaymentMethodException`.

## 10. Critérios de Avaliação Aplicados

### Backend

- Organização e separação de responsabilidades: **Atendido com ressalvas** (boa divisão por camadas, porém com pontos de acoplamento entre camadas internas e infraestrutura).
- Clareza de código: **Atendido** (estrutura legível, nomes claros, validações diretas).
- Tratamento de erros: **Atendido** (hierarquia de exceções e handler padronizado).
- Estrutura da API: **Atendido** (endpoints objetivos e contratos de request/response consistentes).

### Banco de Dados

- Modelagem: **Atendido** (entidades coerentes com o domínio de pedidos).
- Relacionamentos: **Atendido** (1:N bem definido e carregamento controlado).
- Boas práticas: **Atendido** (tipagem adequada, constraints e mapeamentos consistentes).

### Integração

- Tratamento da API externa: **Atendido** (encapsulamento por client/gateway e fallback para erro de integração).
- Normalização de dados: **Atendido** (conversão de preço e padronização de estoque).
- Robustez: **Parcialmente atendido** (boa cobertura, mas há ajuste pendente na ordem de validação do caso de uso).

### Maturidade Técnica

- Qualidade geral da solução: **Boa**.
- Clareza no README: **Atualizada neste documento**.
- Capacidade de justificar decisões: **Atendida**, com explicação de arquitetura, escolhas técnicas e próximos passos.

## 11. Guia de Execução Completo

### 11.1 Pré-requisitos

- JDK 21
- MySQL 8.x em execução
- Git
- Internet para baixar dependências Maven (primeira execução)

### 11.2 Configuração do banco

1. Criar banco:

```sql
CREATE DATABASE online_shop_system;
```

2. Ajustar credenciais em `src/main/resources/application.properties` ou sobrescrever por variáveis de ambiente.

### 11.3 Build e execução

```bash
# compilar
./mvnw clean compile

# executar aplicação
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd clean compile
.\mvnw.cmd spring-boot:run
```

### 11.4 Executar testes

```bash
./mvnw test
```

### 11.5 Acessos úteis

- API base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`

### 11.6 Exemplo rápido de criação de pedido

```bash
curl -X POST "http://localhost:8080/orders" \
  -H "Content-Type: application/json" \
  -d '{
    "nome":"Joao",
    "email":"joao@email.com",
    "endereco":"Rua A, 100",
    "formaPagamento":"PIX",
    "produtos":[{"productId":1,"quantity":1}]
  }'
```

## 12. Pontos de Melhoria (Roadmap)

### 12.1 n8n (automações de negócio)

- Criar workflow de pós-pedido com disparo de confirmação por e-mail/WhatsApp, webhook para ERP/logística e atualização de status assíncrona.
- Criar workflow de observabilidade com alerta para falhas de integração com catálogo externo e notificação automática para o time técnico.

### 12.2 CI/CD com GitHub Actions

- Pipeline de CI com build, testes, análise estática, relatório de cobertura (JaCoCo) e gate de qualidade para merge em `develop/main`.
- Pipeline de CD com build de imagem Docker versionada, publish em registry e deploy automatizado por ambiente (dev/hml/prod).

### 12.3 Containerização com Docker

- Criar `Dockerfile` multi-stage para a API.
- Criar `docker-compose.yml` com serviço `api`, serviço `mysql` e volume persistente.
- Benefícios esperados: padronização de ambiente, onboarding mais rápido e maior previsibilidade em execução local e em esteiras.

### 12.4 Evolução arquitetural recomendada

- remover dependência de `infrastructure` nas camadas `domain` e `application`;
- introduzir profile/config segura para credenciais;
- versionar API (`/api/v1/...`);
- adicionar autenticação/autorização (Spring Security + JWT);
- adicionar migration versionada (Flyway/Liquibase).

## 13. Autor e Candidato

- **Nome:** Raul Fernandes Silva Melo
- **E-mail:** raul.fsm692@gmail.com
- **Telefone:** (34) 99258-5048
- **LinkedIn:** _linkedin.com/in/raul-fernandes-silva-melo-0734403a3_
