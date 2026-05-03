# ia-order-processing-unit

API para recebimento e estruturação de pedidos em texto livre usando IA.

## Pré-requisitos

- [Docker](https://www.docker.com/) e Docker Compose

## Execução

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd ia-order-processing-unit
   ```

2. Configure as variáveis de ambiente:
   ```bash
   cp .env.example .env
   ```
   Preencha `GEMINI_API_KEY` no `.env` com sua chave da API do Google Gemini.

3. Suba os containers:
   ```bash
   docker compose up --build
   ```

4. Acesse:
   - Frontend: http://localhost:5173
   - API: http://localhost:8080/api/v1

## Endpoints

| Método | Endpoint              | Descrição             |
|--------|-----------------------|-----------------------|
| POST   | `/api/v1/pedido`      | Criar pedido          |
| GET    | `/api/v1/pedidos`     | Listar pedidos        |
| GET    | `/api/v1/pedido/{id}` | Buscar pedido por ID  |

### Exemplo de requisição

```bash
curl -X POST http://localhost:8080/api/v1/pedido \
  -H "Content-Type: application/json" \
  -d '{"texto": "Quero 10 caixas de leite integral e 5 fardos de água para entrega amanhã"}'
```

### Exemplo de resposta

```json
{
  "id": 1,
  "cliente": "desconhecido",
  "itens": [
    { "produto": "leite integral", "quantidade": 10 },
    { "produto": "água", "quantidade": 5 }
  ],
  "data_entrega": "2026-05-04",
  "texto_original": "Quero 10 caixas de leite integral e 5 fardos de água para entrega amanhã",
  "criado_em": "2026-05-03T14:00:00"
}
```

## Uso de IA

O texto livre do pedido é enviado ao **Google Gemini Flash** (`gemini-flash-latest`) via API REST.

O prompt instrui o modelo a retornar exclusivamente um JSON estruturado com os campos `cliente`, `itens` e `data_entrega`, interpretando datas relativas (como "amanhã" ou "semana que vem") com base na data atual.

A lógica está centralizada em `AIParserService`, que monta o prompt, chama a API e processa a resposta.

## Decisões técnicas

**Banco de dados — PostgreSQL**
Escolha para persistência relacional por preferência e experiência. O schema é gerenciado pelo Hibernate com `ddl-auto: update`.

**Frontend — React + TypeScript + Vite + Tailwind CSS**
Stack leve para uma SPA simples. O Vite atua como proxy em desenvolvimento, redirecionando chamadas `/api` para o backend sem expor a URL no browser, é mais leve que o NextJS e por ser ambiente local, não existe necessidade de um nginx, por exemplo. Tailwind para estilização sem overhead de CSS customizado.

**Containerização — Docker Compose**
Três serviços orquestrados: `postgres`, `backend` e `frontend`. O backend aguarda o healthcheck do Postgres antes de iniciar. Dados persistidos em volume Docker.

**Versionamento de API — `/api/v1`**
Prefixo no controller para permitir evolução sem quebrar integrações existentes.

**Rotas da api**
Mantive as rotas solicitadas pelo escopo, porém, na minha opinião, poderia usar `@RequestMapping("pedidos")` para manter o padrão das rotas por contexto.

**Campos em snake_case**
Os campos da API (`data_entrega`, `texto_original`, `criado_em`) seguem snake_case para manter consistência entre o contrato JSON e os tipos TypeScript do frontend. Na minha opinião, o correto seria ter mapeamento para camelCase por convenção da stack.
