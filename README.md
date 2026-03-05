# 🐾 AnimalsONG — Backend

API REST desenvolvida para apoiar ONGs de proteção animal, fornecendo os serviços de dados para gestão e adoção de animais resgatados.

> 💡 Este repositório contém apenas o **Backend** da aplicação. Para a interface web, acesse o repositório do **[Frontend →](https://github.com/rafael-blomer/AnimalsONG-Frontend)**

---

## 🌐 Demonstração

A aplicação completa está disponível em produção:

**[https://animalsong-frontend.onrender.com](https://animalsong-frontend.onrender.com)**

---

## 📋 Sobre o Projeto

O **AnimalsONG Backend** é a API REST do sistema AnimalsONG, construída com Java e Spring Boot. Ele fornece todos os endpoints consumidos pelo [AnimalsONG-Frontend](https://github.com/rafael-blomer/AnimalsONG-Frontend), gerenciando animais, ONGs, usuários e processos de adoção.

---

## 🔗 Repositórios do Projeto

| Repositório | Tecnologia | Link |
|---|---|---|
| **Frontend** | Angular 21 + Tailwind CSS | [AnimalsONG-Frontend](https://github.com/rafael-blomer/AnimalsONG-Frontend) |
| **Backend** (este) | Java + Spring Boot + Docker | [AnimalsONG-Backend](https://github.com/rafael-blomer/AnimalsONG-Backend) |

---

## 🚀 Tecnologias

### Core
- **[Java](https://www.java.com/)** — linguagem principal da aplicação
- **[Spring Boot](https://spring.io/projects/spring-boot)** — framework base para criação da API REST
- **[Maven](https://maven.apache.org/)** — gerenciamento de dependências e build

### Banco de Dados
- **[MongoDB Atlas](https://www.mongodb.com/atlas)** — banco de dados NoSQL em nuvem. Toda a persistência da aplicação (animais, usuários, ONGs, adoções) é armazenada no MongoDB Atlas, sem necessidade de configurar banco localmente

### Armazenamento de Imagens
- **[Cloudinary](https://cloudinary.com/)** — plataforma de armazenamento e entrega de imagens na nuvem. Utilizado para upload, gerenciamento e otimização das fotos dos animais cadastrados na plataforma

### Segurança
- **[Spring Security](https://spring.io/projects/spring-security)** — autenticação e autorização da API, protegendo endpoints com controle de acesso baseado em roles e gerenciamento de tokens JWT

### Infraestrutura
- **[Docker](https://www.docker.com/)** — containerização da aplicação para padronizar ambientes de desenvolvimento e produção

---

## ⚙️ Pré-requisitos

Antes de começar, escolha uma das opções de execução:

**Execução local:**
- [Java 21+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/) *(ou use o wrapper `./mvnw` incluído no projeto)*

Além disso, você precisará configurar as variáveis de ambiente para conectar aos serviços externos (veja a seção abaixo).

---

## 🔑 Variáveis de Ambiente

Configure as seguintes variáveis no `application.properties` ou como variáveis de ambiente:

```properties
# MongoDB Atlas
MONGODB_URI=mongodb+srv://<usuario>:<senha>@<cluster>.mongodb.net/<banco>

# Cloudinary
CLOUDINARY_CLOUD_NAME=seu_cloud_name
CLOUDINARY_API_KEY=sua_api_key
CLOUDINARY_API_SECRET=seu_api_secret

# Spring Security / JWT
JWT_SECRET=sua_chave_secreta
```

---

## 🛠️ Instalação e Execução

Clone o repositório:

```bash
git clone https://github.com/rafael-blomer/AnimalsONG-Backend.git
cd AnimalsONG-Backend
```

### Execução local com Maven

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A API estará disponível em [http://localhost:8080](http://localhost:8080).

---

## 🏗️ Build

Para gerar o `.jar` de produção:

```bash
./mvnw clean package
```

O artefato será gerado em `target/`.

---

## 🧪 Testes

```bash
./mvnw test
```

---

## 📁 Estrutura do Projeto

```
AnimalsONG-Backend/
├── src/
│   ├── main/
│   │   ├── java/        # Código-fonte da aplicação
│   │   └── resources/   # Configurações (application.properties)
│   └── test/            # Testes unitários e de integração
├── Dockerfile            # Configuração Docker
├── pom.xml               # Dependências Maven
└── mvnw / mvnw.cmd       # Maven Wrapper
```

---

## 🔌 Integração com o Frontend

Este backend foi desenvolvido para ser consumido pelo [AnimalsONG-Frontend](https://github.com/rafael-blomer/AnimalsONG-Frontend). Para rodar o projeto completo localmente:

1. Inicie este backend (`./mvnw spring-boot:run`)
2. Clone e inicie o [frontend](https://github.com/rafael-blomer/AnimalsONG-Frontend) (`ng serve`)
3. Acesse [http://localhost:4200](http://localhost:4200)

Ou acesse diretamente a versão em produção: **[https://animalsong-frontend.onrender.com](https://animalsong-frontend.onrender.com)**

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga os passos:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/minha-feature`)
3. Commit suas alterações (`git commit -m 'feat: adiciona minha feature'`)
4. Push para a branch (`git push origin feature/minha-feature`)
5. Abra um Pull Request

---

<p align="center">Feito com ❤️ para os animais 🐶🐱</p>
