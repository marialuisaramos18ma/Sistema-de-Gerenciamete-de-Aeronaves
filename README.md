# Sistema de Gerenciamento de Aeronaves e Aeroportos

## Descrição do Projeto

Este projeto é um sistema de gerenciamento de aeronaves e aeroportos que permite o cadastro, edição, exclusão e listagem de aeronaves, aeroportos, usuários e manutenções. Além disso, oferece funcionalidades para calcular a distância entre aeroportos e estimar a autonomia das aeronaves.

## Tecnologias Utilizadas

- **Backend:**
  - **Java com Spring Boot:** Framework robusto para criar aplicações Java com segurança e gerenciamento de dependências.
  - **Spring Data JPA:** Facilita as operações de banco de dados usando ORM (Object-Relational Mapping).
  - **Spring Security:** Gerencia autenticação e autorização dos usuários.

- **Frontend:**
  - **Thymeleaf:** Motor de template usado para renderizar páginas dinâmicas no lado do servidor.
  - **HTML/CSS:** Estrutura e estilização das páginas.
  - **Bootstrap:** Framework CSS para criar layouts responsivos e componentes visuais.

- **Banco de Dados:**
  - **MySQL:** Banco de dados relacional para armazenar todas as informações do sistema.


## Requisitos para Configuração do Projeto

### Pré-requisitos

- **Java JDK 11+:** [Download](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Maven 3.6+:** [Download](https://maven.apache.org/download.cgi)
- **MySQL:** [Download](https://dev.mysql.com/downloads/mysql/)

### Passos para Configuração

1. **Clone o Repositório:**
   ```sh
   git clone https://github.com/seu-usuario/seu-repositorio.git
   cd seu-repositorio

 ### Compilar o Projeto:  
 
 mvn clean install

 ### Executar a Aplicação  

 mvn spring-boot:run

2. **Configure as Credenciais do Banco de Dados:**
   Atualize o arquivo application.properties com as credenciais do seu banco de dados:

3. **Configure o Banco de Dados:**

CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE Aeronave (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(255) NOT NULL,
    tipoCombustivel VARCHAR(255) NOT NULL,
    tipoMotor VARCHAR(255) NOT NULL,
    quantidadeMotores INT NOT NULL,
    capacidadeTanque INT NOT NULL
);

CREATE TABLE Aeroporto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

CREATE TABLE Manutencao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    aeronave_id BIGINT,
    blindagemAeronave BOOLEAN,
    cabineCentralPiloto BOOLEAN,
    motoresTurbinas BOOLEAN,
    assentosCintosSeguranca BOOLEAN,
    tremPouso BOOLEAN,
    dataAtualizacao TIMESTAMP NOT NULL,
    nomeUsuario VARCHAR(255),
    FOREIGN KEY (aeronave_id) REFERENCES Aeronave(id)
);





