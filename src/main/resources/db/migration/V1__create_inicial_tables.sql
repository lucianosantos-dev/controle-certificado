CREATE TABLE tb_usuario(
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(150) NOT NULL,
    perfil VARCHAR(15) NOT NULL
);

CREATE TABLE tb_solicitacao(
    id BIGSERIAL PRIMARY KEY,
    nome_aluno VARCHAR(50) NOT NULL,
    curso VARCHAR(50) NOT NULL,
    data_solicitacao DATE NOT NULL,
    data_conclusao DATE NOT NULL,
    data_limite_entrega DATE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    tipo_certificado VARCHAR(20) NOT NULL,
    status_solicitacao VARCHAR(20) NOT NULL DEFAULT('PENDENTE'),
    usuario_id BIGINT,
    FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id)
);