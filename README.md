#  Venda de Água — Backend

API REST em **Java + Spring Boot + Spring Security (JWT) + PostgreSQL** para gerenciar
vendas, presença, calendário e finanças de uma equipe que vende água aos sábados.

## Stack

- Java 17
- Spring Boot 3 (Web, Data JPA, Security, Validation)
- PostgreSQL (local ou [Neon](https://neon.tech))
- JWT (jjwt)
- Maven

---

## 1. Pré-requisitos

- [Java 17+ (JDK)](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- Um banco PostgreSQL — local ou na nuvem (ex: [Neon](https://neon.tech), gratuito)

Confirme as instalações no terminal:

```bash
java -version
mvn -version
```

## 2. Clonar e configurar

```bash
git clone <url-deste-repositorio>
cd <pasta-do-repositorio>
cd backend
```

## 3. Rodar o backend

**Linux / macOS:**

```bash
export $(cat .env | xargs)
mvn spring-boot:run
```

**Windows (PowerShell) — use o script incluso, que lê o `.env` automaticamente:**

```powershell
.\run.ps1
```

Se aparecer erro de permissão de execução de script no Windows:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1
```

## 4. Primeiro acesso

Na primeira execução, um administrador é criado automaticamente:

```
E-mail: admin@vendaagua.com
Senha:  admin123
```

**Troque essa senha assim que possível** (edite o próprio usuário admin depois de logar,
ou crie um novo admin e desative/exclua o padrão).


## Regras de negócio principais

- **Meta semanal**: a meta financeira (`Configuracao.metaFinanceira`) é fixa e se
  aplica a cada semana (domingo a sábado), sem acumular entre semanas.
- **Bruto vs. Líquido**: o dashboard sempre retorna os dois valores prontos —
  Bruto = vendas + aportes + taxas de ausência pagas da semana;
  Líquido = Bruto − gastos de reposição da semana. A escolha de qual exibir é feita
  no frontend, não no backend.
- **Fechamento de caixa**: só administradores registram (`POST /api/vendas`),
  com o valor dividido em moedas, notas e pix.
- **Presença e taxa**: membros podem justificar a própria ausência
  (`POST /api/presencas/justificativa`); só o admin marca oficialmente presente/ausente
  (`POST /api/presencas/marcar`) e confirma o pagamento da taxa
  (`PATCH /api/presencas/{id}/taxa-paga`), o que passa a contar na meta da semana.
- **Calendário**: qualquer usuário autenticado vê as datas marcadas
  (`GET /api/dias-de-venda`); só admin adiciona ou remove datas.

## Principais endpoints

| Método | Rota | Quem pode usar | Descrição |
|---|---|---|---|
| POST | `/api/auth/login` | Público | Login, retorna JWT |
| GET | `/api/dashboard` | Autenticado | Painel da semana atual (bruto/líquido) |
| POST/PUT/DELETE | `/api/vendas` | Admin | Fechamento de caixa (moedas/notas/pix) |
| GET | `/api/vendas` | Autenticado | Histórico de fechamentos |
| GET | `/api/presencas?data=` | Autenticado | Lista de presença de uma data |
| POST | `/api/presencas/justificativa` | Autenticado | Membro justifica a própria ausência |
| POST | `/api/presencas/marcar` | Admin | Marca presença/ausência oficial |
| PATCH | `/api/presencas/{id}/taxa-paga` | Admin | Confirma pagamento da taxa |
| GET | `/api/dias-de-venda` | Autenticado | Lista o calendário |
| POST/DELETE | `/api/dias-de-venda` | Admin | Marca/remove data de venda |
| GET/POST/PUT/DELETE | `/api/usuarios` | Admin | Gestão de membros |
| GET/POST/DELETE | `/api/aportes` | Autenticado (POST/DELETE: Admin) | Doações/contribuições extras |
| GET/POST/DELETE | `/api/despesas` | Autenticado (POST/DELETE: Admin) | Gastos de reposição |
| GET/PUT | `/api/configuracao` | Autenticado (PUT: Admin) | Meta semanal e taxa de ausência |

## Estrutura de pastas

```
src/main/java/com/vendaagua/
  config/        -> segurança, JWT, seed do admin inicial
  controller/    -> endpoints REST
  dto/           -> objetos de entrada/saída da API
  exception/     -> tratamento global de erros
  model/         -> entidades JPA
  repository/    -> acesso a dados
  service/       -> regras de negócio
```
