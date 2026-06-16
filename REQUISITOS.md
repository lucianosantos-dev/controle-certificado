# 🎓 Sistema de Gestão de Certificados

> Uma plataforma centralizada para gerenciar, solicitar e acompanhar a emissão de certificados acadêmicos, garantindo o
> controle rigoroso de prazos e otimizando o fluxo de trabalho entre alunos, secretaria e equipe pedagógica.

---

## 📖 Visão Geral

O objetivo deste sistema é modernizar o fluxo de emissão de certificados. A plataforma oferece à **Secretaria** e ao *
*Pedagógico** controle visual absoluto sobre o prazo rigoroso de **20 dias corridos** para a entrega de documentos,
evitando atrasos. Simultaneamente, fornece aos **Alunos** um ambiente simples e transparente para solicitação e
acompanhamento de seus pedidos.

---

## 👥 Atores do Sistema

* **🛡️ Secretaria / Pedagógico (`SECRETARIA` / `PEDAGOGICO`):** Usuários administrativos com permissão total. Podem
  visualizar o painel geral de solicitações, buscar registros por alunos específicos, acompanhar métricas de urgência (
  SLA) e evoluir o status do processo de emissão.
* **🎓 Aluno (`ALUNO`):** Usuário final. Responsável por realizar seu próprio cadastro simplificado, preencher o
  formulário detalhado para requerer um certificado e acompanhar o andamento de seus pedidos.

---

## ⚙️ Requisitos Funcionais (RF)

### 🔐 Autenticação e Perfil

- **RF01:** O sistema deve possuir um cadastro universal e simplificado de usuários, exigindo obrigatoriamente:
  `Nome Completo`, `E-mail`, `Username`, `Senha` (mínimo de 6 caracteres) e `Perfil de Acesso`.
- **RF02:** O sistema deve realizar a autenticação (login) dos usuários utilizando a combinação estrita de **Username**
  e **Senha**.
- **RF03:** O sistema deve permitir que o aluno visualize um painel exclusivo com suas próprias solicitações,
  acompanhando prazos e status.

### 📝 Módulo de Solicitações (Core Business)

- **RF04:** O aluno logado deve poder enviar uma solicitação preenchendo os dados do escopo acadêmico: `Curso`,
  `Data de Conclusão`, `CPF`, `Telefone` e `Formato de Entrega` (Digital ou Impresso).
- **RF05:** O painel administrativo deve listar de forma paginada e ordenada todas as solicitações cadastradas na base
  de dados.
- **RF06:** O painel administrativo deve possuir um recurso de busca instantânea, filtrando solicitações por **Nome do
  Aluno** ou **CPF**.
- **RF07:** O sistema deve destacar solicitações com base na proximidade do vencimento do prazo de 20 dias (Alertas
  visuais de Urgência/Atenção).
- **RF08:** O administrador deve conseguir abrir os detalhes de uma solicitação específica e alterar seu status de
  processamento.

---

## 🛠️ Requisitos Não-Funcionais (RNF)

- **RNF01 (Persistência):** O banco de dados utilizado deve ser relacional (**PostgreSQL**), aplicando a separação
  arquitetural entre tabelas de Identidade (`usuarios`) e tabelas de Negócio (`solicitacoes`).
- **RNF02 (Segurança):** A autenticação e a proteção das rotas da API devem ser implementadas via **Tokens JWT** (JSON
  Web Tokens).
- **RNF03 (Interface):** O frontend deve ser construído de forma responsiva, seguindo fielmente as especificações do
  protótipo UI/UX (Figma), com suporte total a dispositivos móveis.
- **RNF04 (Autorização):** O controle de acesso aos endpoints deve ser validado por *Roles* de segurança (`SECRETARIA`,
  `PEDAGOGICO` e `ALUNO`).
- **RNF05 (Auditoria):** É terminantemente proibida a exclusão física (*Hard Delete*) de registros. O sistema trabalhará
  exclusivamente com a inativação de registros (*Soft Delete* / Exclusão Lógica).

---

## 📜 Regras de Negócio (RN)

- **RN01 - Unicidade de Acesso:** O sistema garantirá a integridade das contas validando a unicidade exclusiva dos
  campos `E-mail` e `Username`. Não haverá trava de unicidade para CPF ou Telefone nas solicitações, permitindo a
  recompra/novos pedidos no futuro.
- **RN02 - Fluxo Inicial:** Toda nova solicitação enviada pelo aluno nascerá, por padrão, com o status `PENDENTE`.
- **RN03 - Sessão Obrigatória:** Apenas usuários autenticados e portadores da *role* `ALUNO` estão autorizados a
  registrar um pedido de certificado.
- **RN04 - Prazo de SLA:** O prazo limite (Data de Vencimento) será calculado somando-se **20 dias corridos** à data
  exata de registro da requisição no sistema.
- **RN05 - Máquina de Estados:** A evolução administrativa da solicitação obedecerá estritamente ao ciclo de vida (
  ENUM): `PENDENTE` ➔ `CONCLUIDO` ➔ `ENTREGUE`.
- **RN06 - Coleta de Dados Sensíveis:** Informações de contato (CPF e Telefone) não pertencem à conta inicial do
  usuário, sendo exigidas única e exclusivamente no momento oficial da solicitação do documento.
- **RN07 - Prevenção de Duplicidade:** O aluno não poderá ter mais de uma solicitação (em aberto ou concluída) para o
  *mesmo curso*. Um mesmo CPF só poderá abrir novos chamados para *cursos diferentes*.
- **RN08 - Formato do Documento:** É obrigatória a escolha explícita do formato de entrega desejado (ENUM: `DIGITAL` ou
  `IMPRESSO`) durante o preenchimento do formulário.