# Controle de solicitação e emissão de seguros
Este projeto consiste em um microsserviço de gerenciamento de pedido e emissão de apólices de seguro.
A proposta é o desenvolvimento de um microsserviço que se comunica com alguns recursos externos. 
A seguir explorarei cada um desses recursos e o que foi usado para simular ou mockar a integração com eles.


### Recursos necessários
- JDK17
- Maven
- Docker

### Ambiente de desenvolvimento
- JDK17 - Amazon Corretto 17.0.15
- Maven 3.9.10
- Docker 28.3.1
- IntelliJ IDEA 2025.1.3 (Community Edition) 
- Ubuntu 24.04.2 LTS

## Contrato de API
Para o desenvolvimento da API, utilizei uma dependência do OpenAPI que, a partir de um Swagger, ele disponibiliza 
interfaces a serem implementadas. Este recurso é um habilitador de uma abordagem contract-first. O arquivo de Swagger em
questão fica na raiz do projeto: [openapi.yaml](openapi.yaml).

Em razão dessa característica, é necessário rodar um comando do Maven antes de abrir o código para que as classes 
e interfaces autogeradas estejam disponíveis.

``` bash
mvn clean generate-sources
```

## Docker Compose
A disponibilização dos recursos externos foi realizada por meio do Docker Compose. 

Iniciar os container e disponibilizar os recursos:
``` bash
docker compose up -d
```

Desligar os containers:
``` bash
docker compose down
```

A seguir o detalhamento de cada dos containers

### Banco de dados
Foi utilizado o PostgreSQL na versão 16.3. Os dados estão direcionados para serem gravados 
em um volume montado no diretório `/postgresql-data`, que caso seja necessário limpar

### Serviço de Anti-fraude

### Broker





``` bash
sudo rm -Rf ./postgresql-data
```






docker exec -it rabbitmq sh -c "rabbitmqctl"


PUT /api/exchanges/{vhost}/{name}/publish


### Columns' prefixes
| Purpose                  | Prefix | Datatype        |
|--------------------------|--------|-----------------|
| Identification           | id     | INTEGER, BIGINT |
| Alphanumeric code        | cd     | VARCHAR, CHAR   |
| Date                     | dt     | DATE            |
| Date and time            | ts     | TIMESTAMP       |
| Decimal value            | vl     | DECIMAL         |
| Integer number           | nb     | INTEGER, BIGINT |
| Character for flags      | ch     | CHAR            |
| Alphanumeric name        | nm     | VARCHAR         |
| Alphanumeric description | ds     | VARCHAR         |
| Long text                | tx     | TEXT            |