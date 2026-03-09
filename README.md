# Mini Shop – Frontend

Frontend do Mini Shop, um sistema simples de compras online desenvolvido como parte de um desafio técnico. A aplicação permite que o usuário visualize produtos, adicione itens ao carrinho, finalize pedidos e consulte os detalhes de uma compra realizada.

Este frontend foi desenvolvido com foco em:

* organização de código
* separação de responsabilidades
* integração com API REST
* clareza de implementação
* experiência de usuário funcional

---

# Contexto do Projeto

O objetivo do desafio foi implementar um mini sistema de e-commerce composto por backend e frontend.

## Backend (API REST)

Responsável por:

* consumir uma API pública de produtos
* normalizar os dados
* registrar pedidos em banco de dados relacional
* fornecer endpoints REST para o frontend

Principais endpoints:

```
GET /products
POST /orders
GET /orders/:id
```

---

## Frontend

Responsável por:

* exibir catálogo de produtos
* gerenciar carrinho de compras
* realizar checkout
* enviar pedido para o backend
* exibir confirmação do pedido
* consultar detalhes do pedido

---

# Tecnologias Utilizadas

## Frontend

* React 19
* Vite
* JavaScript
* TailwindCSS
* Axios
* React Router

## Ambiente

* Node.js
* NPM

---

# Arquitetura do Frontend

A aplicação foi organizada seguindo uma estrutura modular para manter o código legível e escalável.

```
src
 ├── app
 │   └── App.jsx
 │
 ├── components
 │   ├── ProductCard.jsx
 │   ├── CartItemRow.jsx
 │   ├── CheckoutForm.jsx
 │   ├── OrderSummary.jsx
 │   └── layout
 │
 ├── contexts
 │   └── CartContext.jsx
 │
 ├── hooks
 │   └── useCart.js
 │
 ├── pages
 │   ├── ProductsPage.jsx
 │   ├── CartPage.jsx
 │   ├── CheckoutPage.jsx
 │   ├── OrderSuccessPage.jsx
 │   └── OrderDetailsPage.jsx
 │
 ├── routes
 │   └── AppRoutes.jsx
 │
 ├── services
 │   ├── api.js
 │   ├── productService.js
 │   └── orderService.js
 │
 ├── utils
 │   ├── currency.js
 │   └── validations.js
 │
 └── main.jsx
```

---

# Funcionalidades Implementadas

## Catálogo de Produtos

* listagem de produtos consumida do backend
* cards contendo imagem, nome, descrição, preço e estoque disponível

Endpoint utilizado:

```
GET /products
```

---

## Carrinho de Compras

O carrinho foi implementado utilizando React Context API, permitindo compartilhar estado entre toda a aplicação.

Funcionalidades:

* adicionar produto
* remover produto
* aumentar quantidade
* diminuir quantidade
* cálculo automático do total
* controle de estoque visual

### Controle de estoque no frontend

O estoque exibido no card é calculado dinamicamente:

```
estoque disponível = estoque original - quantidade no carrinho
```

Isso permite:

* reduzir o estoque visual ao adicionar produtos
* restaurar o estoque ao remover itens
* evitar inconsistência de dados

---

## Checkout

O usuário informa:

* nome
* email
* endereço
* forma de pagamento

Métodos suportados:

```
PIX
CARTAO
BOLETO
```

---

## Criação do Pedido

Quando o usuário finaliza a compra, o frontend envia:

```
POST /orders
```

Payload enviado:

```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "endereco": "Rua Exemplo 123",
  "formaPagamento": "PIX",
  "produtos": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Resposta do backend:

```json
{
  "id": "uuid-do-pedido",
  "numeroPedido": "PED-00001"
}
```

---

## Tela de Sucesso

Após a criação do pedido:

* exibe número do pedido
* exibe ID do pedido
* permite navegar para detalhes do pedido

---

## Consulta de Pedido

O usuário pode visualizar os detalhes do pedido:

```
GET /orders/{id}
```

Informações exibidas:

* dados do cliente
* forma de pagamento
* status
* data de criação
* lista de itens
* valor total do pedido

---

# Integração com Backend

A comunicação com a API foi centralizada utilizando Axios.

### services/api.js

```javascript
import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json"
  }
});
```

---

### Serviço de produtos

```
services/productService.js
```

```javascript
api.get("/products")
```

---

### Serviço de pedidos

```
services/orderService.js
```

```javascript
POST /orders
GET /orders/:id
```

---

# Princípios Aplicados

Durante a implementação foram considerados os seguintes princípios:

### Separação de responsabilidades

* services: comunicação com API
* pages: telas da aplicação
* components: componentes reutilizáveis
* contexts: gerenciamento de estado global

---

### Organização

Estrutura modular e organizada para facilitar manutenção e evolução do projeto.

---

### Reutilização

Componentes reutilizáveis como:

* ProductCard
* OrderSummary
* CartItemRow

---

### Clareza de código

* funções pequenas e específicas
* nomenclatura descritiva
* separação entre lógica de negócio e interface

---

### Validação de dados

Validação do formulário de checkout:

* nome obrigatório
* email válido
* endereço obrigatório
* forma de pagamento obrigatória

---

# Instalação do Projeto

## 1. Clonar o repositório

```
git clone <url-do-repositorio>
```

Entrar na pasta:

```
cd online-shop-system-front
```

---

## 2. Instalar dependências

```
npm install
```

---

## 3. Configurar variáveis de ambiente

Criar arquivo `.env`

```
VITE_API_BASE_URL=http://localhost:8080
```

---

## 4. Executar o projeto

```
npm run dev
```

A aplicação ficará disponível em:

```
http://localhost:5173
```

---

# Fluxo de Teste da Aplicação

1. Acessar a página de produtos

```
/products
```

2. Adicionar produtos ao carrinho

3. Acessar o carrinho

```
/cart
```

4. Finalizar compra

```
/checkout
```

5. Preencher formulário de checkout

6. Confirmar pedido

7. Visualizar tela de confirmação

8. Consultar detalhes do pedido

---

# Scripts Disponíveis

Executar em modo desenvolvimento:

```
npm run dev
```

Build para produção:

```
npm run build
```

Preview da build:

```
npm run preview
```

---

# Tecnologias

| Tecnologia   | Uso                     |
| ------------ | ----------------------- |
| React        | Biblioteca de interface |
| Vite         | Build tool              |
| TailwindCSS  | Estilização             |
| Axios        | Comunicação com API     |
| React Router | Navegação               |

---

# Melhorias Futuras

Possíveis melhorias para evolução do projeto:

* autenticação de usuário
* persistência do carrinho
* paginação de produtos
* skeleton loading para carregamento
* tratamento avançado de erros
* testes automatizados

---

# Autor

Raul Fernandes Silva Melo

Email: [raul.fsm692@gmail.com](mailto:raul.fsm692@gmail.com)
Telefone: (34) 99258-5048
LinkedIn: [https://linkedin.com/in/raul-fernandes-silva-melo-0734403a3](https://linkedin.com/in/raul-fernandes-silva-melo-0734403a3)

---
