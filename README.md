# 🎓 API Controle de Certificados - Inap

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Gradle](https://img.shields.io/badge/gradle-%2302303A.svg?style=for-the-badge&logo=gradle&logoColor=white)
![Render](https://img.shields.io/badge/Render-%2346E3B7.svg?style=for-the-badge&logo=render&logoColor=white)

API RESTful desenvolvida para o sistema de Controle de Certificados da instituição Inap. O projeto tem como objetivo principal gerenciar de forma segura e eficiente a autenticação de usuários e o fluxo de emissão de certificados para os alunos.

Este repositório contém o **Backend** da aplicação. O frontend foi construído em Angular e consome esta API.

## 💻 Interface de Usuário (Frontend)

O painel visual utilizado pelos alunos e administradores foi desenvolvido em Angular 21.

👉 **[Acesse o repositório do Frontend clicando aqui](https://github.com/lucianosantos-dev/controle-certificado-front)**

## 🚀 Tecnologias e Arquitetura

O projeto foi construído seguindo princípios de Clean Code e aplicando os padrões mais atuais da indústria para o ecossistema Java:

**Backend:**
* **Java 21+**
* **Spring Boot 4** (Web, Data JPA, Security, Validation)
* **Spring Security & JWT (JSON Web Token)** para autenticação e autorização stateless
* **Flyway** para versionamento e migrações do banco de dados
* **Gradle** como gerenciador de build e dependências

**Banco de Dados:**
* **PostgreSQL** hospedado em nuvem (Supabase)

**Infraestrutura e DevOps:**
* **Docker:** para conteinerização e padronização do ambiente
* **Render:** Hospedagem da API na nuvem
* **Netlify:** Hospedagem do Frontend (Angular)
* **UptimeRobot:** Monitoramento contínuo (Health Checks) para garantir alta disponibilidade e evitar o cold start (hibernação) do servidor no Render, garantindo um login instantâneo

## 🛡️ Segurança

A segurança é tratada como prioridade. A API implementa:

* Senhas criptografadas no banco de dados via BCrypt
* Autenticação via tokens JWT com tempo de expiração
* Proteção contra vazamento de credenciais: nenhuma chave secreta (`JWT_SECRET`) ou credencial de banco de dados fica exposta no código-fonte. Todas as informações sensíveis são injetadas no ambiente de produção via **Variáveis de Ambiente**

## ⚙️ Como executar o projeto localmente

### Pré-requisitos

* Java JDK 21 ou superior
* Gradle (ou use o `gradlew` incluso no projeto, que não exige instalação)
* PostgreSQL rodando localmente (porta 5432) ou Docker

### Passo a passo

**1. Clone o repositório:**
```bash
git clone https://github.com/lucianosantos-dev/controle-certificado.git
cd controle-certificado
```

**2. Configure as variáveis de ambiente**

Crie um arquivo `.env` na raiz do projeto (ou configure diretamente no `application.properties`) com:
```
DB_URL=jdbc:postgresql://localhost:5432/controle_certificado
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta
```

**3. Suba o banco de dados** (opcional, caso use Docker):
```bash
docker compose up -d
```

**4. Rode as migrações e inicie a aplicação:**
```bash
./gradlew bootRun
```

**5. Pronto!** A API estará disponível em:
```
http://localhost:8080
```

## 📄 Licença

Este projeto foi desenvolvido durante estágio na Inap, com fins de aprendizado e uso interno da instituição.
