# 🎓 API Controle de Certificados - Inap

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Render](https://img.shields.io/badge/Render-%46E3B7.svg?style=for-the-badge&logo=render&logoColor=white)

API RESTful desenvolvida para o sistema de Controle de Certificados da instituição Inap. O projeto tem como objetivo principal gerenciar de forma segura e eficiente a autenticação de usuários e o fluxo de emissão de certificados para os alunos.

Este repositório contém o **Backend** da aplicação. O frontend foi construído em Angular e consome esta API.

## 💻 Interface de Usuário (Frontend)
O painel visual utilizado pelos alunos e administradores foi desenvolvido em Angular 17. 
👉 **[Acesse o repositório do Frontend clicando aqui](https://github.com/lucianosantos-dev/controle-certificado-front)**

## 🚀 Tecnologias e Arquitetura

O projeto foi construído seguindo princípios de Clean Code e aplicando os padrões mais atuais da indústria para o ecossistema Java:

**Backend:**
* **Java 21+**
* **Spring Boot 4** (Web, Data JPA, Security)
* **Spring Security & JWT (JSON Web Token)** para autenticação e autorização stateless.
* **Flyway** para versionamento e migrações do banco de dados.

**Banco de Dados:**
* **PostgreSQL** hospedado em nuvem (Supabase).

**Infraestrutura e DevOps:**
* **Render:** Hospedagem da API na nuvem.
* **Netlify:** Hospedagem do Frontend (Angular).
* **UptimeRobot:** Monitoramento contínuo (Health Checks) para garantir alta disponibilidade e evitar o cold start (hibernação) do servidor no Render, garantindo um login instantâneo.

## 🛡️ Segurança

A segurança é tratada como prioridade. A API implementa:
* Senhas criptografadas no banco de dados via BCrypt.
* Autenticação via tokens JWT com tempo de expiração.
* Proteção contra vazamento de credenciais: Nenhuma chave secreta (`JWT_SECRET`) ou credencial de banco de dados fica exposta no código-fonte. Todas as informações sensíveis são injetadas no ambiente de produção via **Variáveis de Ambiente**.

## ⚙️ Como executar o projeto localmente

### Pré-requisitos
* Java JDK 21 ou superior
* Maven
* PostgreSQL rodando localmente (porta 5432)

### Passo a Passo

1. **Clone o repositório:**
```bash
   git clone [https://github.com/lucianosantos-dev/controle-certificado.git](https://github.com/lucianosantos-dev/controle-certificado.git)
   cd controle-certificado
