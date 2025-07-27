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

### Antes de qualquer outra coisa...
Para rodar a aplicação localmente, vá até o diretório onde o repo foi clonado e execute:

``` bash
docker compose up -d
mvn clean spring-boot:run
```

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

A seguir o detalhamento de cada um dos containers.

### Banco de dados

Foi utilizado o PostgreSQL na versão 16.3 que estará disponível na porta `5432` da máquina host.

Dados de conexão:
```
host: localhost
port: 5432
user: usrOwner
pass: abc@123
```

Os dados estão direcionados para serem gravados em um volume montado no diretório `/postgresql-data`.
Caso seja necessário limpar os dados e subir um container "limpo" execute (com o container desligado):

``` bash
sudo rm -Rf ./postgresql-data
```

Para a criação das tabelas e outros objetos de banco de dados foi usado o Liquibase embarcado na aplicação.
Os arquivos de scripts estão aqui: `/src/main/resources/liquibase`.


### Serviço de Anti-fraude

Um mock server foi adotado e estará disponível na porta `1080` da máquina host. 
A seguir um exemplo de request para este container:

```bash
curl --location 'localhost:1080/v1/analysis' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
    "customerId": "b8304e05-0178-4e41-81e2-403df10f9263"
}'
```

O retorno deste mock server está configurado para trazer responses variando a classificação de risco com
base no valor do `customerId` por meio de uma regex considerando o último caracter do UUID.

| Regex          | Exemplo                              | Retorno          |
|----------------|--------------------------------------|------------------|
| `(.){35}[a-f]` | d91d586a-8240-4cdc-a821-17475340c70d | `REGULAR`        |
| `(.){35}[0-3]` | b8304e05-0178-4e41-81e2-403df10f9263 | `HIGH_RISK`      |
| `(.){35}[4-6]` | 2d3e90fc-5672-4fe9-9d68-c292e5249815 | `PREFERRED`      |
| `(.){35}[7-9]` | 2e8de788-af74-4d67-a8b9-25d7efa9ff09 | `NO_INFORMATION` |


### Broker

Para o broker de mensageria foi escolhido o RabbitMQ que ficará disponível na porta `5672` da máquina host para 
operação e na porta `15672` ficará disponível o console de gerenciamento que poderá ser 
acessado em [http://localhost:15672](http://localhost:15672) por meio das credenciais `guest/guest`.

Vale destacar que no startup do container serão criados alguns recursos, mapeados a seguir.

| Recurso           | Finalidade                                                                                                                                                 |
|-------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `orders_topic`    | É o tópico que será sensibilizado a cada alteração de status de um pedido. É para onde vamos enviar as mensagens.                                          |
| `orders_queue`    | Fila destino do `orders_topic`. Servirá para que possamos consultar os eventos gerados e representa algum outro sistema consumidor do nosso.               |
| `insurance_topic` | Tópico origem das atualizações de subscrição de seguros. É onde vamos colocar as mensagens para que possamos receber notificações de subscrição de seguro. |
| `insurance_queue` | Fila destino do `insurance_topic`. Escutaremos essa fila para sermos informados de subscrição de seguro.                                                   |
| `payments_topic`  | Tópico origem das confirmações de pagamento. É onde vamos colocar as mensagens para que possamos receber notificações de confirmação de pagamento.         |
| `payments_queue`  | Fila destino do `payments_topic`. Escutaremos essa fila para sermos informados de confirmação de pagamento.                                                |


## Comandos para testes

A seguir diversos comandos para serem usados de base para comandar diversas ações no programa em execução. 
Atentar-se que os requests que demandam um `order id` são apenas exemplos e que os ids precisam ser substituídos pelos 
ids gerados em tempo de execução dos testes. 

### Criação de pedidos

Pedido de cliente `HIGH_RISK` que vai passar no anti-fraude:
``` bash
curl --location 'http://localhost:8080/api/v1/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "b8304e05-0178-4e41-81e2-403df10f9263",
  "productId": 986554,
  "category": "AUTO",
  "totalMonthlyPremiumAmount": 2587.95,
  "insuredAmount": 240000.52,
  "paymentMethod": "CREDIT_CARD",
  "salesChannel": "MOBILE",
  "coverages": [
    {
      "name": "Roubo",
      "amount": 20000.99
    },
    {
      "name": "Colisão com terceiros",
      "amount": 15000.99
    },
    {
      "name": "Perda Total",
      "amount": 65000.99
    }
  ],
  "assistances": [
    "Guincho até 250km",
    "Troca de Óleo",
    "Chaveiro 24h"
  ]
}'
```

Pedido de cliente `REGULAR` que vai passar no anti-fraude:
``` bash
curl --location 'http://localhost:8080/api/v1/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "d91d586a-8240-4cdc-a821-17475340c70d",
  "productId": 125448,
  "category": "LIFE",
  "totalMonthlyPremiumAmount": 150.65,
  "insuredAmount": 150000.99,
  "paymentMethod": "CREDIT_CARD",
  "salesChannel": "WEB",
  "coverages": [
    {
      "name": "Morte Acidental",
      "amount": 150000
    },
    {
      "name": "Doença grave",
      "amount": 75000.88
    }
  ]
}'
```

Pedido de cliente `PREFERRED` que vai ser barrado no anti-fraude:
``` bash
curl --location 'http://localhost:8080/api/v1/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "2d3e90fc-5672-4fe9-9d68-c292e5249815",
  "productId": 986663,
  "category": "HOME",
  "totalMonthlyPremiumAmount": 3277.44,
  "insuredAmount": 9000000,
  "paymentMethod": "BOLETO",
  "salesChannel": "MOBILE",
  "coverages": [
    {
      "name": "Incêndio",
      "amount": 1200000.99
    },
    {
      "name": "Danos elétricos",
      "amount": 75000
    }
  ],
  "assistances": [
    "Encanador",
    "Eletricista",
    "Chaveiro 24h"
  ]
}'
```

Pedido de cliente `NO_INFORMATION` que vai ser barrado no anti-fraude:
``` bash
curl --location 'http://localhost:8080/api/v1/orders' \
--header 'Content-Type: application/json' \
--data '{
  "customerId": "2e8de788-af74-4d67-a8b9-25d7efa9ff09",
  "productId": 12244,
  "category": "TRAVEL",
  "totalMonthlyPremiumAmount": 3277.44,
  "insuredAmount": 500000,
  "paymentMethod": "BOLETO",
  "salesChannel": "MOBILE",
  "coverages": [
    {
      "name": "Extravio de bagagens",
      "amount": 8540.99
    },
    {
      "name": "Atendimento em hospital",
      "amount": 15000
    }
  ]
}'
```

Obtenção de um pedido pelo id:
``` bash
curl --location 'http://localhost:8080/api/v1/orders/fcb6d06c-0161-4b5b-9666-70dafe1840ab'
```

Obtenção de pedidos pelo customerId:
``` bash
curl --location 'http://localhost:8080/api/v1/orders?customerId=2e8de788-af74-4d67-a8b9-25d7efa9ff09'
```

Cancelamento de pedido:
``` bash
curl --location --request DELETE 'http://localhost:8080/api/v1/orders/84ae6e8e-b5cb-4cab-8e79-e0b74f6e47c2'
```

Consultar Anti-Fraude resultando em `REGULAR`:
``` bash
curl --location 'localhost:1080/v1/analysis' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
    "customerId": "d91d586a-8240-4cdc-a821-17475340c70d"
}'
```

Consultar Anti-Fraude resultando em `HIGH_RISK`:
``` bash
curl --location 'localhost:1080/v1/analysis' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
    "customerId": "b8304e05-0178-4e41-81e2-403df10f9263"
}'
```

Consultar Anti-Fraude resultando em `PREFERRED`:
``` bash
curl --location 'localhost:1080/v1/analysis' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
    "customerId": "2d3e90fc-5672-4fe9-9d68-c292e5249815"
}'
```

Consultar Anti-Fraude resultando em `NO_INFORMATION`:
``` bash
curl --location 'localhost:1080/v1/analysis' \
--header 'Content-Type: application/json' \
--data '{
    "orderId": "e053467f-bd48-4fd2-9481-75bd4e88ee40",
    "customerId": "2e8de788-af74-4d67-a8b9-25d7efa9ff09"
}'
```

Publicar no tópico de **Pagamentos** para receber notificação:
``` bash
curl --location 'localhost:15672/api/exchanges/%2F/payments_topic/publish' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic Z3Vlc3Q6Z3Vlc3Q=' \
--data '{
  "properties": {},
  "routing_key": "",
  "payload": "{\"orderId\":\"b68535ac-fb92-49ae-9366-55880d008e6e\"}",
  "payload_encoding": "string"
}'
```

Publicar no tópico de **Subscrição de Seguro** para receber notificação:
``` bash
curl --location 'localhost:15672/api/exchanges/%2F/insurance_topic/publish' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic Z3Vlc3Q6Z3Vlc3Q=' \
--data '{
  "properties": {},
  "routing_key": "",
  "payload": "{\"orderId\":\"b68535ac-fb92-49ae-9366-55880d008e6e\"}",
  "payload_encoding": "string"
}'
```